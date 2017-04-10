package com.tierzero.stacksonstacks.containers;

import com.tierzero.stacksonstacks.pile.IPileContainer;
import com.tierzero.stacksonstacks.pile.Pile;
import com.tierzero.stacksonstacks.pile.RelativeBlockPos;
import com.tierzero.stacksonstacks.registration.EnumRegisteredItemType;
import com.tierzero.stacksonstacks.registration.RegistrationHandler;
import com.tierzero.stacksonstacks.util.RelativeBlockPosUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class TilePileContainer extends TileEntity implements IPileContainer {
    protected Pile pile;

    public TilePileContainer() {
        this.pile = new Pile(EnumRegisteredItemType.INGOT, this);

    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) pile;
        return super.getCapability(capability, facing);
    }

    @Override
    public IPileContainer getNextPileContainer() {
        return null;
    }

    public boolean removeSlot(World world, EntityPlayer player, RayTraceResult rayTrace) {
        if (rayTrace != null) {
            if (!world.isRemote) {
                RelativeBlockPos relativeBlockPos = new RelativeBlockPos(rayTrace);
                int slot = relativeBlockPos.getSlot();
                ItemStack extracted = pile.extractItem(slot, 1, false);
                if(!extracted.isEmpty())
                    ItemHandlerHelper.giveItemToPlayer(player,extracted);
                return pile.isEmpty();
            }
        }
        return false;
    }
    public boolean breakBlock(World world, EntityPlayer player, RayTraceResult rayTraceResult) {
        IBlockState state =  world.getBlockState(rayTraceResult.getBlockPos());
        state.getBlock().onBlockHarvested(world, pos, state, player);
        return world.setBlockState(pos, net.minecraft.init.Blocks.AIR.getDefaultState(), world.isRemote ? 11 : 3);
    }

    public void dropItems(EntityPlayer player) {
        if (!player.isCreative()) {
            for (int i = 0; i < pile.getSlots(); i++) {
                if (!pile.getStackInSlot(i).isEmpty())
                    ItemHandlerHelper.giveItemToPlayer(player, pile.getStackInSlot(i));
            }
        }
    }

    public boolean place(World world, EntityPlayer player, ItemStack itemStack, RayTraceResult result, RelativeBlockPos relativeBlockPos) {
        if (relativeBlockPos.isValid() && RegistrationHandler.isRegistered(itemStack)) {
            if(itemStack.isEmpty())
                return false;
            pile.insertItem(relativeBlockPos.getSlot(), itemStack.copy(), false);
            itemStack.shrink(1);
            SoundType soundtype = world.getBlockState(pos).getBlock().getSoundType(world.getBlockState(pos), world, pos, player);
            world.playSound(player, pos, SoundEvents.BLOCK_METAL_STEP, SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            return true;
        }
        return false;
    }

    public boolean placeAll(World world, EntityPlayer player, RayTraceResult result, ItemStack stack) {
        int i = 0;
        while(stack.getCount() > 0) {
            if(!place(world, player, stack, result, RelativeBlockPos.fromSlot(i)))
                return false;
            i++;
        }
        return true;
    }

    @Override
    public boolean onPlayerLeftClick(World world, EntityPlayer player, RayTraceResult rayTrace, ItemStack stack) {
        if(removeSlot(world,player,rayTrace))
            return breakBlock(world,player,rayTrace);
        return false;
    }

    @Override
    public boolean onPlayerShiftLeftClick(World world, EntityPlayer player, RayTraceResult rayTraceResult, ItemStack stack) {
        return breakBlock(world, player, rayTraceResult);
    }

    @Override
    public boolean onPlayerRightClick(World world, EntityPlayer player, RayTraceResult rayTraceResult, ItemStack stack) {
        return place(world, player, stack, rayTraceResult, RelativeBlockPosUtils.getRelativeBlockPositionFromMOPHit(rayTraceResult.hitVec));
    }


    @Override
    public boolean onPlayerShiftRightClick(World world, EntityPlayer player, RayTraceResult rayTraceResult, ItemStack stack) {
        return placeAll(world, player, rayTraceResult, stack);
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.merge(pile.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        pile.deserializeNBT(compound);
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    public Pile getPile() {
        return pile;
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }
}
