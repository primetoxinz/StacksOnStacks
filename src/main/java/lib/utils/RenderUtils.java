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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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

	public static TextureAtlasSprite getSprite(ItemStack stack) {
		if (renderItem == null) {
			renderItem = Minecraft.getMinecraft().getRenderItem();
		}

		return renderItem.getItemModelWithOverrides(stack, null, null).getParticleTexture();
	}

}
