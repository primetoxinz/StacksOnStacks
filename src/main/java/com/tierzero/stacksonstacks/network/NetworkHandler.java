package com.tierzero.stacksonstacks.network;

import com.tierzero.stacksonstacks.lib.LibCore;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class NetworkHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(LibCore.MOD_ID);
}
