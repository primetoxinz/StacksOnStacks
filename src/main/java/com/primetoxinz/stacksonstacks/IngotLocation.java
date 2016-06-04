package com.primetoxinz.stacksonstacks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by tyler on 6/4/16.
 */
public class IngotLocation {
    //TODO fix
    private static final double MX=4, MY=8, MZ=2,bx=1/MX,by=1/MY,bz=1/MZ;
    private float x,y,z;

    public IngotLocation(float x, float y,float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public IngotLocation(double x, double y,double z) {
        this((float)x,(float)y,(float)z);
    }
    public AxisAlignedBB getBounds() {
        return new AxisAlignedBB(bx*x, (by*y),bz*z,(bx*x)-bx, (by*y)+by, (bz*z)-bz);
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
    public Vector3f getLocation() {
        return new Vector3f(x,y,z);
    }

}
