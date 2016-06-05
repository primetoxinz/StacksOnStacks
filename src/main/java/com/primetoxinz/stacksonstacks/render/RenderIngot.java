package com.primetoxinz.stacksonstacks.render;

import com.primetoxinz.stacksonstacks.PartIngot;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector3f;
import pl.asie.charset.lib.render.ModelFactory;
import pl.asie.charset.lib.render.SimpleBakedModel;
import pl.asie.charset.lib.utils.RenderUtils;

public class RenderIngot extends ModelFactory<PartIngot> {
    private TextureAtlasSprite texture;
    public static final RenderIngot INSTANCE = new RenderIngot();
    public RenderIngot() {
        super(PartIngot.PROPERTY, new ResourceLocation("blocks/iron_block"));
        addDefaultBlockTransforms();

    }

    @Override
    public IBakedModel bake(PartIngot ingot, boolean isItem, BlockRenderLayer layer) {
        texture = RenderUtils.textureGetter.apply(new ResourceLocation("blocks/iron_block"));

        SimpleBakedModel model = new SimpleBakedModel(this);
        EnumFacing facing;
        Vector3f tf = ingot.location.getLocation();
        int renderColor = ingot.type.getColor();
        float hp = 1/4f;
        float MX = (4*tf.x)-hp, MY = 2*(tf.y+1), MZ = (8*tf.z)-hp;
        float mx = (4*(tf.x-1))+hp, my = 2*(tf.y), mz = (8*(tf.z-1))+hp;

        Vector3f[] from = new Vector3f[]{
                new Vector3f(mx,my,mz),
                new Vector3f(mx,MY,mz),
                new Vector3f(mx,my,mz),
                new Vector3f(mx,my,MZ),
                new Vector3f(mx,my,mz),
                new Vector3f(MX,my,mz),
        };
        Vector3f[] to = new Vector3f[]{
                new Vector3f(MX,my,MZ),
                new Vector3f(MX,MY,MZ),
                new Vector3f(MX,MY,mz),
                new Vector3f(MX,MY,MZ),
                new Vector3f(mx,MY,MZ),
                new Vector3f(MX,MY,MZ),
        };

        for(int i = 0; i< 6;i++) {
            facing = EnumFacing.VALUES[i];
            model.addQuad(facing, RenderUtils.BAKERY.makeBakedQuad(from[i],to[i],renderColor,texture,facing, ModelRotation.X0_Y0, true));
            model.addQuad(facing, RenderUtils.BAKERY.makeBakedQuad(from[i],to[i],renderColor,texture,facing.getOpposite(), ModelRotation.X0_Y0, true));
        }


        return model;
    }

    @Override
    public PartIngot fromItemStack(ItemStack stack) {  return null;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return texture;
    }
}
