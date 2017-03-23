package com.tierzero.stacksonstacks.util;

import com.tierzero.stacksonstacks.pile.RelativeBlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class RelativeBlockPosUtils {

	public static RelativeBlockPos getRelativeBlockPositionFromMOPHit(Vec3d hit) {
		return new RelativeBlockPos(hit.xCoord,hit.yCoord,hit.zCoord, EnumFacing.Axis.X);
	}
	
}
