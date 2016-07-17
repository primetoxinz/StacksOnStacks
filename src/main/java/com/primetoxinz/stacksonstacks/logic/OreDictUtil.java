package com.primetoxinz.stacksonstacks.logic;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by tyler on 7/16/16.
 */
public class OreDictUtil {
    public static  String[] getItemStackOreNames(ItemStack stack) {
        int[] ids = OreDictionary.getOreIDs(stack);
        String[] names= new String[ids.length];
        for(int i = 0; i < ids.length;i++)
            names[i] = OreDictionary.getOreName(ids[i]);
        return names;
    }
    public static String getOreDictionaryNameStartingWith(ItemStack stack, String start) {
        String value = null;
        if(stack != null) {
            List<String> names = Arrays.asList(getItemStackOreNames(stack));

            if(names.size() == 1 && names.get(0).startsWith(start)) {
                value = names.get(0);
            }
            else {
                Optional<String> ore = names.stream().filter(name -> name.startsWith(start)).findFirst();
                if (ore.isPresent())
                    value = ore.get();
            }
        }

        return value;
    }
    public static ItemStack getCompressIngotBlock(ItemStack stack) {
        String ingot = getOreDictionaryNameStartingWith(stack, "ingot");
        if(ingot != null) {
            List<ItemStack> blocks = OreDictionary.getOres(ingot.replace("ingot","block"));
            if(blocks != null && !blocks.isEmpty())
                return blocks.get(0);
        }
        return null;
    }
}
