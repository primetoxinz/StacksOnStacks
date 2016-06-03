package com.primetoxinz.stacksonstacks;

import net.minecraft.item.ItemStack;
import org.lwjgl.util.Color;

/**
 * Created by tyler on 5/28/16.
 */
class Ingot {
    private Color color;
    private String name;
    private int meta;
    private ItemStack stack;
    public Ingot(ItemStack stack) {
        this.color = createColor(stack);
        this.name = stack.getUnlocalizedName();
        this.meta = stack.getMetadata();
        this.stack = stack;
    }

    ItemStack getStack() {
        return stack;
    }

    private static Color createColor(ItemStack stack) {
        return new Color(0,0,1);
    }

    public Color getColor() {
        return this.color;
    }
    public String getName() {
        return name;
    }
    public int getMeta() { return meta;}

    public ItemStack getItemStack() { return stack;}
}
