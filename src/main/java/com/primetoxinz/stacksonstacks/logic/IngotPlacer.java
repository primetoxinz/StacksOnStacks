package com.primetoxinz.stacksonstacks.logic;

import mcmultipart.block.TileMultipartContainer;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

import static mcmultipart.multipart.MultipartHelper.getPartContainer;
import static net.minecraft.util.EnumActionResult.*;

public class IngotPlacer {
    public static ArrayList<IngotType.DummyStack> ingotRegistry = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    @SideOnly(Side.CLIENT)
    public final void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
        EntityPlayer player = event.getPlayer();
        if (canBeIngot(player.getHeldItemMainhand()) || canBeIngot(player.getHeldItemOffhand())) {
            drawSelectionBox(player, event.getTarget(), 0,  event.getPartialTicks());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public final void placeIngot(PlayerInteractEvent.RightClickBlock e) {
        if (e.getSide().isServer())
        onItemUse(e.getItemStack(), e.getEntityPlayer(), e.getWorld(), e.getPos(), e.getHand(), e.getFace(), e.getHitVec());
    }

    private double round(double num, double r) {
        return ((int) (num * (int) (r))) / r;
    }

    private double relativePos(double x) {
        double pos = ((x > 0) ? Math.floor(x): Math.ceil(x));
        return Math.abs(x - pos);
    }

    private IngotLocation getPositionFromHit(Vec3d hit, EntityPlayer player) {
        double x = relativePos(round(hit.xCoord, 2));
        if ( hit.xCoord < 0) {
            x=(x+.5)%1;
        }
        double y = relativePos(round(hit.yCoord, 8));
        double z = relativePos(round(hit.zCoord, 4));
        if ( hit.zCoord < 0) {
            z=Math.abs(.75-z);
        }
        IngotLocation loc = new IngotLocation(x, y, z, player.getHorizontalFacing().getAxis());
        return loc;
    }

    private static boolean canBeIngot(ItemStack stack) {
        if (stack == null)
            return false;
        IngotType.DummyStack dummy = new IngotType.DummyStack(stack);
        if(ingotRegistry.contains(dummy)) {
            return true;
        } else {
            return getOreDictionaryNameStartingWith(stack,"ingot") != null;
        }
    }

    public static  String[] getItemStackOreNames(ItemStack stack) {
        int[] ids = OreDictionary.getOreIDs(stack);
        String[] names= new String[ids.length];
        for(int i = 0; i < ids.length;i++)
            names[i] = OreDictionary.getOreName(ids[i]);
        return names;
    }
    public static  String getOreDictionaryNameStartingWith(ItemStack stack, String start) {
        String value = null;
        if(stack != null) {
            Collection<String> names = Arrays.asList(getItemStackOreNames(stack));
            Optional<String> ore = names.stream().filter(name -> name.startsWith(start)).findFirst();
            if(ore.isPresent())
                value = ore.get();
        }
        return value;
    }
    public static ItemStack getCompressIngotBlock(ItemStack stack) {
        String ingot = getOreDictionaryNameStartingWith(stack, "ingot");
        if(ingot != null) {
            String block = getOreDictionaryNameStartingWith(null,ingot.replace("ingot", "block"));
            List<ItemStack> blocks = OreDictionary.getOres(block);
            if(blocks != null && !blocks.isEmpty())
                return blocks.get(0);
        }
        return null;
    }
    private boolean canAddPart(World world, BlockPos pos, PartIngot ingot) {
        IMultipartContainer container = getPartContainer(world, pos);
        if (container == null) {
            List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();
            for (AxisAlignedBB bb : list)
                if (!world.checkNoEntityCollision(bb.offset(pos.getX(), pos.getY(), pos.getZ()))) return false;
            Collection<? extends IMultipart> parts = MultipartRegistry.convert(world, pos, true);
            if (parts != null && !parts.isEmpty()) {
                TileMultipartContainer tmp = new TileMultipartContainer();
                for (IMultipart p : parts)
                    tmp.getPartContainer().addPart(p, false, false, false, false, UUID.randomUUID());
                return tmp.canAddPart(ingot);
            }
            return true;
        } else {
            return container.canAddPart(ingot);
        }
    }

    private EnumActionResult onItemUse(final ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, Vec3d hit) {
        if(stack == null || stack.stackSize <= 0)
            return PASS;
        if (canBeIngot(stack) && player.canPlayerEdit(pos, side, stack)) {
            if (player.isSneaking()) {
                Thread thread = new Thread(() -> {
                    long startTime = System.currentTimeMillis();
                    Vec3d h = new Vec3d(0, 0, 0);
                    while (stack.stackSize > 0 && (System.currentTimeMillis()-startTime)<10000) {
                        if(stack.stackSize <= 0)
                            break;
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        boolean p = place(world, pos, side, h, stack, player);
                        if (p) {
                            consumeItem(stack);
                        }
                        h=nextHit(h);
                    }
                });
                thread.start();
                return SUCCESS;
            } else {
                if (place(world, pos, side, hit, stack, player)) {
                    if (!player.isCreative()) consumeItem(stack);
                    return SUCCESS;
                }
            }
            return PASS;
        }
        return FAIL;
    }

    private Vec3d nextHit(Vec3d hit) {
        double x = hit.xCoord;
        double y = hit.yCoord;
        double z = hit.zCoord;
        if(x>.5f) {
            z+=.25f;
            if(z > .75f) {
                y+=.125f;
                z=0;
            }
            x=0;
        } else {
            x+=.5f;
        }
        return new Vec3d(x,y,z);
    }

    private boolean place(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
        if (side != EnumFacing.UP)
            return false;
        if (MultipartHelper.getPartContainer(world, pos) == null) {
            pos = pos.offset(side);
        } else {
            IngotLocation location = getPositionFromHit(hit, player);

            if (location.getRelativeLocation().getY() == 0) {
                pos = pos.offset(side);
            }
        }
        IngotLocation location = getPositionFromHit(hit, player);
        PartIngot part = new PartIngot(location, new IngotType(stack));

        if (canAddPart(world, pos, part)) {
            if (!world.isRemote) {
                try {MultipartHelper.addPart(world, pos, part);}catch(NullPointerException e){}
            }
            SoundType sound = SoundType.METAL;
            if (sound != null)
                world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, sound.getVolume(), sound.getPitch());
            return true;
        }
        return false;

    }

