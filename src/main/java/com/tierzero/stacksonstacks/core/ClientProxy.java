package com.tierzero.stacksonstacks.core;

import com.tierzero.stacksonstacks.client.TESRPile;
import com.tierzero.stacksonstacks.containers.TileContainer;
import com.tierzero.stacksonstacks.lib.LibCore;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileContainer.class, new TESRPile());
        OBJLoader.INSTANCE.addDomain(LibCore.MOD_ID);
    }
}
