package com.primetoxinz.stacksonstacks.capability;

import com.primetoxinz.stacksonstacks.lib.LibResources;

import mcmultipart.block.TileMultipartContainer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
    public void attachCapability(AttachCapabilitiesEvent.TileEntity event) {
        if (event.getTileEntity() instanceof TileMultipartContainer) {
            TileMultipartContainer container = (TileMultipartContainer) event.getTileEntity();
            if (!container.hasCapability(IngotCountProvider.CAPABILITY_INGOT_COUNT, null)) {
            	event.addCapability(LibResources.CAPABILITY_INGOT_COUNT, new IngotCountProvider());
            }
        }
    }
	
}
