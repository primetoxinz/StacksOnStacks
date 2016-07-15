package com.primetoxinz.stacksonstacks.render;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.common.FMLLog;

/**
 * Created by tyler on 7/14/16.
 */
public class ModelLoader implements ICustomModelLoader {
    private static final Model MODEL = new Model();

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.equals(MODEL.LOCATION);
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        FMLLog.info("Loading Stacks On Stacks Model");
        return new Model();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }
}
