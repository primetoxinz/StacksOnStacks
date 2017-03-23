package com.tierzero.stacksonstacks.registration;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemRegistry {
	
	private EnumRegisteredItemType type;
	private List<RegisteredItem> registry;
	
	public ItemRegistry(EnumRegisteredItemType type) {
		this.type = type;
		this.registry = new ArrayList<RegisteredItem>();
	}
	
	/**
	 * Attempts to register the ItemStack
	 * @param itemStack - The ItemStack to register
	 * @return True if successful, False if not
	 */
	public boolean registerItemStack(ItemStack itemStack) {
		//func_190926_b = isEmpty in currentMappings
		if(!itemStack.isEmpty()) {
			RegisteredItem registeredItem = getRegisteredItem(itemStack);
			if(registeredItem != null) {
				registry.add(registeredItem);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Attempts to get the RegisteredItem object for the itemstack
	 * @param itemStack - The ItemStack to search with
	 * @return The RegisteredItem object if found, else null
	 */
	public RegisteredItem getRegisteredItem(ItemStack itemStack) {
		//func_190926_b = isEmpty in currentMappings
		if(!itemStack.isEmpty()) {
			for (RegisteredItem registeredItem : registry) {
				if(registeredItem.isItemStack(itemStack)) {
					return registeredItem;
				}
			}
			return new RegisteredItem(itemStack.getItem(), itemStack.getMetadata());
		}
		
		return null;
	}

	public EnumRegisteredItemType getRegisteredItemType() {
		return type;
	}

	@Override
	public String toString() {
		return String.format("%s : %s", type, registry);
	}
}
