package com.tierzero.stacksonstacks.capability;

import net.minecraft.nbt.NBTTagCompound;

public interface IPile {

	int getStoredAmount();
	int getMaxStoredAmount();

	void deserializeNBT(NBTTagCompound tag);
	NBTTagCompound serializeNBT();
}
