package com.primetoxinz.stacksonstacks.logic;

import lib.utils.RenderUtils;
import mcmultipart.block.TileMultipartContainer;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static mcmultipart.multipart.MultipartHelper.getPartContainer;
import static net.minecraft.util.EnumActionResult.FAIL;
import static net.minecraft.util.EnumActionResult.SUCCESS;

public class IngotPlacer {
    public static ArrayList<DummyStack> ingotRegistry = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    @SideOnly(Side.CLIENT)
    public final void onDvrawBlockHighlight(DrawBlockHighlightEvent event) {
        EntityPlayer player = event.getPlayer();
        if (canBeIngot(player.getHeldItemMainhand()) || canBeIngot(player.getHeldItemOffhand())) {
            RenderUtils.drawSelectionBox(player, event.getTarget(), 0, event.getPartialTicks());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public final void placeIngot(PlayerInteractEvent.RightClickBlock e) {
        onItemUse(e.getItemStack(), e.getEntityPlayer(), e.getWorld(), e.getPos(), e.getHand(), e.getFace(), e.getHitVec());
    }

    private static double round(double num, double r) {
        return ((int) (num * (int) (r))) / r;
    }

    private static double relativePos(double x) {
        double pos = ((x > 0) ? Math.floor(x): Math.ceil(x));
        return Math.abs(x - pos);
    }

    private static IngotLocation getPositionFromHit(Vec3d hit, EntityPlayer player) {
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

    public static boolean canBeIngot(ItemStack stack) {
        if (stack == null)
            return false;
        DummyStack dummy = new DummyStack(stack);
        if(ingotRegistry.contains(dummy)) {
            return true;
        } else {
            return OreDictUtil.getOreDictionaryNameStartingWith(stack, "ingot") != null;
        }
    }

    private static boolean canAddPart(World world, BlockPos pos, PartIngot ingot) {
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

    private static EnumActionResult onItemUse(final ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, Vec3d hit) {
        if (canBeIngot(stack) && player.canPlayerEdit(pos, side, stack)) {
            if (player.isSneaking()) {
                placeAll(world, pos, side, stack, player);
            } else {
                place(world, pos, side, hit, stack, player);
            }
            return SUCCESS;
        }
        return FAIL;
    }

    private static Vec3d nextHit(Vec3d hit) {
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

    public static void placeAll(World world, BlockPos pos, EnumFacing side, ItemStack stack, EntityPlayer player) {
        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            Vec3d h = new Vec3d(0, 0, 0);
            while (stack.stackSize > 0 && (System.currentTimeMillis() - startTime) < 10000) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                place(world, pos, side, h, stack, player);
                h = nextHit(h);
            }
        }).start();
    }

    public static void place(World world, BlockPos pos, Vec3d hit, ItemStack stack, EntityPlayer player) {
        IngotLocation location = getPositionFromHit(hit, player);
        PartIngot part = new PartIngot(location, new IngotType(stack));
        if (canAddPart(world, pos, part)) {
            if (!world.isRemote) {
                try {
                    MultipartHelper.addPart(world, pos, part);
                } catch (Throwable e) {
                }
            }
            consumeItem(player, stack);
        }
    }

    private static void place(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
        //TODO REWRITE THIS SHIT
        if (side == EnumFacing.UP) {
            IMultipartContainer container = MultipartHelper.getPartContainer(world, pos);
            if (container == null) {
                pos = pos.offset(side);
            } else {
                IngotLocation location = getPositionFromHit(hit, player);
                if (location.getRelativeLocation().getY() == 0 || isContainerFull(container)) {
                    pos = pos.offset(side);
                }
            }
            place(world, pos, hit, stack, player);
        } else {
            if (player.isSneaking() && side.getAxis().isHorizontal()) {
//                if (IngotPlacer.isContainerFull(MultipartHelper.getPartContainer(world, pos))) {
//                    IngotPlacer.placeAll(player.getEntityWorld(), pos, UP, stack, player);
//                } else {
//                    IngotPlacer.placeAll(player.getEntityWorld(), pos.down(), UP, stack, player);
//                }
            }
        }
    }

    public static boolean isContainerFull(IMultipartContainer container) {
        if (container == null)
            return false;
        List<IMultipart> parts = container.getParts().stream().filter(part -> part instanceof PartIngot).collect(Collectors.toList());
        if (parts.size() == 64)
            return true;
        return false;
    }

    private static void consumeItem(EntityPlayer player, ItemStack stack) {
        stack.stackSize--;
        if (stack.stackSize <= 0 && player.getActiveHand() != null)
            player.setHeldItem(player.getActiveHand(), null);
    }


}