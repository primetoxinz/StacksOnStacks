package com.tierzero.stacksonstacks.pile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public interface IPileContainer {

	public IPileContainer getNextPileContainer();
	public boolean onPlayerLeftClick(World world, EntityPlayer player, RayTraceResult rayTraceResult, ItemStack stack);
	public boolean onPlayerRightClick(World world, EntityPlayer player, RayTraceResult rayTraceResult, ItemStack stack);
	public boolean onPlayerShiftLeftClick(World world, EntityPlayer player, RayTraceResult rayTraceResult, ItemStack stack);
	public boolean onPlayerShiftRightClick(World world, EntityPlayer player, RayTraceResult rayTraceResult, ItemStack stack);
}
