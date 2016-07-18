package com.primetoxinz.stacksonstacks.ingot;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by tyler on 7/16/16.
 */
public class DummyStack {
    private Item item;
    private int meta;
    
    public DummyStack(ItemStack stack) {
        this(stack.getItem(), stack.getMetadata());
    }
    public DummyStack(Item item, int meta) {
        this.item = item;
        this.meta = meta;
    }
    
    @Override
    public boolean equals(Object object) {
        return ((DummyStack) object).item == this.item && ((DummyStack) object).meta == this.meta;
    }

    @Override
    public int hashCode() {
        return Item.getIdFromItem(item) * meta;
    }
}