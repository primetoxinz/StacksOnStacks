package com.tierzero.stacksonstacks.pile;

import com.tierzero.stacksonstacks.lib.LibRegistries;
import com.tierzero.stacksonstacks.registration.RegisteredItem;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class PileItem implements INBTSerializable<NBTTagCompound> {

	private static final String NBT_TAG_ITEM_STACK = "itemStack";
	private static final String NBT_TAG_RELATIVE_BLOCK_POS = "relativeBlockPos";
	
	private RegisteredItem registeredItem;
	private RelativeBlockPos relativeBlockPos;
	
	public PileItem(RegisteredItem registeredItem, RelativeBlockPos relativeBlockPos) {
		this.registeredItem = registeredItem;
		this.relativeBlockPos = relativeBlockPos;
	}
	
	public PileItem(NBTTagCompound compoundTag) {
		this.deserializeNBT(compoundTag);
	}

	public RegisteredItem getRegisteredItem() {
		return registeredItem;
	}
	
	public RelativeBlockPos getRelativeBlockPos() {
		return relativeBlockPos;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag(NBT_TAG_ITEM_STACK, registeredItem.asItemStack().serializeNBT());
		tag.setTag(NBT_TAG_RELATIVE_BLOCK_POS, relativeBlockPos.serializeNBT());
		return tag;		
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		ItemStack itemStack = new ItemStack(nbt.getCompoundTag(NBT_TAG_ITEM_STACK));
		
		this.registeredItem = LibRegistries.INGOT_REGISTRY.getRegisteredItem(itemStack);
		this.relativeBlockPos = RelativeBlockPos.getFromDeserializeNBT(nbt);
	}
	
}
