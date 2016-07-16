package com.primetoxinz.stacksonstacks;

import mcmultipart.multipart.MultipartRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = SoS.MODID,version = SoS.MOD_VERISON, acceptedMinecraftVersions = SoS.MC_VERISON, dependencies = SoS.DEP)
public class SoS {
    public static final String MODID = "stacksonstacks";
    public static final String DEP = "required-after:mcmultipart";
    public static final String MC_VERISON = "@MC_VERSION@";
    public static final String MOD_VERISON = "@MOD_VERSION@";

    @Mod.Instance(MODID)
    public static SoS instance;
    private static final String PROXY = "com.primetoxinz.stacksonstacks.proxy.";
    @SidedProxy( modId = MODID,clientSide = PROXY+"ClientProxy",serverSide = PROXY+"CommonProxy")
    public static com.primetoxinz.stacksonstacks.proxy.CommonProxy proxy;

    public static Config config;
    @Mod.EventHandler
    public void pre(FMLPreInitializationEvent e) {
        config = new Config(e.getSuggestedConfigurationFile());
        config.pre();
        MinecraftForge.EVENT_BUS.register(new IngotPlacer());
        MultipartRegistry.registerPart(PartIngot.class, "partIngot");
        proxy.pre(e);
    }
}
