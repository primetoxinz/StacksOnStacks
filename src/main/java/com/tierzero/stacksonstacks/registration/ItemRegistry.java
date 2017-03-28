package com.tierzero.stacksonstacks.registration;

import java.util.ArrayList;
import java.util.List;

import com.tierzero.stacksonstacks.core.ConfigHandler;
import com.tierzero.stacksonstacks.core.LogHandler;
import com.tierzero.stacksonstacks.util.OreDictUtil;

import net.minecraft.item.ItemStack;

public class ItemRegistry {
	
	private EnumRegisteredItemType type;
	private List<ItemStack> registry;
	
	public ItemRegistry(EnumRegisteredItemType type) {
		this.type = type;
		this.registry = new ArrayList<ItemStack>();
		loadFromOreDict();
	}
	
	private void loadFromOreDict() {
		List<ItemStack> stacksToRegister = OreDictUtil.findWithPrefix(type.getName());
		
		if(ConfigHandler.printOnRegistration) {
			LogHandler.logInfo("Registering " + stacksToRegister.size() + type);
		}
		
		for(ItemStack itemStack : stacksToRegister) {
			registry.add(itemStack);
			if(ConfigHandler.printOnRegistration) {
				LogHandler.logInfo("Registering " + itemStack.getDisplayName() + " to Registry " + type);
			}
			
		}
	}
	
	/**
	 * Attempts to register the ItemStack
	 * @param itemStack - The ItemStack to register
	 * @return True if successful, False if not
	 */
	public boolean registerItemStack(ItemStack itemStack) {
		if(!isRegistered(itemStack)) {
			registry.add(itemStack);
			return true;
		}
		
		return false;
	}
	
	public boolean isRegistered(ItemStack itemStack) {
		return registry.stream().anyMatch(stack -> stack.isItemEqual(itemStack));
	}
	
	public EnumRegisteredItemType getRegisteredItemType() {
		return type;
	}

	@Override
	public String toString() {
		return String.format("%s : %s", type, registry);
	}
}
