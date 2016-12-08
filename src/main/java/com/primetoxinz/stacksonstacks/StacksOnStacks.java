package com.primetoxinz.stacksonstacks;

import com.primetoxinz.stacksonstacks.capability.IIngotCount;
import com.primetoxinz.stacksonstacks.capability.IngotCount;
import com.primetoxinz.stacksonstacks.capability.IngotCountStorage;
import com.primetoxinz.stacksonstacks.core.CommonProxy;
import com.primetoxinz.stacksonstacks.core.ConfigHandler;
import com.primetoxinz.stacksonstacks.core.LogHandler;
import com.primetoxinz.stacksonstacks.ingot.MultiPartIngot;
import com.primetoxinz.stacksonstacks.lib.LibCore;
import com.primetoxinz.stacksonstacks.logic.IngotFinder;
import com.primetoxinz.stacksonstacks.logic.IngotPlacer;

import mcmultipart.multipart.MultipartRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = LibCore.MOD_ID, name = LibCore.MOD_NAME, version = LibCore.MOD_VERISON, acceptedMinecraftVersions = LibCore.MC_VERISON, dependencies = LibCore.DEP)
public class StacksOnStacks {

    @Mod.Instance(LibCore.MOD_ID)
    public static StacksOnStacks instance;
    
    @SidedProxy(clientSide = LibCore.PROXY_CLIENT, serverSide = LibCore.PROXY_CLIENT)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(IIngotCount.class, new IngotCountStorage(), IngotCount.class);
        
        LogHandler.initLogger(event.getModLog());
        ConfigHandler.initConfig(event.getSuggestedConfigurationFile());
        
        MinecraftForge.EVENT_BUS.register(new IngotPlacer());
        MultipartRegistry.registerPart(MultiPartIngot.class, "partIngot");
        proxy.pre(event);

    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	IngotFinder.findIngotsInOreDictionary();
    }
}
