package com.tierzero.stacksonstacks.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IImmutableNBTSerializer<DeserializeType> extends INBTSerializable<NBTTagCompound> {

	default void deserializeNBT(NBTTagCompound tag) { 
		//NO-OP
	}
	
	DeserializeType getFromDeserializedNBT(NBTTagCompound tag);
	
}
