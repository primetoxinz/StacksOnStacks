package com.tierzero.stacksonstacks.client;

import com.tierzero.stacksonstacks.containers.TileContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WireframeRenderHandler {
	
	@SubscribeEvent
	public void drawBlockHighlightEvent(DrawBlockHighlightEvent event) {
		World world = event.getPlayer().getEntityWorld();
		BlockPos hitBlockPosition = event.getTarget().getBlockPos();
		
		if(event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK) {
			TileEntity tileAtHitPosition = world.getTileEntity(hitBlockPosition);
			if(tileAtHitPosition instanceof TileContainer) {
				if(TESRPile.renderWireframe(world, event.getPlayer(), tileAtHitPosition, event.getTarget().hitVec)) {
//					event.setCanceled(true);
				}
			}
		}
	}
	

}
