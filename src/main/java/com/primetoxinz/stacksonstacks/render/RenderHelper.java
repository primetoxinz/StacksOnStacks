package com.primetoxinz.stacksonstacks.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

/**
 * Created by tyler on 7/11/16.
 */
public class RenderHelper {

    public static void drawLineWithColor(
            final Vec3d a,
            final Vec3d b,
            final BlockPos blockPos,
            final EntityPlayer player,
            final float partialTicks,
            final boolean NormalBoundingBox,
            final int red,
            final int green,
            final int blue,
            final int alpha,
            final int seeThruAlpha) {
        if (a != null && b != null) {
            final double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
            final double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
            final double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            GL11.glLineWidth(2.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GlStateManager.shadeModel(GL11.GL_FLAT);

            final Vec3d a2 = a.addVector(-x + blockPos.getX(), -y + blockPos.getY(), -z + blockPos.getZ());
            final Vec3d b2 = b.addVector(-x + blockPos.getX(), -y + blockPos.getY(), -z + blockPos.getZ());
            if (!NormalBoundingBox) {
                RenderHelper.renderLine(a2, b2, red, green, blue, alpha);
            }

            GlStateManager.disableDepth();
            RenderHelper.renderLine(a2, b2, red, green, blue, seeThruAlpha);

            GlStateManager.shadeModel(Minecraft.isAmbientOcclusionEnabled() ? GL11.GL_SMOOTH : GL11.GL_FLAT);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }

    public static void renderLine(
            final Vec3d a,
            final Vec3d b,
            final int red,
            final int green,
            final int blue,
            final int alpha) {
        final Tessellator tess = Tessellator.getInstance();
        final VertexBuffer buffer = tess.getBuffer();
        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(a.xCoord, a.yCoord, a.zCoord).color(red, green, blue, alpha).endVertex();
        buffer.pos(b.xCoord, b.yCoord, b.zCoord).color(red, green, blue, alpha).endVertex();
        tess.draw();
    }

}
