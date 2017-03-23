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
				
	public static void registerIngots() {
		List<ItemStack> ingotStacksToRegister = OreDictUtil.findWithPrefix("ingot");
		ItemRegistry ingotRegistry = getItemRegistryForType(EnumRegisteredItemType.INGOT);
		
		LogHandler.logInfo("Registering " + ingotStacksToRegister.size() + " ingots!");
		
		if(ingotRegistry != null) {
			for(ItemStack itemStack : ingotStacksToRegister) {
				if(ingotRegistry.registerItemStack(itemStack)) {
					LogHandler.logInfo("Registering " + itemStack.getDisplayName() + " to Registry " + ingotRegistry.getRegisteredItemType());
				}
			}
		}
	}

	public static RegisteredItem getRegisteredItem(ItemStack stack, EnumRegisteredItemType type) {
		ItemRegistry registry = RegistrationHandler.getItemRegistryForType(type);
		RegisteredItem item = registry.getRegisteredItem(stack);
		return item;
	}

	public static boolean isRegistered(ItemStack stack, EnumRegisteredItemType type) {
		return getRegisteredItem(stack, type) != null;
	}
}
