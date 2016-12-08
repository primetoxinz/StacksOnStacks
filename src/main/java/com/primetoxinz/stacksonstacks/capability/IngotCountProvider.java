package com.primetoxinz.stacksonstacks.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * Created by tyler on 7/20/16.
 */
public class IngotCountProvider implements ICapabilitySerializable<NBTBase> {

    @CapabilityInject(IIngotCount.class)
    public static final Capability<IIngotCount> CAPABILITY_INGOT_COUNT = null;

    private IIngotCount instance = CAPABILITY_INGOT_COUNT.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CAPABILITY_INGOT_COUNT;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CAPABILITY_INGOT_COUNT) {
            return CAPABILITY_INGOT_COUNT.cast(instance);
        }
        return null;
    }

	@Override
	public NBTBase serializeNBT() {
		return CAPABILITY_INGOT_COUNT.getStorage().writeNBT(CAPABILITY_INGOT_COUNT, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		CAPABILITY_INGOT_COUNT.getStorage().readNBT(CAPABILITY_INGOT_COUNT, instance, null, nbt);		
	}
}