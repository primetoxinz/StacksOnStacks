package com.primetoxinz.stacksonstacks;

import com.primetoxinz.stacksonstacks.proxy.CommonProxy;
import mcmultipart.multipart.MultipartRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


/**
 * Created by tyler on 5/28/16.
 */
@Mod(modid = SoS.MODID)
public class SoS {
    public static final String MODID = "stacksonstacks";
    private static final String PROXY = "com.primetoxinz.stacksonstacks.proxy.";
    @Mod.Instance()
    public static SoS instance;

    @SidedProxy(clientSide = PROXY+"ClientProxy",serverSide = PROXY+"CommonProxy", modId = MODID)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void pre(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new IngotPlacer());
        MultipartRegistry.registerPart(PartIngot.class, "partIngot");
        MinecraftForge.EVENT_BUS.register(proxy);

    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {

    }
    @Mod.EventHandler
    public void post(FMLPostInitializationEvent e) {

    }
}
