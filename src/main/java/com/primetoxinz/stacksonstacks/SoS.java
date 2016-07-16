package com.primetoxinz.stacksonstacks;

import mcmultipart.multipart.MultipartRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = SoS.MODID)
public class SoS {
    public static final String MODID = "stacksonstacks";
    private static final String PROXY = "com.primetoxinz.stacksonstacks.proxy.";
    @Mod.Instance(MODID)
    public static SoS instance;

    @SidedProxy( modId = MODID,clientSide = PROXY+"ClientProxy",serverSide = PROXY+"CommonProxy")
    public static com.primetoxinz.stacksonstacks.proxy.CommonProxy proxy;

    @Mod.EventHandler
    public void pre(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new IngotPlacer());
        MultipartRegistry.registerPart(PartIngot.class, "partIngot");
        proxy.pre(e);
    }
}
