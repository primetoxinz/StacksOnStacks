package com.primetoxinz.stacksonstacks.render;

import com.primetoxinz.stacksonstacks.PartIngot;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector3f;
import pl.asie.charset.lib.render.ModelFactory;
import pl.asie.charset.lib.render.SimpleBakedModel;
import pl.asie.charset.lib.utils.RenderUtils;

import static net.minecraft.util.EnumFacing.*;

public class RenderIngot extends ModelFactory<PartIngot> {
    private TextureAtlasSprite texture;
    public static final RenderIngot INSTANCE = new RenderIngot();
    public RenderIngot() {
        super(PartIngot.PROPERTY, new ResourceLocation("blocks/iron_block"));
        addDefaultBlockTransforms();
    }

    public SimpleBakedModel createRectPrism(float x, float y, float z, float w, float h, float l, int color, TextureAtlasSprite texture) {
        SimpleBakedModel model = new SimpleBakedModel(this);
        ModelRotation rot = ModelRotation.X0_Y0;
        model.addQuad(null, RenderUtils.BAKERY.makeBakedQuad(new Vector3f(x, y, z), new Vector3f(x + w, y, z + l), color, texture, DOWN, rot, false));
        model.addQuad(null, RenderUtils.BAKERY.makeBakedQuad(new Vector3f(x, y + h, z), new Vector3f(x + w, y + h, z + l), color, texture, UP, rot, false));
        model.addQuad(null, RenderUtils.BAKERY.makeBakedQuad(new Vector3f(x, y, z), new Vector3f(x, y + h, z + l), color, texture, WEST, rot, false));
        model.addQuad(null, RenderUtils.BAKERY.makeBakedQuad(new Vector3f(x + w, y, z), new Vector3f(x + w, y + h, z + l), color, texture, EAST, rot, false));
        model.addQuad(null, RenderUtils.BAKERY.makeBakedQuad(new Vector3f(x, y, z), new Vector3f(x + w, y + h, z), color, texture, NORTH, rot, false));
        model.addQuad(null, RenderUtils.BAKERY.makeBakedQuad(new Vector3f(x, y, z + l), new Vector3f(x+w, y + h, z + l), color, texture, SOUTH, rot, false));
        return model;
    }

    @Override
    public IBakedModel bake(PartIngot ingot, boolean isItem, BlockRenderLayer layer) {
        texture = RenderUtils.textureGetter.apply(new ResourceLocation("blocks/iron_block"));
        Vector3f loc = ingot.location.getRelativeLocation();
        int color = ingot.type.getColor();
        return createRectPrism(loc.x+0.2f, loc.y, loc.z+0.1f, 8-0.4f, 2, 4-0.2f, color, texture);
    }

    @Override
    public PartIngot fromItemStack(ItemStack stack) {  return null;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return texture;
    }
}
