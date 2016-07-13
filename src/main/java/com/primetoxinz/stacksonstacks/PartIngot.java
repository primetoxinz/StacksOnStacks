package com.primetoxinz.stacksonstacks;

import mcmultipart.MCMultiPartMod;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.Multipart;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
        if( player != null && player.isSneaking()) {
            IMultipartContainer container = MultipartHelper.getPartContainer(getWorld(),getPos());
            for(IMultipart part: container.getParts()) {
                if (part instanceof PartIngot) {
                    PartIngot ingot = (PartIngot) part;
                    ingot.drop();
                }
            }
        } else {
            super.harvest(player,hit);
        }
    }

    public void drop() {
        World world = getWorld();
        BlockPos pos = getPos();
        double x = pos.getX() + 0.5, y = pos.getY() + 0.5, z = pos.getZ() + 0.5;

       if(!world.isRemote && world.getGameRules().getBoolean("doTileDrops")
                && !world.restoringBlockSnapshots) {
            for (ItemStack stack : getDrops()) {
                EntityItem item = new EntityItem(world, x, y, z, stack);
                item.setDefaultPickupDelay();
                world.spawnEntityInWorld(item);
            }
        }
        getContainer().removePart(this);

    }

    @Override
    protected void notifyBlockUpdate() {
        super.notifyBlockUpdate();
    }

    @Override
    public void onNeighborBlockChange(Block block) {
        super.onNeighborBlockChange(block);
        World world = getWorld();
        IBlockState down = world.getBlockState(getPos().down());
        if(down.getMaterial() == Material.AIR) {
            drop();
        }
    }

    @Override
    public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
        return true;
    }
}
