package com.tierzero.stacksonstacks.containers;

import com.tierzero.stacksonstacks.pile.IPileContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class TileContainer extends TileEntity implements IPileContainer {

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
		// TODO Auto-generated method stub
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
