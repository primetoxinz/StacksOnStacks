package com.primetoxinz.stacksonstacks.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

/**
 * Created by tyler on 7/20/16.
 */
public class IngotCountProvider implements INBTSerializable<NBTTagCompound>, ICapabilityProvider {


    private final IngotCount provide;

    public IngotCountProvider(IngotCount provide) {
        this.provide = provide;
    }
    public IngotCountProvider() {
        this.provide = new IngotCount(64);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == IngotCapabilities.CAPABILITY_INGOT;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == IngotCapabilities.CAPABILITY_INGOT)
            return (T) provide;
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return provide.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        provide.deserializeNBT(nbt);
    }
}
