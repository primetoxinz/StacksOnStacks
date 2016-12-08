package com.primetoxinz.stacksonstacks.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class IngotCountStorage implements IStorage<IIngotCount> {

	private static final String NBT_TAG_COUNT = "count";
	private static final String NBT_TAG_MAX = "max";

	@Override
	public NBTBase writeNBT(Capability<IIngotCount> capability, IIngotCount instance, EnumFacing side) {
	    NBTTagCompound nbtTag = new NBTTagCompound();
	    nbtTag.setInteger(NBT_TAG_COUNT, instance.getCount());
	    nbtTag.setInteger(NBT_TAG_COUNT, instance.getMax());
        return nbtTag;
	}

	@Override
	public void readNBT(Capability<IIngotCount> capability, IIngotCount instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
		
		instance.setCount(tag.getInteger(NBT_TAG_COUNT));
		instance.setMax(tag.getInteger(NBT_TAG_MAX));
	}
}
