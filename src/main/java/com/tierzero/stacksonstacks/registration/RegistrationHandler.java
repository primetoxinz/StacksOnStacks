package com.tierzero.stacksonstacks.registration;

import java.util.ArrayList;
import java.util.List;

import com.tierzero.stacksonstacks.lib.LibRegistries;
import com.tierzero.stacksonstacks.util.OreDictUtil;

import net.minecraft.item.ItemStack;

public class RegistrationHandler {

	public static void registerUnkownItem(ItemStack itemStack) {
		
	}
	
	public static void registerIngots() {
		List<ItemStack> ingotStacksToRegister = OreDictUtil.findWithPrefix("ingot");
		for(ItemStack itemStack : ingotStacksToRegister) {
			LibRegistries.INGOT_REGISTRY.registerItemStack(itemStack);
		}
	}	
}
