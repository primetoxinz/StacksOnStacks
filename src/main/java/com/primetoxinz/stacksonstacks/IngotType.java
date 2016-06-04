package com.primetoxinz.stacksonstacks;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.util.Color;

/**
 * Created by tyler on 6/4/16.
 */
public class IngotType {
    public Color color;
    public ItemStack stack;
    public IngotType(ItemStack stack, Color color) {
        if(stack != null) {
            stack = stack.copy();
            stack.stackSize=1;
        }
        this.stack = stack;
        this.color = color;
    }
    public IngotType(ItemStack stack) {
        this(stack,createColor(stack));
    }
    private static Color createColor(ItemStack stack) {
        return (Color) Color.BLACK;
    }
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if(stack != null)
            stack.writeToNBT(tag);
        return tag;
    }
    public static IngotType readFromNBT(NBTTagCompound tag) {
        ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
        return new IngotType(stack);
    }


}
