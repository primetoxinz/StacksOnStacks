package com.primetoxinz.stacksonstacks.render;

import com.google.common.base.Function;
import com.primetoxinz.stacksonstacks.PartIngot;
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
import org.lwjgl.util.vector.Vector3f;
import pl.asie.charset.lib.render.ModelFactory;
import pl.asie.charset.lib.render.SimpleBakedModel;

import static net.minecraft.util.EnumFacing.*;

public class RenderIngot extends ModelFactory<PartIngot> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("blocks/iron_block");
    private TextureAtlasSprite sprite;
    private VertexFormat format;

    public RenderIngot(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        super(PartIngot.PROPERTY, TEXTURE);
        sprite = bakedTextureGetter.apply(TEXTURE);
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

    private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4,EnumFacing side) {
        Vec3d normal = v1;//v1.subtract(v2).crossProduct(v3.subtract(v2));
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(sprite);
        builder.setQuadOrientation(UP);
        builder.setApplyDiffuseLighting(true);
        putVertex(builder, v1, v1.xCoord, v1.yCoord, v1.zCoord, 0, 0);
        putVertex(builder, v2, v2.xCoord, v2.yCoord, v2.zCoord, 0, 16);
        putVertex(builder, v3, v3.xCoord, v3.yCoord, v3.zCoord, 16,16);
        putVertex(builder, v4, v4.xCoord, v4.yCoord, v4.zCoord, 16, 0);
        return builder.build();
    }

    public SimpleBakedModel createRectPrism(float x, float y, float z, float w, float h, float l, int color) {
        SimpleBakedModel model = new SimpleBakedModel(this);
        Vec3d vec = new Vec3d(x, y, z);
        float o = 0.4f/16;
        EnumFacing face = DOWN;

        model.addQuad(null, createQuad(vec, vec.addVector(w, 0, 0), vec.addVector(w, 0,l), vec.addVector(0, 0, l),face));
        vec = vec.addVector(0,h,0);
        face = UP;
        model.addQuad(null, createQuad(vec.addVector(o,0,o), vec.addVector(o, 0, l-o), vec.addVector(w-o, 0,l-o), vec.addVector(w-o, 0, o),face));
        vec = new Vec3d(x,y,z);
        face = NORTH;
        model.addQuad(null, createQuad(vec, vec.addVector(o, h, o), vec.addVector(w-o, h,o), vec.addVector(w, 0, 0),face));
        vec = vec.addVector(0,0,l);
        face = SOUTH;
        model.addQuad(null, createQuad(vec, vec.addVector(w, 0, 0), vec.addVector(w-o, h,-o), vec.addVector(o, h, -o),face));
        vec = new Vec3d(x,y,z);
        face = EAST;
        model.addQuad(null, createQuad(vec, vec.addVector(0, 0, l), vec.addVector(o, h,l-o), vec.addVector(o, h, o),face));
        vec = vec.addVector(w,0,0);
        face = WEST;
        model.addQuad(null, createQuad(vec,vec.addVector(-o, h, o) , vec.addVector(-o, h,l-o), vec.addVector(0, 0, l),face));
        return model;
    }

    @Override
    public IBakedModel bake(PartIngot ingot, boolean isItem, BlockRenderLayer layer) {
        Vector3f loc = ingot.location.getRelativeLocation();
        float o = 0.1f/16;
        return createRectPrism(loc.x+o, loc.y, loc.z+o, .5f-2*o, 0.125f, .25f-2*o, ingot.type.getColor());
    }

    @Override
    public PartIngot fromItemStack(ItemStack stack) {  return null;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return sprite;
    }


}
