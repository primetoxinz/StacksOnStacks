package com.tierzero.stacksonstacks.client;

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
                true);
        buffer.setTranslation(0, 0, 0);

        for (int i = 0; i < 24; i++)
            putColor(buffer, getAverageColor(stack), i + 1);
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
        if (sprite == null)
            return -1;

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

        return (avgColor[0] | (avgColor[1] << 8) | (avgColor[2] << 16)) | 0xFF000000;
    }

    @SideOnly(Side.CLIENT)
    public static TextureAtlasSprite getSprite(ItemStack stack) {
        if (renderItem == null) {
            renderItem = Minecraft.getMinecraft().getRenderItem();
        }

        return renderItem.getItemModelWithOverrides(stack, null, null).getParticleTexture();
    }

}
