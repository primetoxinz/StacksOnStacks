package com.tierzero.stacksonstacks.capability;

import com.tierzero.stacksonstacks.pile.IPileContainer;
import com.tierzero.stacksonstacks.pile.PileItem;
import com.tierzero.stacksonstacks.pile.RelativeBlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public interface IPile {

	PileItem getPileItemAtRelativeBlockPos(RelativeBlockPos relativeBlockPos);
	boolean addPileItem(World world, EntityPlayer player, RayTraceResult rayTraceResult, IPileContainer pileContainer, PileItem pileItem);

	List<PileItem> getItems();

	int getStoredAmount();
	int getMaxStoredAmount();

	void deserializeNBT(NBTTagCompound tag);
	NBTTagCompound serializeNBT();
}
