package com.tierzero.stacksonstacks.client;

import com.tierzero.stacksonstacks.containers.TilePileContainer;
import com.tierzero.stacksonstacks.pile.Pile;
import com.tierzero.stacksonstacks.pile.RelativeBlockPos;
import com.tierzero.stacksonstacks.registration.RegistrationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class TESRPile extends TileEntitySpecialRenderer<TilePileContainer> {

    @Override
    public void renderTileEntityAt(TilePileContainer te, double x, double y, double z, float partialTicks, int destroyStage) {
    	Pile pile = te.getPile();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
    	
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        
        RenderHelper.disableStandardItemLighting();
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }
        setLightmapDisabled(true);
        //TODO - Make this bind the texture on each individual ingot
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        GlStateManager.translate(x-te.getPos().getX(), y-te.getPos().getY(), z-te.getPos().getZ());
        
        for(int slot = 0; slot < pile.getSlots(); slot++) {
            ItemStack stack = pile.getStackInSlot(slot);
            if(!stack.isEmpty()) {
                IngotRender.renderIngotToBuffer(buffer, te.getWorld(), te.getPos(), RelativeBlockPos.fromSlot(slot),stack);
            }
        }
        tessellator.draw();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public static boolean renderWireframe(World world, EntityPlayer player, TileEntity tileEntity, Vec3d hitPos) {
    	ItemStack stack = player.getHeldItemMainhand();
    	if(!RegistrationHandler.isRegistered(stack))
    	    return false;
    	RelativeBlockPos relativeBlockPos = new RelativeBlockPos(hitPos.xCoord - Math.floor(hitPos.xCoord), hitPos.yCoord - Math.floor(hitPos.yCoord), hitPos.zCoord - Math.floor(hitPos.zCoord), EnumFacing.Axis.X);

    	GlStateManager.pushMatrix();

    	Tessellator tess = Tessellator.getInstance();
        tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        GlStateManager.translate(-player.getPosition().getX(), -player.getPosition().getY(), -player.getPosition().getZ());
        GlStateManager.translate(relativeBlockPos.getX(),0,relativeBlockPos.getZ());
        IngotRender.renderIngotToBuffer(tess.getBuffer(), tileEntity.getWorld(), tileEntity.getPos(), relativeBlockPos,stack);
        tess.draw();
    	GlStateManager.popMatrix();
    	return true;
    	
    }
    
}
