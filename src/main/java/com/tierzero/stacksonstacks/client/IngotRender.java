package com.tierzero.stacksonstacks.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tierzero.stacksonstacks.core.LogHandler;
import com.tierzero.stacksonstacks.lib.LibCore;
import com.tierzero.stacksonstacks.pile.RelativeBlockPos;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IngotRender {

    public static IModel model = loadModel();
    public static IBakedModel bakedModel = loadBakedModel();

    private static IModel loadModel() {
        try {
            return ModelLoaderRegistry.getModel(new ResourceLocation(LibCore.MOD_ID, "block/ingot.obj"));
        } catch (Exception e) {
            LogHandler.logError("Could not load ingot model! Please report this to the mod author!");
        }
        return null;
    }

    public static IBakedModel loadBakedModel() {
        return model.bake(TRSRTransformation.identity(), DefaultVertexFormats.BLOCK,
                location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
    }

    public static void renderIngotToBuffer(VertexBuffer buffer, World world, BlockPos renderPosition, RelativeBlockPos relativeRenderPos, ItemStack stack) {
        GlStateManager.pushMatrix();
        buffer.setTranslation(relativeRenderPos.getX(), relativeRenderPos.getY(), relativeRenderPos.getZ());
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
                world,
                bakedModel,
                world.getBlockState(renderPosition),
                renderPosition,
                buffer,
                false);
        buffer.setTranslation(0, 0, 0);
        
        int averageColor = getAverageColor(stack);
        for (int i = 0; i < 24; i++) {
        	putColor(buffer, averageColor, i + 1);
        }
        
        GlStateManager.popMatrix();
    }

    private static void putColor(VertexBuffer buffer, int color, int vertexIndex) {
        int index = buffer.getColorIndex(vertexIndex);
        int red = color >> 16 & 255;
        int green = color >> 8 & 255;
        int blue = color & 255;
        int alpha = color >> 24 & 255;
        buffer.putColorRGBA(index, red, green, blue, alpha);
    }

    private static RenderItem renderItem;

    public static int getAverageColor(ItemStack stack) {
        TextureAtlasSprite sprite = getSprite(stack);
        if (sprite == null) {
            return -1;
        }
        
        //TODO - Figure out a way to handle multiple render frames to have animated textures for things like avarita ingots
        int mappedAverageColor = ColorCache.getSpriteAverageColorForFrame(sprite.getIconName(), 0);
        
        if(mappedAverageColor == -1) {
            int pixelCount = 0;
            int[] data = sprite.getFrameTextureData(0)[0];
            int[] avgColor = new int[3];
            int k = 4;
            pixelCount = (sprite.getIconHeight()) * (sprite.getIconWidth());

            for (int j = k; j < sprite.getIconHeight()-k; j++) {
                for (int i = k; i < sprite.getIconWidth()-k; i++) {
                    int c = data[j * sprite.getIconWidth() + i];
                    int r = (c & 0xFF);
                    int g = ((c >> 8) & 0xFF);
                    int b = ((c >> 16) & 0xFF);
                    avgColor[0] += r;
                    avgColor[1] += g;
                    avgColor[2] += b;
                }
            }

            for (int i = 0; i < 3; i++) {
                avgColor[i] = (avgColor[i] / pixelCount) & 0xFFFFFF;
            }
            
            mappedAverageColor = (avgColor[0] | (avgColor[1] << 8) | (avgColor[2] << 16)) | 0xFF000000;
            ColorCache.put(sprite.getIconName(), 0, mappedAverageColor);
        }
        
 

        return mappedAverageColor;
    }

    @SideOnly(Side.CLIENT)
    public static TextureAtlasSprite getSprite(ItemStack stack) {
        if (renderItem == null) {
            renderItem = Minecraft.getMinecraft().getRenderItem();
        }

        return renderItem.getItemModelWithOverrides(stack, null, null).getParticleTexture();
    }
    
    /**
     * A wrapper helper class that maps sprite names to a list of computed average values for each frame, 
     * where sprite.getFrameCount() = listIndex for fast lookup.
     * @author michaelepps
     *
     */
    private static class ColorCache {
    	private static final Map<String, List<Integer>> spriteColorMap = new HashMap<String, List<Integer>>();
    
    	
    	/**
    	 * Attempts to get the stored average color for the sprite name on the specified frame.
    	 * @param spriteName - The name of the sprite from sprite.getIconName()
    	 * @param frame - The frame from sprite.getFrameCount()
    	 * @return
    	 */
    	public static int getSpriteAverageColorForFrame(String spriteName, int frame) {
    		int averageColor = -1;
    		List<Integer> averageColors = spriteColorMap.get(spriteName);
    		
    		if(averageColors != null && averageColors.size() > frame) {
    			averageColor = averageColors.get(frame);
    		}
    		
    		return averageColor;
    	}
    	
    	/**
    	 * Puts the mapping into the cache if one does not already exist, otherwise overwrite the mapping with the new one
    	 * @param spriteName - The name of the sprite from sprite.getIconName()
    	 * @param frame - The frame from sprite.getFrameCount()
    	 * @param averageColor - The average color of the sprite's current frame
    	 */
    	public static void put(String spriteName, int frame, int averageColor) {
    		List<Integer> averageColors = spriteColorMap.get(spriteName);
    		
    		if(averageColors != null) {
    			if(averageColors.size() > frame) {
    				averageColors.set(frame, averageColor);
    			} else {
    				/*
    				 * If the frame doesnt exist then just add it to the end of the list 
    				 * since each frame is always called in order
    				 * Could possibly do some fancy things with filling indicies in the range (size - 1, frame)
    				 * with -1 if frames are called out of order for some reason, needs testing
    				 */
    				averageColors.add(averageColor);
    			}
    		} else {
    			averageColors = new ArrayList<Integer>();
    			averageColors.add(averageColor);	
    		}
    		
    		spriteColorMap.put(spriteName, averageColors);
    	}
    }
}
