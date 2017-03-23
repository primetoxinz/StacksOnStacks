package com.tierzero.stacksonstacks.pile;

import com.tierzero.stacksonstacks.containers.TileContainer;
import com.tierzero.stacksonstacks.registration.EnumRegisteredItemType;
import com.tierzero.stacksonstacks.registration.RegisteredItem;
import com.tierzero.stacksonstacks.registration.RegistrationHandler;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static com.tierzero.stacksonstacks.StacksOnStacks.CONTAINER;

public class PlacementHandler {
	
	@SubscribeEvent
	public void onPlayerInteractEvent(PlayerInteractEvent.RightClickBlock event) {
	    if(event.getWorld().getTileEntity(event.getPos()) instanceof TileContainer)
	        return;
		if(event.getSide() == Side.SERVER) {
			if (event.getItemStack() != null) {
				RegisteredItem item = RegistrationHandler.getRegisteredItem(event.getItemStack(), EnumRegisteredItemType.INGOT);
				if(item != null) {
                createContainer(event.getWorld(),event.getEntityPlayer(),event.getPos().offset(event.getFace()),item);
                }
			}
		}
	}
	public void createContainer(World world, EntityPlayer player, BlockPos pos, RegisteredItem item) {
	    world.setBlockState(pos, CONTAINER.getDefaultState());
	    //play sound
	    SoundType soundtype = world.getBlockState(pos).getBlock().getSoundType(world.getBlockState(pos),world,pos,player);
	    world.playSound(player,pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS,(soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
    }
}
