package com.tierzero.stacksonstacks.pile;

import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlacementHandler {
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public static void onPlayerInteractEvent(PlayerInteractEvent event) {
		RayTraceResult playerRayTrace = event.getEntityPlayer().rayTrace(3, 1);
		
		System.out.println(playerRayTrace.getBlockPos());
	}
	
}
