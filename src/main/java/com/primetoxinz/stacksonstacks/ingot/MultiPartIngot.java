package com.primetoxinz.stacksonstacks.ingot;

import static mcmultipart.multipart.MultipartHelper.getPartContainer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.primetoxinz.stacksonstacks.capability.IIngotCount;
import com.primetoxinz.stacksonstacks.capability.IngotCountProvider;

import lib.render.IRenderComparable;
import lib.utils.GenericExtendedProperty;
import mcmultipart.MCMultiPartMod;
import mcmultipart.block.TileMultipartContainer;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.INormallyOccludingPart;
import mcmultipart.multipart.Multipart;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created by tyler on 5/28/16.
 */

public class MultiPartIngot extends Multipart implements IRenderComparable<MultiPartIngot>, INormallyOccludingPart {

    public static final GenericExtendedProperty<MultiPartIngot> PROPERTY = new GenericExtendedProperty<MultiPartIngot>("part", MultiPartIngot.class);

    private IngotLocation location;
    private RegisteredIngot ingot;
    
    public MultiPartIngot() {
    	this(new IngotLocation(0, 0, 0, Axis.X), null);
    }

    public MultiPartIngot(IngotLocation location, RegisteredIngot type) {
        this.location = location;
        this.ingot = type;
    }
    
    //BlockState functions
    @Override
    public BlockStateContainer createBlockState() {
        return new ExtendedBlockState(MCMultiPartMod.multipart, new IProperty[0], new IUnlistedProperty[]{PROPERTY});
    }
    
    @Override
    public IBlockState getExtendedState(IBlockState state) {
        return ((IExtendedBlockState) state).withProperty(PROPERTY, this);
    }
    
    //NBT functions
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if(location != null)
            location.writeToNBT(tag);
        if(ingot != null)
            ingot.writeToNBT(tag);
        else {
            getPartContainer(getWorld(), getPos()).removePart(this);
        }
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        location = IngotLocation.readFromNBT(tag);
        ingot = RegisteredIngot.readFromNBT(tag);
    }
    
    //Network Functions
    @Override
    public void writeUpdatePacket(PacketBuffer buf) {
        if(location != null) {
            location.writeUpdatePacket(buf);
        } 
        
        if(ingot != null) {
            ingot.writeUpdatePacket(buf);
        }
    }    
    
    @Override
    public void readUpdatePacket(PacketBuffer buf) {
        location = IngotLocation.readUpdatePacket(buf);
        ingot = RegisteredIngot.readUpdatePacket(buf);
    }
    
    //Player-Interaction functions    
    @Override
    public void onAdded() {
        TileEntity tile = getWorld().getTileEntity(getPos());
        if(tile instanceof TileMultipartContainer && hasIngotCountCapability(tile)) {
        	IIngotCount ingotCount = getIngotCountCapability(tile);
        	ingotCount.addIngot();
        }
    }

    @Override
    public void onRemoved() {
        TileEntity tile = getWorld().getTileEntity(getPos());
        if(tile instanceof TileMultipartContainer && hasIngotCountCapability(tile)) {
        	IIngotCount ingotCount = getIngotCountCapability(tile);
        	ingotCount.removeIngot();
        }
    }

    @Override
    public ItemStack getPickBlock(EntityPlayer player, PartMOP hit) {
    	BlockPos hitBlockPos = hit.getBlockPos();
    	
        TileEntity tile = getWorld().getTileEntity(hitBlockPos);
        if(tile instanceof TileMultipartContainer && hasIngotCountCapability(tile)) {
        	IIngotCount ingotCount = getIngotCountCapability(tile);
        	ItemStack pickedStack = ingot.asItemStack();
        	
        	pickedStack.stackSize = ingotCount.getCount();
        	
        	return pickedStack;
        }

        return null;
    }

    @Override
    public List<ItemStack> getDrops() {
        if(ingot != null)
            return Arrays.asList(ingot.asItemStack());
        return Collections.emptyList();
    }



    @Override
    public void harvest(EntityPlayer player, PartMOP hit) {
        if( player != null && player.isSneaking()) {
            dropAll(player,MultipartHelper.getPartContainer(player.worldObj,hit.getBlockPos()));
        } else {
            World world = getWorld();
            BlockPos pos = getPos();
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;

            if ((player == null || !player.capabilities.isCreativeMode) && !world.isRemote && world.getGameRules().getBoolean("doTileDrops")
                    && !world.restoringBlockSnapshots) {
                for (ItemStack stack : getDrops()) {
                    if(player == null || !player.inventory.addItemStackToInventory(stack)) {
                        EntityItem item = new EntityItem(world, x, y, z,stack);
                        item.setDefaultPickupDelay();
                        world.spawnEntityInWorld(item);
                    }
                }
            }
            getContainer().removePart(this);
        }
    }


    public void dropAll(EntityPlayer player, IMultipartContainer container) {
        notifyBlockUpdate();
        Iterator<IMultipart> iter = (Iterator<IMultipart>) container.getParts().iterator();
        while (iter.hasNext()) {
             IMultipart part = iter.next();
             if (part instanceof MultiPartIngot) {
            	 ((MultiPartIngot) part).drop(player);
             }
        }
    }

    public void drop(EntityPlayer player) {
        World world = getWorld();
        BlockPos pos = getPos();
        
        if(world != null && pos != null) {
        	double offset = 0.5;
            double x = pos.getX() + offset; 
            double y = pos.getY() + offset;
            double z = pos.getZ() + offset;
            
            if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops") && !world.restoringBlockSnapshots) {
                if(player == null || !player.inventory.addItemStackToInventory(getDrops().get(0))) {
                    EntityItem item = new EntityItem(world, x, y, z, getDrops().get(0));
                    item.setDefaultPickupDelay();
                    world.spawnEntityInWorld(item);
                }
            }
            if (getContainer() != null && getContainer().getParts() != null && !getContainer().getParts().isEmpty())
                    getContainer().removePart(this);
            notifyBlockUpdate();
        }

    }
    
    //Rendering
    @Override
    public boolean renderEquals(MultiPartIngot other) {
        return false;
    }

    @Override
    public int renderHashCode() {
        return 0;
    }


    //Location
    public IngotLocation getLocation() {
        return location;
    }

    public Vec3d getRelativeLocation() {
        return location.getRelativeLocation();
    }

    //Ingot
    public RegisteredIngot getRegisteredIngot() {
        return ingot;
    }
    
    //AABB Functions
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
    public void addOcclusionBoxes(List<AxisAlignedBB> list) {
        list.add(getBounds());
    }
    
    public AxisAlignedBB getBounds() {
        if(location != null) {
            return location.getBounds();
        }
        return new AxisAlignedBB(0,0,0,8/16d,2/16d,4/16d);
    }
    
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return getBounds();
    }
    
    //Helper Functions
    private static boolean hasIngotCountCapability(TileEntity tileEntity) {
    	return tileEntity.hasCapability(IngotCountProvider.CAPABILITY_INGOT_COUNT, null);
    }
    
    private static IIngotCount getIngotCountCapability(TileEntity tileEntity) {
    	return tileEntity.getCapability(IngotCountProvider.CAPABILITY_INGOT_COUNT, null);
    }
}
