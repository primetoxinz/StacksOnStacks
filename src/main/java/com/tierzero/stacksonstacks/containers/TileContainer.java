package com.tierzero.stacksonstacks.containers;

import com.tierzero.stacksonstacks.capability.Capabilities;
import com.tierzero.stacksonstacks.pile.IPileContainer;
import com.tierzero.stacksonstacks.pile.Pile;
import com.tierzero.stacksonstacks.pile.PileItem;
import com.tierzero.stacksonstacks.registration.EnumRegisteredItemType;
import com.tierzero.stacksonstacks.registration.RegisteredItem;
import com.tierzero.stacksonstacks.registration.RegistrationHandler;
import com.tierzero.stacksonstacks.util.RelativeBlockPosUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TileContainer extends TileEntity implements IPileContainer {
	Pile pile;

	public TileContainer() {
		this.pile = new Pile(EnumRegisteredItemType.INGOT);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if(capability == Capabilities.CAPABILITY_PILE)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability == Capabilities.CAPABILITY_PILE)
			return Capabilities.CAPABILITY_PILE.cast(pile);
		return super.getCapability(capability, facing);
	}

	@Override
	public IPileContainer getNextPileContainer() {
		return null;
	}

	@Override
	public boolean onPlayerLeftClick(World world, EntityPlayer player, RayTraceResult rayTraceResult) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onPlayerRightClick(World world, EntityPlayer player, RayTraceResult rayTraceResult) {
		if(world.isRemote)
			return false;
		if(player.getActiveItemStack() != null) {
			ItemStack stack = player.getHeldItemMainhand();
			RegisteredItem registeredItem = RegistrationHandler.getRegisteredItem(stack, EnumRegisteredItemType.INGOT);
			if(registeredItem != null) {
				PileItem item = new PileItem(registeredItem, RelativeBlockPosUtils.getRelativeBlockPositionFromMOPHit(rayTraceResult.hitVec));
				if(item != null) {
					if(pile.addPileItem(world,player,rayTraceResult,this,item)) {
						stack.shrink(1);
						return true;
					}
				}
			}
		}


		return false;
	}

	@Override
	public boolean onPlayerShiftLeftClick(World world, EntityPlayer player, RayTraceResult rayTraceResult) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onPlayerShiftRightClick(World world, EntityPlayer player, RayTraceResult rayTraceResult) {
		// TODO Auto-generated method stub
		return false;
	}

}
