package com.tierzero.stacksonstacks.pile;

import com.tierzero.stacksonstacks.registration.EnumRegisteredItemType;
import net.minecraftforge.items.ItemStackHandler;

public class Pile extends ItemStackHandler {
    private EnumRegisteredItemType type;
    private IPileContainer container;
    public Pile(EnumRegisteredItemType type,IPileContainer container) {
        super(64);
        this.type = type;
        this.container = container;
    }
    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    public boolean isEmpty() {
        return stacks.isEmpty();
    }

    @Override
    protected void onContentsChanged(int slot) {
        container.markDirty();
    }
}
