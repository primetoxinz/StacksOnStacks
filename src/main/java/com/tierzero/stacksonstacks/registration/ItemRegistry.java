package com.tierzero.stacksonstacks.registration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

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
		if(!itemStack.isEmpty()) {
			RegisteredItem registeredItem = getRegisteredItem(itemStack);
			if(registeredItem != null) {
				registry.add(registeredItem);
				return true;
			} else {
				registry.add(new RegisteredItem(itemStack.getItem(),itemStack.getMetadata()));
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Attempts to get the RegisteredItem object for the itemstack
	 * @param itemStack - The ItemStack to search for
	 * @return The RegisteredItem object if found, else the default registered item (Stone)
	 */
	@Nonnull
	public RegisteredItem getRegisteredItem(ItemStack itemStack) {
		return registry.stream().filter(registeredItem -> registeredItem.isItemStack(itemStack)).findFirst().orElse(RegisteredItem.DEFAULT);
	}
	
	
	public EnumRegisteredItemType getRegisteredItemType() {
		return type;
	}

	@Override
	public String toString() {
		return String.format("%s : %s", type, registry);
	}
}
