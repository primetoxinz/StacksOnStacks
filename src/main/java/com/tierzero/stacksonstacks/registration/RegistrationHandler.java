package com.tierzero.stacksonstacks.registration;

import com.tierzero.stacksonstacks.core.LogHandler;
import com.tierzero.stacksonstacks.util.OreDictUtil;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RegistrationHandler {

	private static final List<ItemRegistry> registries = new ArrayList<ItemRegistry>();
	
	public static void loadRegistries() {
		for(EnumRegisteredItemType type : EnumRegisteredItemType.values()) {
			registries.add(new ItemRegistry(type));
		}
	}
	
	public static ItemRegistry getItemRegistryForType(EnumRegisteredItemType type) {
		for(ItemRegistry itemRegistry : registries) {
			if(itemRegistry.getRegisteredItemType() == type) {
				return itemRegistry;
			}
		}
		return null;
	}
	
	public static boolean isRegistered(ItemStack itemStack) {
		return registries.stream().anyMatch(registry -> registry.isRegistered(itemStack));
	}
	
	public static boolean isRegisteredForType(ItemStack itemStack, EnumRegisteredItemType type) {
		ItemRegistry typeRegistry = registries.stream().filter(registry -> registry.getRegisteredItemType().equals(type)).findFirst().get();
	
		return typeRegistry.isRegistered(itemStack);
	}
}
