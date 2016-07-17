package com.primetoxinz.stacksonstacks.logic;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by tyler on 7/16/16.
 */
public class DummyStack {
    Item item;
    int meta;
    public DummyStack(ItemStack stack) {
        this(stack.getItem(), stack.getMetadata());
    }
    public  DummyStack(Item item, int meta) {
        this.item = item;
        this.meta = meta;
    }
    @Override
    public boolean equals(Object o) {
        return ((DummyStack) o).item == this.item &&
                ((DummyStack) o).meta == this.meta;
    }

    @Override
    public int hashCode() {
        return Item.getIdFromItem(item)*meta;
    }
}