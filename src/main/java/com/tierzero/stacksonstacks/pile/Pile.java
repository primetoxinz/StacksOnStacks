package com.tierzero.stacksonstacks.pile;

import com.tierzero.stacksonstacks.capability.IPile;
import com.tierzero.stacksonstacks.registration.EnumRegisteredItemType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class Pile implements IPile, INBTSerializable<NBTTagCompound> {
	
	private static final String NBT_TAG_TYPE = "type";
	private static final String NBT_TAG_STORED_ITEMS = "storedItems";
	private static final String NBT_TAG_STORED_ITEMS_COUNT = "storedItemsCount";

	private EnumRegisteredItemType type;
	private List<PileItem> storedItems;
	
	public Pile(EnumRegisteredItemType type) {
		this.type = type;
		this.storedItems = new ArrayList<PileItem>();
	}
	
	public boolean addPileItem(World world, EntityPlayer player, RayTraceResult rayTraceResult, IPileContainer pileContainer, PileItem pileItem) {
		if((getMaxStoredAmount() - getStoredAmount()) <= 0) {
			IPileContainer nextPileContainer = pileContainer.getNextPileContainer();
			if(nextPileContainer != null) {
				if(player.isSneaking()) {
					return nextPileContainer.onPlayerShiftRightClick(world, player, rayTraceResult);
				} else {
					return nextPileContainer.onPlayerRightClick(world, player, rayTraceResult);
				}
			}
		} else {
			//RelativeBlockPos rayTracedPosition = RelativeBlockPos.getFromRayTraceResult(rayTraceResult);
			
		}
		
		this.storedItems.add(pileItem);
		
		return true;
	}
	
	public PileItem getPileItemAtRelativeBlockPos(RelativeBlockPos relativeBlockPos) {
		for(PileItem pileItem : storedItems) {
			RelativeBlockPos pileItemRelativeBlockPos = pileItem.getRelativeBlockPos();
			
			if(pileItemRelativeBlockPos.equals(relativeBlockPos)) {
				return pileItem;
			}
		}
		
		
		return null;
	}
		
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag(NBT_TAG_TYPE, type.serializeNBT());
		
		NBTTagCompound storedItemsTag = new NBTTagCompound();
		storedItemsTag.setInteger(NBT_TAG_STORED_ITEMS_COUNT, storedItems.size());
		for(int iter = 0; iter < storedItems.size(); iter++) {
			storedItemsTag.setTag(String.valueOf(iter), storedItems.get(iter).serializeNBT());
		}
				
		tag.setTag(NBT_TAG_STORED_ITEMS, storedItemsTag);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tag) {	
		this.type = EnumRegisteredItemType.getFromNBT(tag.getCompoundTag(NBT_TAG_TYPE));
		
		NBTTagCompound storedItemsTag = tag.getCompoundTag(NBT_TAG_STORED_ITEMS);
		for(int iter = 0; iter < tag.getInteger(NBT_TAG_STORED_ITEMS_COUNT); iter++) {
			storedItems.add(PileItem.getFromDeserializeNBT(storedItemsTag.getCompoundTag(String.valueOf(iter))));
		}
	}

	@Override
	public int getStoredAmount() {
		return storedItems.size();
	}

	@Override
	public int getMaxStoredAmount() {
		return type.getMaxStackSize();
	}
}
