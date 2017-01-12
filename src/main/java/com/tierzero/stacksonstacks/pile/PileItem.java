package com.tierzero.stacksonstacks.pile;

import com.tierzero.stacksonstacks.registration.RegisteredItem;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class PileItem implements INBTSerializable<NBTTagCompound> {

	private static final String NBT_TAG_REGISTERED_ITEM = "registeredItem";
	private static final String NBT_TAG_RELATIVE_BLOCK_POS = "relativeBlockPos";
	
	private RegisteredItem registeredItem;
	private RelativeBlockPos relativeBlockPos;
	
	public PileItem(RegisteredItem registeredItem, RelativeBlockPos relativeBlockPos) {
		this.registeredItem = registeredItem;
		this.relativeBlockPos = relativeBlockPos;
	}
	
	public RegisteredItem getRegisteredItem() {
		return registeredItem;
	}
	
	public BlockPos getRelativeBlockPos() {
		return relativeBlockPos;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag(NBT_TAG_REGISTERED_ITEM, registeredItem.serializeNBT());
		tag.setTag(NBT_TAG_RELATIVE_BLOCK_POS, relativeBlockPos.serializeNBT());
		return null;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tag) {
		RegisteredItem registeredItem = tag.getTag(NBT_TAG_REGISTERED_ITEM);
		
	}
	
}
