package com.tierzero.stacksonstacks.pile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class PileItem implements INBTSerializable<NBTTagCompound> {

	private static final String NBT_TAG_ITEM_STACK = "itemStack";
	private static final String NBT_TAG_RELATIVE_BLOCK_POS = "relativeBlockPos";
	
	private ItemStack itemStack;
	private RelativeBlockPos relativeBlockPos;
	
	public PileItem(ItemStack itemStack, RelativeBlockPos relativeBlockPos) {
		this.itemStack = itemStack;
		this.relativeBlockPos = relativeBlockPos;
	}
	
	public PileItem(NBTTagCompound compoundTag) {
		this.deserializeNBT(compoundTag);
	}

	public ItemStack getItemStack() {
		return itemStack;
	}
	
	public RelativeBlockPos getRelativeBlockPos() {
		return relativeBlockPos;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag(NBT_TAG_ITEM_STACK, itemStack.serializeNBT());
		tag.setTag(NBT_TAG_RELATIVE_BLOCK_POS, relativeBlockPos.serializeNBT());
		return tag;		
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.itemStack = new ItemStack(nbt.getCompoundTag(NBT_TAG_ITEM_STACK));
		this.relativeBlockPos = RelativeBlockPos.getFromDeserializeNBT(nbt);
	}
	
}
