package com.tierzero.stacksonstacks.capability;

import com.tierzero.stacksonstacks.pile.Pile;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/22/17
 */
public class Capabilities {
    @CapabilityInject(IPile.class)
    public static Capability<IPile> CAPABILITY_PILE = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IPile.class, new Capability.IStorage<IPile>() {
            @Override
            public NBTBase writeNBT(Capability<IPile> capability, IPile instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<IPile> capability, IPile instance, EnumFacing side, NBTBase nbt) {
                instance.deserializeNBT((NBTTagCompound) nbt);
            }
        }, Pile.class);
    }
}
