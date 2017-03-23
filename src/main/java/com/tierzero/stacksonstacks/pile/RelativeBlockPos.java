package com.tierzero.stacksonstacks.pile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class RelativeBlockPos extends BlockPos {

	private static final String NBT_TAG_POS_X = "posX";
	private static final String NBT_TAG_POS_Y = "posY";
	private static final String NBT_TAG_POS_Z = "posZ";
	private static final String NBT_TAG_AXIS = "axis";

	private EnumFacing.Axis axis;

	public RelativeBlockPos(double x, double y, double z, EnumFacing.Axis axis) {
		super(x, y, z);
		this.axis = axis;
	}



	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setFloat(NBT_TAG_POS_X, getX());
		return null;
	}

	public static RelativeBlockPos getFromDeserializeNBT(NBTTagCompound tag) { 
		EnumFacing.Axis axis = EnumFacing.Axis.X;
		if(tag.hasKey(NBT_TAG_AXIS)) {
			axis = EnumFacing.Axis.values()[tag.getInteger(NBT_TAG_AXIS)];
		}
		
		return new RelativeBlockPos(tag.getDouble(NBT_TAG_POS_X), tag.getDouble(NBT_TAG_POS_Y), tag.getDouble(NBT_TAG_POS_Z), axis);
	}
}
