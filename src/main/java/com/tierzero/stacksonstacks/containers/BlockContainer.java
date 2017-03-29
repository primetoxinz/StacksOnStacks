package com.tierzero.stacksonstacks.containers;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockContainer extends Block {

    public BlockContainer() {
        super(Material.IRON);
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        TileContainer tile = (TileContainer) worldIn.getTileEntity(pos);
        super.onBlockClicked(worldIn, pos, playerIn);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        TileContainer tile = (TileContainer) source.getTileEntity(pos);
        if(tile == null)
            return super.getBoundingBox(state,source,pos);


        int i = 0;
        for(int j = 0; j < tile.pile.getSlots(); j++) {
            if(!tile.pile.getStackInSlot(i).isEmpty())
                i=j;
        }
        return new AxisAlignedBB(0,0,0,1,1,1);
    }

    @Nullable
    @Override
    protected RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox) {

        return super.rayTrace(pos, start, end, boundingBox);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileContainer tile = (TileContainer) worldIn.getTileEntity(pos);
        RayTraceResult result = new RayTraceResult(new Vec3d(hitX, hitY, hitZ), side, pos);
        if (playerIn.isSneaking()) {
            return tile.onPlayerShiftRightClick(worldIn, playerIn, result, playerIn.getHeldItem(hand));
        } else {
            return tile.onPlayerRightClick(worldIn, playerIn, result, playerIn.getHeldItem(hand));
        }
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

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {

        TileContainer tile = (TileContainer) world.getTileEntity(pos);
        if(player.isCreative())
            tile.dropItems(player);
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileContainer();
    }
}
