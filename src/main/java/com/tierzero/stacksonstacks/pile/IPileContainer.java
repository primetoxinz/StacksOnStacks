package com.tierzero.stacksonstacks.pile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public interface IPileContainer {

	IPileContainer getNextPileContainer();
	Pile getPile();
	boolean onPlayerLeftClick(World world, EntityPlayer player, RayTraceResult rayTraceResult, ItemStack stack);
	boolean onPlayerRightClick(World world, EntityPlayer player, RayTraceResult rayTraceResult, ItemStack stack);
	boolean onPlayerShiftLeftClick(World world, EntityPlayer player, RayTraceResult rayTraceResult, ItemStack stack);
	boolean onPlayerShiftRightClick(World world, EntityPlayer player, RayTraceResult rayTraceResult, ItemStack stack);
	void markDirty();

}
