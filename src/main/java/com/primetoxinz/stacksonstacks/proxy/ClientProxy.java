package com.primetoxinz.stacksonstacks.proxy;

import com.primetoxinz.stacksonstacks.SoS;
import com.primetoxinz.stacksonstacks.render.RenderIngot;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by tyler on 5/28/16.
 */
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPostBake(ModelBakeEvent event) {
        System.out.println("ONPOSTBAKE");
        event.getModelRegistry().putObject(new ModelResourceLocation(SoS.MODID+":partIngot", "multipart"), RenderIngot.INSTANCE);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTextureStitch(TextureStitchEvent.Pre event) {

    }
}
