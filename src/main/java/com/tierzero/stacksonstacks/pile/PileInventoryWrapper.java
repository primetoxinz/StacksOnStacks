package com.tierzero.stacksonstacks.pile;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/23/17
 */
public class PileInventoryWrapper implements IItemHandler {
    private Pile pile;

    public PileInventoryWrapper(Pile pile) {
        this.pile = pile;
    }

    @Override
    public int getSlots() {
        return pile.getMaxStoredAmount();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return pile.getItems().get(slot).getItemStack();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

        return null;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {

        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }
}
