package com.tierzero.stacksonstacks.capability;

import com.tierzero.stacksonstacks.pile.IPileContainer;
import com.tierzero.stacksonstacks.pile.PileItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public interface IPile extends IItemHandler  {

	boolean addPileItem(World world, EntityPlayer player, RayTraceResult rayTraceResult, IPileContainer pileContainer, int slot, ItemStack stack);

	List<PileItem> getItems();

	int getStoredAmount();

	void deserializeNBT(NBTTagCompound tag);
	NBTTagCompound serializeNBT();
}
