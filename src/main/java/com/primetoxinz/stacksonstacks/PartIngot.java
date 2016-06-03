package com.primetoxinz.stacksonstacks;

import mcmultipart.multipart.Multipart;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyler on 5/28/16.
 */

public class PartIngot extends Multipart {

    private Ingot ingot;

    private double x,y,z;
    private static final float bx=1/4f,by=1/8f,bz=1/2f;
    public PartIngot() {}

    public PartIngot(Ingot ingot, Vec3d vec) {
        this.ingot = ingot;
        this.x = vec.xCoord;
        this.y = vec.yCoord;
        this.z = vec.zCoord;
    }

    private AxisAlignedBB getBoundsFromPosition() {

        return new AxisAlignedBB(bx*x, (by*y),bz*z,(bx*x)-bx, (by*y)+by, (bz*z)-bz);
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {
        list.add(getBoundsFromPosition());
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        AxisAlignedBB box = getBoundsFromPosition();
        if (box.intersectsWith(mask)) {
            list.add(box);
        }
    }

    @Override
    public List<ItemStack> getDrops() {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        list.add(ingot.getItemStack());
        return list;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setDouble("x",x);
        compound.setDouble("y",y);
        compound.setDouble("z",z);
        tag.setTag("position", compound);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        NBTTagCompound compound = tag.getCompoundTag("position");
        this.x = compound.getDouble("x");
        this.y = compound.getDouble("y");
        this.z = compound.getDouble("z");
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
    }

    @Override
    public void writeUpdatePacket(PacketBuffer buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

}
