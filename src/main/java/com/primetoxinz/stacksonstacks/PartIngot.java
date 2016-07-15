package com.primetoxinz.stacksonstacks;

import mcmultipart.MCMultiPartMod;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.Multipart;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import org.lwjgl.util.vector.Vector3f;
import pl.asie.charset.lib.render.IRenderComparable;

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
        return new AxisAlignedBB(0,0,0,8/16d,2/16d,4/16d);
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
    public AxisAlignedBB getRenderBoundingBox() {
        return getBounds();
    }

    @Override
    public void harvest(EntityPlayer player, PartMOP hit) {
        notifyPartUpdate();
        if( player != null && player.isSneaking()) {
            dropAll(this);
        } else {
            super.harvest(player,hit);
        }
    }

    public static void drop(PartIngot ingot) {
        World world = ingot.getWorld();
        BlockPos pos = ingot.getPos();
        if (world == null || pos == null)
            return;
        double x = pos.getX() + 0.5, y = pos.getY() + 0.5, z = pos.getZ() + 0.5;
        if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops")
                && !world.restoringBlockSnapshots) {
            for (ItemStack stack : ingot.getDrops()) {
                EntityItem item = new EntityItem(world, x, y, z, stack);
                item.setDefaultPickupDelay();
                world.spawnEntityInWorld(item);
            }
        }
        if (ingot.getContainer().getParts().size() == 1) {
            world.setBlockToAir(pos);
        }
//        if (ingot != null && ingot.getContainer() != null)
        try {ingot.getContainer().removePart(ingot);}catch(IllegalStateException e) {}
    }

    public void dropAll(PartIngot ingot) {
        IMultipartContainer container = ingot.getContainer();
        if (container != null) {
            for (IMultipart part : container.getParts()) {
                if (part instanceof PartIngot) {
                    PartIngot i = (PartIngot) part;
                    drop(i);
                }
            }
        }

    }

    @Override
    public void onPartChanged(IMultipart part) {
        if (part instanceof PartIngot) {
            PartIngot ingot = (PartIngot) part;
            if (!onSurface(ingot)) {
                dropAll(ingot);
            }
        }
    }

    @Override
    public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
        return true;
    }

    public IngotLocation getLocation() {
        return location;
    }

    public Vector3f getRelativeLocation() {
        return location.getRelativeLocation();
    }

    public boolean isBelow(PartIngot ingot) {
        Vector3f b = ingot.getRelativeLocation();
        Vector3f i = getRelativeLocation();
        return b.x == i.x && b.y - 2 == i.y && b.z == i.z;
    }

    public boolean onSurface(PartIngot ingot) {
        if (ingot != null && ingot.getWorld() != null && !ingot.getWorld().isAirBlock(getPos().down()))
            return true;
        return false;
    }

    @Override
    public boolean renderEquals(PartIngot other) {
        return false;
    }

    @Override
    public int renderHashCode() {
        return 0;
    }
}
