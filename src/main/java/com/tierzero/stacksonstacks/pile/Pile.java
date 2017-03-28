package com.tierzero.stacksonstacks.pile;

import com.tierzero.stacksonstacks.registration.EnumRegisteredItemType;
import net.minecraftforge.items.ItemStackHandler;

public class Pile extends ItemStackHandler  {

    private EnumRegisteredItemType type;
    public Pile(EnumRegisteredItemType type) {
        super(64);
        this.type = type;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }
}
