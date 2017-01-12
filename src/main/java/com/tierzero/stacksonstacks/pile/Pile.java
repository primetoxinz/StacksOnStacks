package com.tierzero.stacksonstacks.pile;

import java.util.ArrayList;
import java.util.List;

import com.tierzero.stacksonstacks.registration.EnumRegisteredItemType;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class Pile implements INBTSerializable<NBTTagCompound> {
	
	private static final String NBT_TAG_TYPE = "type";
	private static final String NBT_TAG_STORED_ITEMS = "storedItems";
		
	private EnumRegisteredItemType type;
	private List<PileItem> storedItems;
	private int amountStored;
	
	public Pile(EnumRegisteredItemType type) {
		this.type = type;
		this.storedItems = new ArrayList<PileItem>();
		this.amountStored = 0;
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		
		return null;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {		
	}

}
