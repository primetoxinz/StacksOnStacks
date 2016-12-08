package com.primetoxinz.stacksonstacks.ingot;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.primetoxinz.stacksonstacks.core.LogHandler;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.RegistryDelegate;

public class RegisteredIngot {
	
	private static final String NBT_TAG_COLOR = "color";

    private Pair<RegistryDelegate<Item>, Integer> ingotInfo;
    private Pair<Integer, ResourceLocation> renderInfo;

    public RegisteredIngot(@Nonnull Pair<RegistryDelegate<Item>, Integer> ingotInfoPair, @Nonnull Pair<Integer, ResourceLocation> colorSpritePair) {
       	this.ingotInfo = ingotInfoPair;
    	this.renderInfo = colorSpritePair;
    }
    
    //NBT Functions
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    	ItemStack stack = IngotRegistry.getItemStackFromIngotInfo(ingotInfo);
    	
        if (stack != null) {
            stack.writeToNBT(tag);
        }
        
        return tag;
    }

    public static RegisteredIngot readFromNBT(NBTTagCompound tag) {
        ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
        
        if(stack != null) {
        	LogHandler.logInfo("Reading from NBT");
        	RegisteredIngot registeredIngot = IngotRegistry.getRegisteredIngot(stack);
        
        	if(registeredIngot == null) {
            	IngotRegistry.registerIngot(stack);
        	}
        	
        	return registeredIngot;        	
        }
                	
        return IngotRegistry.getRegisteredIngot(new ItemStack(Items.IRON_INGOT, 0, 0));
    }

    //Network functions
    public void writeUpdatePacket(PacketBuffer buf) {
        buf.writeItemStackToBuffer(asItemStack());
    }

    public static RegisteredIngot readUpdatePacket(PacketBuffer buf) {
        ItemStack stack = null;
        RegisteredIngot type = null;
        try {
            stack = buf.readItemStackFromBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return type;
    }
    
    //Getters
    public Pair<RegistryDelegate<Item>, Integer> getIngotInfo() {
    	return this.ingotInfo;
    }
    
    public Pair<Integer, ResourceLocation> getRenderInfo() {
    	return this.renderInfo;
    }
    
    public ItemStack asItemStack() {
    	return IngotRegistry.getItemStackFromIngotInfo(ingotInfo);
    }
    
    @Override
    public boolean equals(Object object) {
    	if(object instanceof RegisteredIngot) {
        	RegisteredIngot other = (RegisteredIngot) object;
        	
        	ItemStack otherItemStack = other.asItemStack();
        	ItemStack registeredItemStack = asItemStack();
        	
        	return registeredItemStack.equals(otherItemStack);
    	}
    	
    	return false;
    }
}
