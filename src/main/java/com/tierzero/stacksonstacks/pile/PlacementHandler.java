package com.tierzero.stacksonstacks.pile;

import com.tierzero.stacksonstacks.registration.EnumRegisteredItemType;
import com.tierzero.stacksonstacks.registration.ItemRegistry;
import com.tierzero.stacksonstacks.registration.RegisteredItem;
import com.tierzero.stacksonstacks.registration.RegistrationHandler;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class PlacementHandler {
	
//	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void onPlayerInteractEvent(PlayerInteractEvent.RightClickBlock event) {
		if(event.getSide() == Side.SERVER) {
			if (event.getItemStack() != null) {
				ItemRegistry registry = RegistrationHandler.getItemRegistryForType(EnumRegisteredItemType.INGOT);
				RegisteredItem item = registry.getRegisteredItem(event.getItemStack());
				if(item != null) {
                    
                }
			}
		}
	}
	
}
