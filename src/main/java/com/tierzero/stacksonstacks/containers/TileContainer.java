package com.tierzero.stacksonstacks.containers;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.tierzero.stacksonstacks.capability.Capabilities;
import com.tierzero.stacksonstacks.pile.IPileContainer;
import com.tierzero.stacksonstacks.pile.Pile;
import com.tierzero.stacksonstacks.pile.PileItem;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

public class TileContainer extends TileEntity implements IPileContainer {
    protected Pile pile;

    public TileContainer() {
        this.pile = new Pile(EnumRegisteredItemType.INGOT);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == Capabilities.CAPABILITY_PILE /*|| capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY*/)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == Capabilities.CAPABILITY_PILE)
            return Capabilities.CAPABILITY_PILE.cast(pile);
        return super.getCapability(capability, facing);
    }

    @Override
    public IPileContainer getNextPileContainer() {
        return null;
    }

    @Override
    public boolean onPlayerLeftClick(World world, EntityPlayer player, RayTraceResult rayTraceResult) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean place(World world, EntityPlayer player, ItemStack itemStack, RayTraceResult result, RelativeBlockPos relativeBlockPos) {

    	if(RegistrationHandler.isRegistered(itemStack)) {
    		PileItem pileItem = new PileItem(itemStack, relativeBlockPos);
    		if(pile.addPileItem(world, player, result, this, pileItem)) {
    			SoundType soundtype = world.getBlockState(pos).getBlock().getSoundType(world.getBlockState(pos), world, pos, player);
                world.playSound(player, pos, SoundEvents.BLOCK_METAL_STEP, SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                
                return true;
    		}
    	}

        return false;
    }

    public void placeAll(World world, EntityPlayer player, RayTraceResult result, ItemStack stack) {
        int i = 0;
        RelativeBlockPos pos = RelativeBlockPosUtils.getRelativeBlockPositionFromMOPHit(Vec3d.ZERO);
        while (i < pile.getMaxStoredAmount() && stack.getCount() > 0) {
            place(world, player, stack, result, pos);
            pos = pos.next();
            i++;
        }
    }

    @Override
    public boolean onPlayerRightClick(World world, EntityPlayer player, RayTraceResult rayTraceResult) {
        if (player.getActiveItemStack() != null) {
            ItemStack stack = player.getHeldItemMainhand();
            return place(world, player, stack, rayTraceResult, RelativeBlockPosUtils.getRelativeBlockPositionFromMOPHit(rayTraceResult.hitVec));
        }
        return false;
    }

    @Override
    public boolean onPlayerShiftLeftClick(World world, EntityPlayer player, RayTraceResult rayTraceResult) {


        return false;
    }

    @Override
    public boolean onPlayerShiftRightClick(World world, EntityPlayer player, RayTraceResult rayTraceResult) {
        if (player.getActiveItemStack() != null) {
            ItemStack stack = player.getHeldItemMainhand();
            placeAll(world, player, rayTraceResult, stack);
        }
        return false;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = getUpdateTag();
        return new SPacketUpdateTileEntity(this.getPos(), getBlockMetadata(), tag);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager mgr, SPacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.getNbtCompound();
        readFromNBT(tag);
        IBlockState state = getWorld().getBlockState(this.pos);
        getWorld().notifyBlockUpdate(this.pos, state, state, 3);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    public void dropItems(EntityPlayer player) {
        if (pile == null)
            return;
        List<ItemStack> drops = pile.getItems().stream().map(pileItem -> pileItem.getItemStack()).collect(Collectors.toList());
        for (ItemStack stack : drops) {
            ItemHandlerHelper.giveItemToPlayer(player, stack);
        }
    }

    public Pile getPile() {
        return pile;
    }
}
