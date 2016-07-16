package com.primetoxinz.stacksonstacks.proxy;

import com.primetoxinz.stacksonstacks.render.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
public class ClientProxy extends CommonProxy {
    
    @Override
    public void pre(FMLPreInitializationEvent e) {
        ModelLoaderRegistry.registerLoader(new ModelLoader());
    }

}
