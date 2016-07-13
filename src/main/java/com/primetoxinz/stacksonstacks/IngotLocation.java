package com.primetoxinz.stacksonstacks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by tyler on 6/4/16.
 */
public class IngotLocation {
    protected float x, y, z;

    public IngotLocation(float x, float y,float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public IngotLocation(double x, double y,double z) {
        this((float)x,(float)y,(float)z);
    }

    public AxisAlignedBB getBounds() {
        AxisAlignedBB box = new AxisAlignedBB(0, 0, 0, 8/16d,2/16d,4/16d).offset(x,y,z);
        return box;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setFloat("x",x);
        tag.setFloat("y",y);
        tag.setFloat("z",z);
        return tag;
    }
    public static IngotLocation readFromNBT(NBTTagCompound tag) {
        float x = tag.getFloat("x");
        float y = tag.getFloat("y");
        float z = tag.getFloat("z");
        return new IngotLocation(x,y,z);
    }
    public void writeUpdatePacket(PacketBuffer buf) {
        buf.writeFloat(x);
        buf.writeFloat(y);
        buf.writeFloat(z);
    }
    public static IngotLocation readUpdatePacket(PacketBuffer buf) {
        float x = buf.readFloat();
        float y = buf.readFloat();
        float z = buf.readFloat();
        return new IngotLocation(x,y,z);
    }

    public Vector3f getRelativeLocation() {
        return (Vector3f) new Vector3f(x,y,z).scale(16);
    }

    public boolean isValidLocation() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("X:%s,Y:%s,Z:%s", x, y, z);
    }
}


