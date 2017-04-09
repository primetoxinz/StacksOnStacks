package com.tierzero.stacksonstacks.registration;

import net.minecraft.nbt.NBTTagCompound;

public enum EnumRegisteredItemType {
	
	//TODO - Max stack sizes based on config?
	UNKNOWN("unknown", 1),
	INGOT("ingot", 1);
	
	private static final String NBT_TAG_NAME = "name";
	
	private String name;
	private int maxStackSize;
	
	EnumRegisteredItemType(String name, int maxStackSize) {
		this.name = name;
		this.maxStackSize = maxStackSize;
	}
	
	public String getName() {
		return name;
	}
	
	public int getMaxStackSize() {
		return maxStackSize;
	}

	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString(NBT_TAG_NAME, name);
		
		return tag;
	}

	public static EnumRegisteredItemType getFromNBT(NBTTagCompound tag) {
		if(tag.hasKey(NBT_TAG_NAME)) {
			String name = tag.getString(NBT_TAG_NAME);
			
			for (EnumRegisteredItemType type : EnumRegisteredItemType.values()) {
				if(type.getName().equals(name)) {
					return type;
				}
			}
		} 
		
		return EnumRegisteredItemType.UNKNOWN;
	}
	
}

