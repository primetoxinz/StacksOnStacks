package com.primetoxinz.stacksonstacks.render;

import com.primetoxinz.stacksonstacks.PartIngot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
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
        super(PartIngot.PROPERTY, TextureMap.LOCATION_MISSING_TEXTURE);
        addDefaultBlockTransforms();

    }

    @Override
    public IBakedModel bake(PartIngot ingot, boolean isItem, BlockRenderLayer layer) {
        texture = Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(new ResourceLocation("blocks/iron_block"));
        TextureAtlasSprite ingotTexture;
        if(ingot.type == null || ingot.type.stack == null)
            ingotTexture = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(Items.IRON_INGOT);
        else
            ingotTexture = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(ingot.type.stack.getItem());
        SimpleBakedModel model = new SimpleBakedModel(this);
        EnumFacing facing;
        Vector3f tf = ingot.location.getLocation();

        int renderColorIngot = RenderUtils.getAverageColor(ingotTexture, RenderUtils.AveragingMode.FULL);
        int renderColor = renderColorIngot == -1 ? renderColorIngot :
                (0xFF000000 | (renderColorIngot & 0x00FF00) | ((renderColorIngot & 0xFF0000) >> 16) | ((renderColorIngot & 0x0000FF) << 16));
        float MX = 4*tf.x, MY = 2*(tf.y+1), MZ = 8*tf.z;
        float mx = 4*(tf.x-1), my = 2*(tf.y), mz = 8*(tf.z-1);
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
            model.addQuad(facing, RenderUtils.bakeFace(from[i], to[i], facing, texture, renderColor));

        }

        return model;
    }

    @Override
    public PartIngot fromItemStack(ItemStack stack) {  return null;
    }
}
