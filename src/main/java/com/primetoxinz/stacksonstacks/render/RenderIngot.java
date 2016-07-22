package com.primetoxinz.stacksonstacks.render;

import com.primetoxinz.stacksonstacks.Config;
import com.primetoxinz.stacksonstacks.ingot.PartIngot;
import lib.render.ModelFactory;
import lib.render.SimpleBakedModel;
import lib.utils.RenderUtils;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

import static net.minecraft.util.EnumFacing.*;

public class RenderIngot extends ModelFactory<PartIngot> {
    public static ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("stacksonstacks", "blocks/ingot" + Config.textureVersion);
    private TextureAtlasSprite sprite;
    private VertexFormat format;

    public RenderIngot(VertexFormat format) {
        super(PartIngot.PROPERTY);
        this.format = format;
    }

    private void putVertex(CustomQuad.Builder builder, Vec3d normal, double x, double y, double z, float u, float v) {

        Color c = new Color(builder.getTint());
        for (int e = 0; e < format.getElementCount(); e++) {
            switch (format.getElement(e).getUsage()) {
                case POSITION:
                    builder.put(e, (float) x, (float) y, (float) z, 1.0f);
                    break;
                case COLOR:
                    if (builder.getTint() == 0)
                        builder.put(e, 1, 1, 1, 1.0f);
                    else
                        builder.put(e, c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1.0f);
                    break;
                case UV:
                    if (format.getElement(e).getIndex() == 0) {
                        u = sprite.getInterpolatedU(u);
                        v = sprite.getInterpolatedV(v);
                        builder.put(e, u, v, 0f, 1f);
                        break;
                    }
                case NORMAL:
                    builder.put(e, (float) normal.xCoord, (float) normal.yCoord, (float) normal.zCoord, 0f);
                    break;
                default:
                    builder.put(e);
                    break;
            }
        }
    }

    private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4,EnumFacing side,int color) {
        Vec3d normal = new Vec3d(0,0,1);
        CustomQuad.Builder builder = new CustomQuad.Builder(format);
        builder.setTexture(sprite);
        builder.setQuadTint(color);
        builder.setApplyDiffuseLighting(false);
        if(side == WEST || side == NORTH || side == UP) {
            putVertex(builder, normal, v1.xCoord, v1.yCoord, v1.zCoord, 0, 0);
            putVertex(builder, normal, v2.xCoord, v2.yCoord, v2.zCoord, 0, 16);
            putVertex(builder, normal, v3.xCoord, v3.yCoord, v3.zCoord, 16, 16);
            putVertex(builder, normal, v4.xCoord, v4.yCoord, v4.zCoord, 16, 0);
        } else {
            putVertex(builder, normal, v1.xCoord, v1.yCoord, v1.zCoord, 0, 0);
            putVertex(builder, normal, v2.xCoord, v2.yCoord, v2.zCoord, 16, 0);
            putVertex(builder, normal, v3.xCoord, v3.yCoord, v3.zCoord, 16, 16);
            putVertex(builder, normal, v4.xCoord, v4.yCoord, v4.zCoord, 0, 16);
        }
        return builder.build();
    }

    public SimpleBakedModel createRectPrism(double x, double y, double z, float w, float h, float l, int color) {
        SimpleBakedModel model = new SimpleBakedModel(this);
        Vec3d vec = new Vec3d(x, y, z);
        float o = 0.4f/16;
        model.addQuad(null, createQuad(vec, vec.addVector(w, 0, 0), vec.addVector(w, 0,l), vec.addVector(0, 0, l),DOWN,color));
        vec = vec.addVector(0,h,0);
        model.addQuad(null, createQuad(vec.addVector(o,0,o), vec.addVector(o, 0, l-o), vec.addVector(w-o, 0,l-o), vec.addVector(w-o, 0, o),UP,color));
        vec = new Vec3d(x,y,z);
        model.addQuad(null, createQuad(vec, vec.addVector(o, h, o), vec.addVector(w-o, h,o), vec.addVector(w, 0, 0),NORTH,color));
        vec = vec.addVector(0,0,l);
        model.addQuad(null, createQuad(vec, vec.addVector(w, 0, 0), vec.addVector(w-o, h,-o), vec.addVector(o, h, -o),SOUTH,color));
        vec = new Vec3d(x,y,z);
        model.addQuad(null, createQuad(vec, vec.addVector(0, 0, l), vec.addVector(o, h,l-o), vec.addVector(o, h, o),EAST,color));
        vec = vec.addVector(w,0,0);
        model.addQuad(null, createQuad(vec,vec.addVector(-o, h, o) , vec.addVector(-o, h,l-o), vec.addVector(0, 0, l),WEST,color));
        return model;
    }

    @Override
    public IBakedModel bake(PartIngot ingot, boolean isItem, BlockRenderLayer layer) {
        ingot.type.initRender();
        TextureAtlasSprite tex = ingot.type.getSprite();
        sprite = tex != null ? tex:RenderUtils.textureGetter.apply(DEFAULT_TEXTURE);
        Vec3d loc = ingot.location.getRelativeLocation();
        float o = 0.1f/16;
        return createRectPrism(loc.xCoord+o, loc.yCoord, loc.zCoord+o, .5f-2*o, 0.125f, .25f-2*o, ingot.type.getColor());
    }

    @Override
    public PartIngot fromItemStack(ItemStack stack) {  return null; }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return sprite;
    }


}
