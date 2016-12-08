package com.primetoxinz.stacksonstacks.logic;

import java.util.List;

import com.primetoxinz.stacksonstacks.ingot.IngotRegistry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class IngotFinder {

	public static void findIngotsInOreDictionary() {
		String[] oreNames = OreDictionary.getOreNames();
		
		for(String oreName : oreNames) {
			if(oreName.startsWith("ingot")) {
				List<ItemStack> ores = OreDictionary.getOres(oreName);
				
				for(ItemStack ore : ores) {
					IngotRegistry.registerIngot(ore);
				}
			}
		}
	}
	
}
