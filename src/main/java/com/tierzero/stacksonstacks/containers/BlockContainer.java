package com.tierzero.stacksonstacks.containers;

import com.tierzero.stacksonstacks.pile.RelativeBlockPos;
import com.tierzero.stacksonstacks.util.RelativeBlockPosUtils;
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
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.IntStream;

public class BlockContainer extends Block {

    public BlockContainer() {
        super(Material.IRON);
    }


    public static Optional<TileContainer> getTile(IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        return te != null && te instanceof TileContainer ? Optional.of((TileContainer) te) : Optional.empty();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        Optional<TileContainer> te = getTile(worldIn, pos);
        if (te.isPresent()) {
            RayTraceResult result = new RayTraceResult(new Vec3d(hitX, hitY, hitZ), side, pos);
            if (playerIn.isSneaking()) {
                return te.get().onPlayerShiftRightClick(worldIn, playerIn, result, playerIn.getHeldItem(hand));
            } else {
                return te.get().onPlayerRightClick(worldIn, playerIn, result, playerIn.getHeldItem(hand));
            }
        }
        return false;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        Optional<TileContainer> te = getTile(worldIn, pos);
        te.ifPresent(t -> t.dropItems(player));
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        Pair<Vec3d, Vec3d> vectors = RelativeBlockPosUtils.getRayTraceVectors(player);
        RayTraceResult hit = collisionRayTrace(state, world, pos, vectors.getLeft(), vectors.getRight());
        Optional<TileContainer> tile = getTile(world, pos);
        if(tile.isPresent()) {
            if(player.isSneaking()) {
                return tile.get().onPlayerShiftLeftClick(world,player,hit, null);
            } else {
                return tile.get().onPlayerLeftClick(world,player,hit, null);
            }
        }
        return false;
    }

    @Override
    public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
        Optional<TileContainer> te = getTile(world, pos);
        return te.map(t -> IntStream.range(0, t.getPile().getSlots())).get()//
                .mapToObj(i -> Pair.of(i, RelativeBlockPos.fromSlot(i).getSlotCollision(world, pos, start, end)))//
                .filter(p -> p.getValue() != null)//
                .min(Comparator.comparingDouble(a -> a.getValue().hitVec.squareDistanceTo(start)))//
                .map(p -> {//
                    RayTraceResult hit = new RayTraceResult(p.getValue().hitVec, p.getValue().sideHit, p.getValue().getBlockPos());//
                    hit.hitInfo = p.getValue();//
                    hit.subHit = new RelativeBlockPos(hit).toSlotIndex();//
//                    System.out.println(hit.subHit);
                    return hit;//
                }).orElse(null);//
    }


    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {


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
        if (player.isCreative())
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
