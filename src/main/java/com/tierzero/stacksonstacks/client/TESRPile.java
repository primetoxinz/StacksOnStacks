package com.tierzero.stacksonstacks.client;

import org.lwjgl.opengl.GL11;

import com.tierzero.stacksonstacks.containers.TileContainer;
import com.tierzero.stacksonstacks.pile.Pile;
import com.tierzero.stacksonstacks.pile.RelativeBlockPos;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TESRPile extends TileEntitySpecialRenderer<TileContainer> {

    @Override
    public void renderTileEntityAt(TileContainer te, double x, double y, double z, float partialTicks, int destroyStage) {
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

        //TODO - Make this bind the texture on each individual ingot
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        GlStateManager.translate(x, y, z);
        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
        
        for(int slot = 0; slot < pile.getSlots(); slot++) {
            ItemStack stack = pile.getStackInSlot(slot);
            if(!stack.isEmpty()) {
                IngotRender.renderIngotToBuffer(buffer, te.getWorld(), te.getPos(), RelativeBlockPos.fromSlot(slot));
            }
        }
        
        tessellator.draw();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public static boolean renderWireframe(World world, EntityPlayer player, TileEntity tileEntity, Vec3d hitPos) {
    	
    	Vec3d relativeHitPos = new Vec3d(hitPos.xCoord - Math.floor(hitPos.xCoord), hitPos.yCoord - Math.floor(hitPos.yCoord), hitPos.zCoord - Math.floor(hitPos.zCoord));
    	
    	RelativeBlockPos relativePos = new RelativeBlockPos(relativeHitPos.xCoord, relativeHitPos.yCoord, relativeHitPos.zCoord, EnumFacing.Axis.X);
        
    	GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();

    	Tessellator tess = Tessellator.getInstance();
    	BlockPos playerPosition = player.getPosition();
    	Vec3d translation = new Vec3d(playerPosition.getX() - hitPos.xCoord, playerPosition.getY() - hitPos.yCoord, playerPosition.getZ() - hitPos.zCoord);
        tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        GlStateManager.translate(-player.getPosition().getX(), -player.getPosition().getY(), -player.getPosition().getZ());

        GlStateManager.translate(-translation.xCoord, -translation.yCoord - 1, -translation.zCoord);


        IngotRender.renderIngotToBuffer(tess.getBuffer(), tileEntity.getWorld(), tileEntity.getPos(), relativePos);
        tess.draw();
    	RenderHelper.enableStandardItemLighting();

    	GlStateManager.popMatrix();
    	return true;
    	
    }
    
}
