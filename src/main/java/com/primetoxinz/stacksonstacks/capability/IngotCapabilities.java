package com.primetoxinz.stacksonstacks.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * Created by tyler on 7/20/16.
 */
public class IngotCapabilities {


    @CapabilityInject(IIngotCount.class)
    public static Capability<IIngotCount> CAPABILITY_INGOT = null;

    public static class CapabilityIngotCount implements Capability.IStorage<IIngotCount> {

        @Override
        public NBTBase writeNBT(Capability<IIngotCount> capability, IIngotCount instance, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<IIngotCount> capability, IIngotCount instance, EnumFacing side, NBTBase nbt) {

        }
    }



}
