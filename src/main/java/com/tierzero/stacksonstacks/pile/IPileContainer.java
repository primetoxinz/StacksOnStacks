package com.tierzero.stacksonstacks.pile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public interface IPileContainer {

	IPileContainer getNextPileContainer();
	BlockPos getPos();
	World getWorld();
	Pile getPile();
	boolean onPlayerLeftClick(World world, EntityPlayer player, RayTraceResult rayTraceResult, ItemStack stack);
	boolean onPlayerRightClick(World world, EntityPlayer player, RayTraceResult rayTraceResult, ItemStack stack);
	boolean onPlayerShiftLeftClick(World world, EntityPlayer player, RayTraceResult rayTraceResult, ItemStack stack);
	boolean onPlayerShiftRightClick(World world, EntityPlayer player, RayTraceResult rayTraceResult, ItemStack stack);
	void markDirty();

	NBTTagCompound writeToNBT(NBTTagCompound compound);
	void readFromNBT(NBTTagCompound compound);
}
