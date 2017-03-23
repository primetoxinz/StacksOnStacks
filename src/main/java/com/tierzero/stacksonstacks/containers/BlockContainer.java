package com.tierzero.stacksonstacks.containers;

import com.tierzero.stacksonstacks.registration.EnumRegisteredItemType;
import com.tierzero.stacksonstacks.registration.RegistrationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BlockContainer extends Block implements ITileEntityProvider {

    public BlockContainer() {
        super(Material.IRON);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileContainer();
    }


    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        TileContainer tile = (TileContainer) worldIn.getTileEntity(pos);
        System.out.println("2");
        super.onBlockClicked(worldIn, pos, playerIn);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileContainer tile = (TileContainer) worldIn.getTileEntity(pos);
        RayTraceResult result = new RayTraceResult(new Vec3d(hitX, hitY, hitZ), side, pos);
        if (RegistrationHandler.isRegistered(playerIn.getHeldItem(hand), EnumRegisteredItemType.INGOT)) {
            if (playerIn.isSneaking()) {
                return tile.onPlayerShiftRightClick(worldIn, playerIn, result);
            } else {
                return tile.onPlayerRightClick(worldIn, playerIn, result);
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        TileContainer tile = (TileContainer) worldIn.getTileEntity(pos);
        tile.dropItems(player);
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public SoundType getSoundType() {
        return SoundType.METAL;
    }
}
