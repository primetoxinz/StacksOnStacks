package com.primetoxinz.stacksonstacks;

import mcmultipart.MCMultiPartMod;
import mcmultipart.multipart.Multipart;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import pl.asie.charset.lib.render.IRenderComparable;
import pl.asie.charset.lib.utils.GenericExtendedProperty;

import java.util.Arrays;
import java.util.List;

import static mcmultipart.multipart.MultipartHelper.getPartContainer;

/**
 * Created by tyler on 5/28/16.
 */

public class PartIngot extends Multipart implements IRenderComparable<PartIngot> {
    public static final GenericExtendedProperty<PartIngot> PROPERTY = new GenericExtendedProperty<PartIngot>("part",PartIngot.class);
    public IngotLocation location;
    public IngotType type;
    public PartIngot() {}

    public PartIngot(IngotLocation location, IngotType type) {
        this.location = location;
        this.type = type;
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new ExtendedBlockState(MCMultiPartMod.multipart, new IProperty[0], new IUnlistedProperty[]{PROPERTY});
    }
    public AxisAlignedBB getBounds() {
        if(location != null)
            return location.getBounds();
        return new AxisAlignedBB(0,0,0,1,1,1);
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        AxisAlignedBB box = getBounds();
        if (box.intersectsWith(mask)) {
            list.add(box);
        }
    }
    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {
        list.add(getBounds());
    }

    @Override
    public ItemStack getPickBlock(EntityPlayer player, PartMOP hit) {
        if(type != null && type.stack != null) {
            ItemStack copy = type.stack.copy();
            copy.stackSize = 64;
            return copy;
        }
        return null;
    }

    @Override
    public List<ItemStack> getDrops() {
        if(type != null && type.stack != null)
            return Arrays.asList(type.stack);
        return Arrays.asList(new ItemStack(Blocks.DIRT));
    }

    @Override
    public IBlockState getExtendedState(IBlockState state) {
        return ((IExtendedBlockState) state).withProperty(PROPERTY, this);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if(location != null)
            location.writeToNBT(tag);

        if(type != null)
            type.writeToNBT(tag);
        else {
            System.out.println("REMOVING NULL THING");
            getPartContainer(getWorld(), getPos()).removePart(this);

        }
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        location = IngotLocation.readFromNBT(tag);
        type = IngotType.readFromNBT(tag);
    }

    @Override
    public void writeUpdatePacket(PacketBuffer buf) {
        if(location != null)
            location.writeUpdatePacket(buf);
        if(type != null)
            type.writeUpdatePacket(buf);
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {
        location = IngotLocation.readUpdatePacket(buf);
        type = IngotType.readUpdatePacket(buf);
    }

    @Override
    public boolean renderEquals(PartIngot other) {
        return false;
    }

    @Override
    public int renderHashCode() {
        return 0;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return getBounds();
    }

    @Override
    public void harvest(EntityPlayer player, PartMOP hit) {
        super.harvest(player,hit);
    }



}
