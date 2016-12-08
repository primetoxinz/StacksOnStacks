package com.primetoxinz.stacksonstacks.core;

import com.primetoxinz.stacksonstacks.StacksOnStacks;
import com.primetoxinz.stacksonstacks.ingot.MultiPartIngot;
import com.primetoxinz.stacksonstacks.render.ModelLoader;
import mcmultipart.client.multipart.MultipartRegistryClient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
public class ClientProxy extends CommonProxy {
    
    @Override
    public void pre(FMLPreInitializationEvent e) {
        ModelLoaderRegistry.registerLoader(new ModelLoader());
        //MultipartRegistryClient.registerColorProvider(new ResourceLocation(SoS.MODID,"partIngot"),new MultiPartIngot());
    }

}
