package com.tierzero.stacksonstacks.util;

import com.tierzero.stacksonstacks.pile.RelativeBlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.tuple.Pair;

public class RelativeBlockPosUtils {

	public static RelativeBlockPos getRelativeBlockPositionFromMOPHit(Vec3d hit) {
		return new RelativeBlockPos(hit.xCoord,hit.yCoord,hit.zCoord, EnumFacing.Axis.X);
	}

	public static Pair<Vec3d, Vec3d> getRayTraceVectors(EntityPlayer player) {
		float pitch = player.rotationPitch;
		float yaw = player.rotationYaw;
		Vec3d start = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		float f1 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
		float f2 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
		float f3 = -MathHelper.cos(-pitch * 0.017453292F);
		float f4 = MathHelper.sin(-pitch * 0.017453292F);
		float f5 = f2 * f3;
		float f6 = f1 * f3;
		double d3 = 5.0D;
		if (player instanceof EntityPlayerMP) {
			d3 = ((EntityPlayerMP) player).interactionManager.getBlockReachDistance();
		}
		Vec3d end = start.addVector(f5 * d3, f4 * d3, f6 * d3);
		return Pair.of(start, end);
	}

}
