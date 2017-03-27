package com.tierzero.stacksonstacks.registration;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;


/**
 * An immutable class representing an item and its metadata in a easy to store and use form
 * Effectively the same as storing the ItemStack, but cannot be null for minecraft version 1.10
 * @author Madxmike
 *
 */
public class RegisteredItem {
	
	public static final RegisteredItem DEFAULT = new RegisteredItem(Item.getItemFromBlock(Blocks.STONE), 0);

	private static final String NBT_TAG_ITEM_STACK = "itemStack";
		
	private Item item;
	private int metadata;
	
	public RegisteredItem(@Nonnull Item item, int metadata) {
		this.item = item;
		this.metadata = metadata;
	}
	
	public Item getItem() {
		return item;
	}
	
	public int getMetadata() {
		return metadata;
	}
	
	/**
	 * @return The itemstack representation with a stacksize equal to 1
	 */
	public ItemStack asItemStack() {
		return new ItemStack(item, 1, metadata);
	}
	
	/**
	 * 
	 * @param itemStack - The ItemStack to check
	 * @return True if the RegisteredItem is the same item and metadata as the ItemStack, else false
	 */
	public boolean isItemStack(ItemStack itemStack) {
		return getItem().equals(itemStack.getItem()) && (getMetadata() == itemStack.getMetadata());
	}

	@Override
	public boolean equals(Object object) {
		if(object == this) return true;
		if(object instanceof RegisteredItem) {
			RegisteredItem registeredItem = (RegisteredItem) object;
			
			if(getItem().equals(registeredItem.getItem())) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return 31 * getItem().hashCode() + getMetadata();
	}




}