    private void consumeItem(ItemStack stack) {
        if(stack.stackSize <= 0)
            stack = null;
        else
            stack.stackSize--;
    }

    private static void drawSelectionBox(EntityPlayer player, RayTraceResult movingObjectPositionIn, int execute, float partialTicks) {
        World world = player.getEntityWorld();
        if (execute == 0 && movingObjectPositionIn.typeOfHit == RayTraceResult.Type.BLOCK) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
            GlStateManager.glLineWidth(2.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            BlockPos pos = movingObjectPositionIn.getBlockPos();
            IBlockState state = world.getBlockState(pos);
            EnumFacing.Axis facing = player.getHorizontalFacing().getAxis();
            if (state.getMaterial() != Material.AIR && world.getWorldBorder().contains(pos)) {
                double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
                double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
                double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
                AxisAlignedBB block = state.getSelectedBoundingBox(world, pos);
                double x = pos.getX(), y = (block.maxY), z = pos.getZ();
                AxisAlignedBB box;

                for (int i = 1; i <= 4; i++) {
                    for(int j=1;j<=2;j++) {
                        //TODO rotating ingots
//                        if (facing == EnumFacing.Axis.Z) {

//                            box = new AxisAlignedBB(x + (i / 4d) - .25, y, z + (.5 * (j - 1)), x + (i / 4d), y,z + j * .5 ).expandXyz(.002d).offset(-d0, -d1, -d2);
//                        }
//                        else {
                            box = new AxisAlignedBB(x + (.5 * (j - 1)), y, z + (i / 4d) - .25, x + j * .5, y, z + (i / 4d)).expandXyz(.002d).offset(-d0, -d1, -d2);
//                        }

                        drawLine(box);
                    }
                }

            }
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }

    private static void drawLine(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
    }

}