package com.primetoxinz.stacksonstacks.ingot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.primetoxinz.stacksonstacks.core.ConfigHandler;
import com.primetoxinz.stacksonstacks.core.LogHandler;
import com.primetoxinz.stacksonstacks.lib.LibResources;
import com.primetoxinz.stacksonstacks.logic.OreDictUtil;

import lib.utils.RenderUtils;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.RegistryDelegate;
import net.minecraftforge.fml.relauncher.Side;

public class IngotRegistry {
	
	private static List<RegisteredIngot> ingotRegistry = new ArrayList<RegisteredIngot>();
	
	/**
	 * Registers the ingot and maps its render information
	 * @param ingotStack - The ItemStack to register the ingot from
	 */
	public static void registerIngot(@Nonnull ItemStack ingotStack) {
		Pair<Integer, ResourceLocation> colorSpritePair = Pair.of(getIngotColor(ingotStack), getRenderSpriteResourceLocation(ingotStack));
		Pair<RegistryDelegate<Item>, Integer> ingotInfoPair = getIngotInfoPair(ingotStack);
		registerIngot(new RegisteredIngot(ingotInfoPair, colorSpritePair));
		
	}
	
	public static void registerIngot(@Nonnull RegisteredIngot registeredIngot) {
		LogHandler.logInfo("Registered ingot: " + registeredIngot.getIngotInfo().getLeft().get().getRegistryName());
		ingotRegistry.add(registeredIngot);
	}
		
	/**
	 * @param ingotStack - The ItemStack of the ingot to check
	 * @return True - Registered, False - Not registered
	 */
	public static boolean isIngotRegistered(@Nonnull ItemStack ingotStack) {
		return getRegisteredIngot(ingotStack) != null;
	}
	
	public static RegisteredIngot getRegisteredIngot(@Nonnull ItemStack ingotStack) {
		Pair<RegistryDelegate<Item>, Integer> ingotStackInfo = getIngotInfoPair(ingotStack);
		
		for(RegisteredIngot registeredIngot : ingotRegistry) {
			if(registeredIngot.getIngotInfo().equals(ingotStackInfo)) {
				return registeredIngot;
			}
		}
				
		return null;
	}
	
	//Ingot-info functions
	public static Pair<RegistryDelegate<Item>, Integer> getIngotInfoPair(@Nonnull ItemStack itemStack) {
		if(itemStack != null && itemStack.getItem() != null) {
			return Pair.of(itemStack.getItem().delegate, itemStack.getItemDamage());
		}
		
		return Pair.of(Items.IRON_INGOT.delegate, 0);
	}
	
	public static ItemStack getItemStackFromIngotInfo(Pair<RegistryDelegate<Item>, Integer> ingotInfo) {
	  	Item item = ingotInfo.getLeft().get();
    	Integer meta = ingotInfo.getRight();
    	
    	return new ItemStack(item, 0, meta);
	}
	
	//Helper functions
	private static int getIngotColor(ItemStack ingotStack) {
		if(FMLCommonHandler.instance().getEffectiveSide() != Side.SERVER) {
			return RenderUtils.getAverageColor(ingotStack);
		}
		
		return 0;
	}
	
	private static ResourceLocation getRenderSpriteResourceLocation(ItemStack ingotStack) {
		ResourceLocation renderSpriteResourceLocation = LibResources.DEFAULT_SPRITE;
		
		if(ConfigHandler.useIngotBlockTexture) {
			ItemStack ingotBlock = OreDictUtil.getIngotBlock(ingotStack);		
			
			if(ingotBlock != null) {
				renderSpriteResourceLocation = ingotBlock.getItem().getRegistryName();
			}
		}
		
		return renderSpriteResourceLocation;
	}
}
