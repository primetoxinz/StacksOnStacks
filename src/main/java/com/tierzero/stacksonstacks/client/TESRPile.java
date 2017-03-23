package com.tierzero.stacksonstacks.client;

import com.tierzero.stacksonstacks.containers.TileContainer;
import com.tierzero.stacksonstacks.lib.LibCore;
import com.tierzero.stacksonstacks.pile.Pile;
import com.tierzero.stacksonstacks.pile.PileItem;
import com.tierzero.stacksonstacks.pile.RelativeBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import org.lwjgl.opengl.GL11;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/23/17
 */
public class TESRPile extends TileEntitySpecialRenderer<TileContainer> {


    private IModel model;
    private IBakedModel bakedModel;

    private IBakedModel getBakedModel() {
        // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
        // for rendering
        if (bakedModel == null) {
            try {
                model = ModelLoaderRegistry.getModel(new ResourceLocation(LibCore.MOD_ID, "block/ingot.obj"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        }
        return bakedModel;
    }

    @Override
    public void renderTileEntityAt(TileContainer te, double x, double y, double z, float partialTicks, int destroyStage) {

        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();
        Tessellator tessellator = Tessellator.getInstance();
        Pile pile = te.getPile();
        VertexBuffer buffer = tessellator.getBuffer();
        World world = te.getWorld();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, getBakedModel(), world.getBlockState(te.getPos()), te.getPos(), buffer, false);
        tessellator.draw();
//
//        for (PileItem item : pile.getItems()) {
////            System.out.println(item);
//            renderIngot(te, item, x, y, z, partialTicks, Tessellator.getInstance().getBuffer());
//        }

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }


    public void renderIngot(TileContainer te, PileItem item, double x, double y, double z, float partialTicks, VertexBuffer buffer) {
        GlStateManager.pushMatrix();

        RelativeBlockPos rPos = item.getRelativeBlockPos();
        BlockPos pos = rPos.add(x, y, z);
        World world = te.getWorld();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, getBakedModel(), world.getBlockState(te.getPos()), te.getPos(), buffer, false);
//        tessellator.draw();

        GlStateManager.popMatrix();

    }
}
