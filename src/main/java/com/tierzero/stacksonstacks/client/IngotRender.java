package com.tierzero.stacksonstacks.client;

import com.tierzero.stacksonstacks.core.LogHandler;
import com.tierzero.stacksonstacks.lib.LibCore;
import com.tierzero.stacksonstacks.pile.RelativeBlockPos;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;

public class IngotRender {

	public static IModel model;
	public static IBakedModel wireFrame;
	
	public IBakedModel bakedModel;
	
	public IngotRender(ResourceLocation textureLocation) {
		if(model == null) {
            try {
				model = ModelLoaderRegistry.getModel(new ResourceLocation(LibCore.MOD_ID, "block/ingot.obj"));
			} catch (Exception e) {
				LogHandler.logError("Could not load ingot model! Please report this to the mod author!");
			}
		}
		
		//TODO - Make this use the texture location to bind the correct texture to the model
		bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.BLOCK, 
                location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
	}
	
	public void renderIngotToBuffer(VertexBuffer buffer, World world, BlockPos renderPosition, RelativeBlockPos relativeRenderPos) {

        GlStateManager.pushMatrix();
	        buffer.setTranslation(relativeRenderPos.getX(), relativeRenderPos.getY(), relativeRenderPos.getZ());
	
	        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
	                world, bakedModel,
	                world.getBlockState(renderPosition),
	                renderPosition,
	                buffer,
	                true);
	        buffer.setTranslation(0, 0, 0);
        GlStateManager.popMatrix();

	}
	
	
}
