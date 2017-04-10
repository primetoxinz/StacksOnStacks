package com.tierzero.stacksonstacks.pile;

import com.tierzero.stacksonstacks.registration.EnumRegisteredItemType;
import net.minecraftforge.items.ItemStackHandler;

import java.util.stream.Collectors;

public class Pile extends ItemStackHandler{
    //TODO write to nbt
    private EnumRegisteredItemType type;
    private IPileContainer container;
    public Pile(EnumRegisteredItemType type,IPileContainer container) {
        super(64);
        this.type = type;
        this.container = container;
    }

    public int getCount() {
        return stacks.stream().filter( i -> !i.isEmpty()).collect(Collectors.toList()).size();
    }

    public boolean isEmpty() {
        return getCount() <= 0;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }
    @Override
    protected void onContentsChanged(int slot) {
        container.markDirty();
    }


}
