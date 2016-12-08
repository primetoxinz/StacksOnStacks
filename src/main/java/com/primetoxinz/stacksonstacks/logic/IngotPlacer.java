package com.primetoxinz.stacksonstacks.logic;

import static net.minecraft.util.EnumActionResult.FAIL;
import static net.minecraft.util.EnumActionResult.SUCCESS;

import javax.annotation.Nonnull;

import com.primetoxinz.stacksonstacks.capability.IIngotCount;
import com.primetoxinz.stacksonstacks.capability.IngotCountProvider;
import com.primetoxinz.stacksonstacks.core.LogHandler;
import com.primetoxinz.stacksonstacks.ingot.IngotLocation;
import com.primetoxinz.stacksonstacks.ingot.IngotRegistry;
import com.primetoxinz.stacksonstacks.ingot.MultiPartIngot;

import lib.utils.RenderUtils;
import mcmultipart.block.TileMultipartContainer;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IngotPlacer {

    @SubscribeEvent(priority = EventPriority.LOW)
    @SideOnly(Side.CLIENT)
    public final void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
        EntityPlayer player = event.getPlayer();
        if (IngotRegistry.isIngotRegistered(player.getHeldItemMainhand()) || IngotRegistry.isIngotRegistered(player.getHeldItemOffhand())) {
            RenderUtils.drawSelectionBox(player, event.getTarget(), 0, event.getPartialTicks());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, Vec3d hit) {
        if (IngotRegistry.isIngotRegistered(stack) && player.canPlayerEdit(pos, side, stack)) {
        	handleIngotPlacement(world, pos, side, hit, stack, player);
            return SUCCESS;
        }
        return FAIL;
    }
    
    private static void handleIngotPlacement(World world, BlockPos placePos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
        IMultipartContainer container = MultipartHelper.getPartContainer(world, placePos);
      
        if (container != null) {
            if(!isContainerFull((TileMultipartContainer) container) {
            	if(canAddIngotToContainer(world, pos, ingot)) {

            	
            } else {
            	handleIngotPlacement(world, placePos.up(), side, hit, stack, player);

            }
        } else {
            if(world.getTileEntity(pos) != null)
                return;
            placePos = pos.offset(side);
        }
    }

    private static boolean canAddIngotToContainer(IMultipartContainer container, MultiPartpart ingot) {
        IMultipartContainer container = getPartContainer(world, pos);
        if (container != null) {
            return container.canAddPart(ingot);
        }
        
        return false;
    }

 /*
    if(!world.getBlockState(pos).getMaterial().isReplaceable())
        return false;
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
   */
    private static Vec3d nextHit(Vec3d hit) {
        double x = hit.xCoord;
        double y = hit.yCoord;
        double z = hit.zCoord;
        
        double xReflection = 1/2;
        double zRelection = 3/4;
        
        double xOffset = 1/2;
        double yOffset = 1/8;
        double zOffset = 1/4;
        if(x > xReflection) {
            z += zOffset;
            if(z > zRelection) {
                y += yOffset;
                z=0;
            }
            x = 0;
        } else {
            x += xOffset;
        }
        return new Vec3d(x,y,z);
    }



    private static void placeAll(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            Vec3d hit = new Vec3d(0, 0, 0);
            while (stack.stackSize > 0 && (System.currentTimeMillis() - startTime) < 2000) {
                place(world, pos, hit, stack, player);
                hit = nextHit(hit);
            }
        }).start();
    }

    private static void place(World world, BlockPos pos, Vec3d hit, ItemStack stack, EntityPlayer player) {
    	LogHandler.logInfo("Placing Ingot");
        IngotLocation location = IngotLocation.fromHit(hit, player.getHorizontalFacing().getAxis());
        MultiPartIngot part = new MultiPartIngot(location, IngotRegistry.getRegisteredIngot(stack));
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

    public static boolean isContainerFull(@Nonnull TileMultipartContainer container) {
        if (container.hasCapability(IngotCountProvider.CAPABILITY_INGOT_COUNT, null)) {
            IIngotCount cap = container.getCapability(IngotCountProvider.CAPABILITY_INGOT_COUNT, null);
            return cap.isFull();
        }
        
        return false;
    }

    private static void consumeItem(EntityPlayer player, ItemStack stack) {
        if (!player.isCreative()) {
            stack.stackSize--;
            if (stack.stackSize <= 0 && player.getActiveHand() != null) {
                player.setHeldItem(player.getActiveHand(), null);
            }
        }
        

    }


}