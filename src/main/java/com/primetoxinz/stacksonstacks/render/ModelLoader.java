package com.primetoxinz.stacksonstacks.render;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.primetoxinz.stacksonstacks.SoS;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.FMLLog;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by tyler on 7/14/16.
 */
public class ModelLoader implements ICustomModelLoader {
    public static final ResourceLocation LOCATION =new ModelResourceLocation(SoS.MODID+":partIngot", "multipart") ;
    private static final Model MODEL = new Model();

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.equals(LOCATION);
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        FMLLog.info("Loading Stacks On Stacks Model");
        return new Model();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }
    public static class Model implements IModel {

        @Override
        public Collection<ResourceLocation> getDependencies() {
            return Collections.emptySet();
        }

        @Override
        public Collection<ResourceLocation> getTextures() {
            return ImmutableSet.of(RenderIngot.TEXTURE);
        }

        @Override
        public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
            return new RenderIngot(format);
        }

        @Override
        public IModelState getDefaultState() {
            return TRSRTransformation.identity();
        }
    }
}
