/*
 * Copyright (c) 2015-2016 Adrian Siekierka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lib.utils;

import com.google.common.base.Function;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class RenderUtils {

	public static final Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
		public TextureAtlasSprite apply(ResourceLocation location) {
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
		}
	};

	public enum AveragingMode {
		FULL,
		H_EDGES_ONLY,
		V_EDGES_ONLY,
	};

	private static RenderItem renderItem;

    private RenderUtils() {
    }


    public static int getAverageColor(ItemStack stack) {
        TextureAtlasSprite sprite = getSprite(stack);
        if (sprite == null)
            return -1;

		int pixelCount = 0;
		int[] data = sprite.getFrameTextureData(0)[0];
		int[] avgColor = new int[3];
        int k = 3;
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
            avgColor[i] = (avgColor[i] / pixelCount) & 0xFF;
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

    public static void drawSelectionBox(EntityPlayer player, RayTraceResult movingObjectPositionIn, int execute, float partialTicks) {
        World world = player.getEntityWorld();
        if (execute == 0 && movingObjectPositionIn.typeOfHit == RayTraceResult.Type.BLOCK) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
            GlStateManager.glLineWidth(2.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            BlockPos pos = movingObjectPositionIn.getBlockPos();
            IBlockState state = world.getBlockState(pos);
            EnumFacing.Axis facing = player.getHorizontalFacing().getAxis();
            if (state.getMaterial() != Material.AIR && world.getWorldBorder().contains(pos)) {
                double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
                double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
                double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
                AxisAlignedBB block = state.getSelectedBoundingBox(world, pos);
                double x = pos.getX(), y = (block.maxY), z = pos.getZ();
                AxisAlignedBB box;
                for (int i = 1; i <= 4; i++) {
                    for(int j=1;j<=2;j++) {
                        box = new AxisAlignedBB(x + (.5 * (j - 1)), y, z + (i / 4d) - .25, x + j * .5, y, z + (i / 4d)).expandXyz(.002d).offset(-d0, -d1, -d2);
                        drawLine(box);
                    }
                }
            }
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }

    private static void drawLine(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
    }
}
