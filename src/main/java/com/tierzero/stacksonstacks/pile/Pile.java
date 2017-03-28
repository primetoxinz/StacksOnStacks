package com.tierzero.stacksonstacks.pile;

import com.tierzero.stacksonstacks.capability.IPile;
import com.tierzero.stacksonstacks.registration.EnumRegisteredItemType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Pile implements IPile, INBTSerializable<NBTTagCompound> {

    private static final String NBT_TAG_TYPE = "type";
    private static final String NBT_TAG_STORED_ITEMS = "storedItems";
    private static final String NBT_TAG_STORED_ITEMS_COUNT = "storedItemsCount";

    private EnumRegisteredItemType type;
    private List<PileItem> storedItems;

    public Pile(EnumRegisteredItemType type) {
        this.type = type;
        this.storedItems = new ArrayList<>();

    }

    public boolean addPileItem(World world, EntityPlayer player, RayTraceResult rayTrace, IPileContainer pileContainer, int slot, ItemStack stack) {
        if ((getSlots() - getStoredAmount()) <= 0) {
            IPileContainer nextPileContainer = pileContainer.getNextPileContainer();
            if (nextPileContainer != null) {
                if (player.isSneaking()) {
                    return nextPileContainer.onPlayerShiftRightClick(world, player, rayTrace);
                } else {
                    return nextPileContainer.onPlayerRightClick(world, player, rayTrace);
                }
            }
        }
        storedItems.sort((a, b) -> {
            int c = a.getIndex();
            int d = b.getIndex();
            return c == d ? 0 : c < d ? 1 : -1;
        });
        insertItem(slot, stack, false);
        return true;
    }

    public List<PileItem> getItems() {
        return storedItems;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag(NBT_TAG_TYPE, type.serializeNBT());

        NBTTagCompound storedItemsTag = new NBTTagCompound();
        storedItemsTag.setInteger(NBT_TAG_STORED_ITEMS_COUNT, storedItems.size());
        for (int iter = 0; iter < storedItems.size(); iter++) {
            storedItemsTag.setTag(String.valueOf(iter), storedItems.get(iter).serializeNBT());
        }

        tag.setTag(NBT_TAG_STORED_ITEMS, storedItemsTag);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        this.type = EnumRegisteredItemType.getFromNBT(tag.getCompoundTag(NBT_TAG_TYPE));

        NBTTagCompound storedItemsTag = tag.getCompoundTag(NBT_TAG_STORED_ITEMS);
        for (int iter = 0; iter < tag.getInteger(NBT_TAG_STORED_ITEMS_COUNT); iter++) {
            storedItems.add(new PileItem(storedItemsTag.getCompoundTag(String.valueOf(iter))));
        }
    }

    @Override
    public int getStoredAmount() {
        return storedItems.size();
    }

    @Override
    public int getSlots() {
        return type.getMaxStackSize();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return storedItems.stream().filter(p -> p.getIndex() == slot).findFirst().map(PileItem::getItemStack).orElse(ItemStack.EMPTY);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(!simulate || getStackInSlot(slot).isEmpty()) {
            ItemStack newStack = stack.copy();
            newStack.setCount(1);
            RelativeBlockPos pos =  new RelativeBlockPos(slot);
            PileItem pileItem = new PileItem(newStack,pos);
            storedItems.add(pileItem);
            stack.shrink(1);
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }
}
