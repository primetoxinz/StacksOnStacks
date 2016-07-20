package com.primetoxinz.stacksonstacks.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by tyler on 7/20/16.
 */
public class IngotCount implements IIngotCount, INBTSerializable<NBTTagCompound> {
    private int count,max;
    public IngotCount(int max) {
        this.max = max;
    }
    @Override
    public void addIngot() {
        if(count < max)
            count++;
    }

    @Override
    public void removeIngot() {
        if(count > 0)
            count--;
    }

    @Override
    public boolean isFull() {
        return count == max;
    }

    @Override
    public int getIngotCount() {
        return count;
    }

    @Override
    public int getMaxIngotCount() {
        return max;
    }


    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("count",count);
        tag.setInteger("max",max);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        count = tag.getInteger("count");
        max = tag.getInteger("max");
    }
}
