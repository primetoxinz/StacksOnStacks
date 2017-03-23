package com.tierzero.stacksonstacks;

import com.tierzero.stacksonstacks.core.CommonProxy;
import com.tierzero.stacksonstacks.core.ConfigHandler;
import com.tierzero.stacksonstacks.core.LogHandler;
import com.tierzero.stacksonstacks.lib.LibCore;
import com.tierzero.stacksonstacks.pile.PlacementHandler;
import com.tierzero.stacksonstacks.registration.RegistrationHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = LibCore.MOD_ID, name = LibCore.MOD_NAME, version = LibCore.VERSION, dependencies = LibCore.DEPENDENCIES, acceptedMinecraftVersions = LibCore.REQUIRED_VERSION)
public class StacksOnStacks {

	@Instance
	public static StacksOnStacks INSTANCE;
	
	@SidedProxy(clientSide = LibCore.PROXY_CLIENT, serverSide = LibCore.PROXY_COMMON)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		LogHandler.initLogger(event.getModLog());
		ConfigHandler.initConfig(event.getSuggestedConfigurationFile());
		RegistrationHandler.loadRegistries();
		MinecraftForge.EVENT_BUS.register(new PlacementHandler());
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		RegistrationHandler.registerIngots();
	}
	
	@EventHandler
	public static void postInit(FMLInitializationEvent event) {
		
	}
	
}