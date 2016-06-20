package com.primetoxinz.stacksonstacks.proxy;

import com.primetoxinz.stacksonstacks.SoS;
import com.primetoxinz.stacksonstacks.render.RenderIngot;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPostBake(ModelBakeEvent event) {
        event.getModelRegistry().putObject(new ModelResourceLocation(SoS.MODID+":partIngot", "multipart"), RenderIngot.INSTANCE);
    }
}
