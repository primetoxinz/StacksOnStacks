package com.tierzero.stacksonstacks.pile;

import com.tierzero.stacksonstacks.network.NetworkHandler;
import com.tierzero.stacksonstacks.network.PileSyncPacket;
import com.tierzero.stacksonstacks.registration.EnumRegisteredItemType;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.ItemStackHandler;

import java.util.stream.Collectors;

public class Pile extends ItemStackHandler{
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
        int dim = container.getWorld().provider.getDimension(), x = container.getPos().getX(), y = container.getPos().getY(), z = container.getPos().getZ();
        NetworkHandler.INSTANCE.sendToAllAround(new PileSyncPacket(this.getStackInSlot(slot), slot),
                new NetworkRegistry.TargetPoint(dim,x,y,z,128));
    }


}
