package com.primetoxinz.stacksonstacks.ingot;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class IngotLocation {
	
	private static final String NBT_TAG_POS_X = "x";
	private static final String NBT_TAG_POS_Y = "y";
	private static final String NBT_TAG_POS_Z = "z";
	private static final String NBT_TAG_AXIS = "axis";

    protected float x; 
    protected float y;
    protected float z;
    protected EnumFacing.Axis axis;
    
    public IngotLocation(float x, float y, float z, EnumFacing.Axis axis) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.axis = axis;
    }

    public IngotLocation(double x, double y, double z, EnumFacing.Axis axis) {
        this((float)x,(float)y,(float)z,axis);
    }

    public AxisAlignedBB getBounds() {
        AxisAlignedBB box = new AxisAlignedBB(0, 0, 0, 8/16d,2/16d,4/16d).offset(x,y,z);
        return box;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setFloat(NBT_TAG_POS_X,x);
        tag.setFloat(NBT_TAG_POS_Y,y);
        tag.setFloat(NBT_TAG_POS_Z,z);
        tag.setInteger(NBT_TAG_AXIS ,axis.ordinal());
        return tag;
    }
    
    public static IngotLocation readFromNBT(NBTTagCompound tag) {
        float x = tag.getFloat(NBT_TAG_POS_X);
        float y = tag.getFloat(NBT_TAG_POS_Y);
        float z = tag.getFloat(NBT_TAG_POS_Z);
        int axis = tag.getInteger(NBT_TAG_AXIS);
        return new IngotLocation(x,y,z, EnumFacing.Axis.values()[axis]);
    }
    
    public void writeUpdatePacket(PacketBuffer buf) {
        buf.writeFloat(x);
        buf.writeFloat(y);
        buf.writeFloat(z);
        buf.writeInt(axis.ordinal());
    }
    
    public static IngotLocation readUpdatePacket(PacketBuffer buf) {
        float x = buf.readFloat();
        float y = buf.readFloat();
        float z = buf.readFloat();
        int axis = buf.readInt();
        return new IngotLocation(x,y,z,EnumFacing.Axis.values()[axis]);
    }

    public Vec3d getRelativeLocation() {
        return new Vec3d(x,y,z);
    }

    public boolean equals(IngotLocation loc) {
        return loc.x == x && loc.y == y && loc.z == z;
    }

    @Override
    public String toString() {
     return getRelativeLocation().toString();
    }
    
    public static IngotLocation fromHit(Vec3d hit, EnumFacing.Axis axis) {
        double x = relativePos(round(hit.xCoord, 2));
        double y = relativePos(round(hit.yCoord, 8));
        double z = relativePos(round(hit.zCoord, 4));
        
        if ( hit.xCoord < 0) {
            x=(x+.5)%1;
        }

        if ( hit.zCoord < 0) {
            z=Math.abs(.75-z);
        }
        
        return new IngotLocation(x, y, z, axis);
    }
    
    private static double relativePos(double x) {
        double pos = ((x > 0) ? Math.floor(x): Math.ceil(x));
        return Math.abs(x - pos);
    }
    
    
    private static double round(double num, double r) {
        return ((int) (num * (int) (r))) / r;
    }
}


