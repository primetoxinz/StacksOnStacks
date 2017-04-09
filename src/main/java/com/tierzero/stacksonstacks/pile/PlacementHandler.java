package com.tierzero.stacksonstacks.pile;

import com.tierzero.stacksonstacks.containers.TilePileContainer;
import com.tierzero.stacksonstacks.registration.RegistrationHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.tierzero.stacksonstacks.StacksOnStacks.CONTAINER;

public class PlacementHandler {

    @SubscribeEvent
    public void onPlayerInteractEvent(PlayerInteractEvent.RightClickBlock event) {

    	World world = event.getWorld();
    	ItemStack heldItemStack = event.getEntityPlayer().getHeldItem(event.getHand());
    	if(canPlaceOn(world,event.getPos(),event.getUseBlock())&& RegistrationHandler.isRegistered(heldItemStack)) {
    		RayTraceResult rayTrace = new RayTraceResult(event.getHitVec(), event.getFace(), event.getPos());
    		createContainer(world, event.getEntityPlayer(), rayTrace, heldItemStack);
    	}
    }

    public void createContainer(World world, EntityPlayer player, RayTraceResult rayTrace, ItemStack stack) {
        BlockPos pos = rayTrace.getBlockPos().offset(rayTrace.sideHit);
        world.setBlockState(pos, CONTAINER.getDefaultState());
        TilePileContainer tile = (TilePileContainer) world.getTileEntity(pos);
        if (player.isSneaking()) {
            tile.onPlayerShiftRightClick(world, player, rayTrace, stack);
        } else {
            tile.onPlayerRightClick(world, player, rayTrace, stack);
        }
    }

    public boolean canPlaceOn(World world, BlockPos pos, Event.Result useBlock) {
        TileEntity tile = world.getTileEntity(pos);
        IBlockState state = world.getBlockState(pos);
        return tile == null && useBlock != Event.Result.ALLOW;
    }
}
