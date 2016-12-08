package com.primetoxinz.stacksonstacks.render;

import static net.minecraft.util.EnumFacing.DOWN;
import static net.minecraft.util.EnumFacing.EAST;
import static net.minecraft.util.EnumFacing.NORTH;
import static net.minecraft.util.EnumFacing.SOUTH;
import static net.minecraft.util.EnumFacing.UP;
import static net.minecraft.util.EnumFacing.WEST;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Mouse;

import com.primetoxinz.stacksonstacks.ingot.MultiPartIngot;
import com.primetoxinz.stacksonstacks.ingot.RegisteredIngot;

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
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

public class RenderIngot extends ModelFactory<MultiPartIngot> {
	public static ResourceLocation defaultSpriteResourceLocation = new ResourceLocation("stackonstacks", "blocks/default_ingot");

    private TextureAtlasSprite sprite;
    private VertexFormat format;

    public RenderIngot(VertexFormat format) {
        super(MultiPartIngot.PROPERTY);
        this.format = format;
    }

    private void putVertex(UnpackedBakedQuad.Builder builder, Vec3d normal, double x, double y, double z, float u, float v) {
        for (int e = 0; e < format.getElementCount(); e++) {
            switch (format.getElement(e).getUsage()) {
                case POSITION:
                    builder.put(e, (float) x, (float) y, (float) z, 1.0f);
                    break;
                case COLOR:
                    builder.put(e, 1.0f, 1.0f, 1.0f, 1.0f);
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
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(sprite);
        builder.setApplyDiffuseLighting(false);
        builder.setQuadTint(color);
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
    public IBakedModel bake(MultiPartIngot multipartIngot, boolean isItem, BlockRenderLayer layer) {
    	RegisteredIngot ingot = multipartIngot.getRegisteredIngot();
    	Pair<Integer, ResourceLocation> renderInfo = ingot.getRenderInfo();
    	sprite = RenderUtils.textureGetter.apply(renderInfo.getRight());
    	
        Vec3d loc = multipartIngot.getLocation().getRelativeLocation();
        float offset = 0.1f/16;
        return createRectPrism(loc.xCoord + offset, loc.yCoord, loc.zCoord + offset, .5f-2 * offset, 0.125f, .25f-2 * offset, renderInfo.getLeft());
    }

    @Override
    public MultiPartIngot fromItemStack(ItemStack stack) {  return null; }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return sprite;
    }
}
