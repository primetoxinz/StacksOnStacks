package com.primetoxinz.stacksonstacks;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.MultipartHelper;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class IngotPlacer {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void placeIngot(PlayerInteractEvent.RightClickBlock e) {
        onItemUse(e.getItemStack(), e.getEntityPlayer(), e.getWorld(), e.getPos(), e.getHand(), e.getFace(), e.getHitVec());
    }

    private IngotLocation getPositionFromHit(Vec3d hit, BlockPos pos) {
        int x1 = Math.abs(pos.getX());
        int y1 = Math.abs(pos.getY());
        int z1 = Math.abs(pos.getZ());
        double x2 = Math.abs(hit.xCoord)-x1;
        double y2 = Math.abs(hit.yCoord)-y1;
        double z2 = Math.abs(hit.zCoord)-z1;
        x2 = Math.ceil(x2*4d);
        y2 = Math.ceil(y2*8d);
        z2 = Math.ceil(z2*2d);
        return new IngotLocation(x2,y2,z2);
    }



    private boolean canBeIngot(ItemStack stack) {
        int[] ids = OreDictionary.getOreIDs(stack);
        for(int id: ids) {
            String name = OreDictionary.getOreName(id);
            System.out.println(String.format("%s, %s",id,name));
            if(name.startsWith("ingot")) {
                return true;
            }
        }
        return  stack.getUnlocalizedName().contains("ingot");
    }

    private boolean place(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {

        if (!player.canPlayerEdit(pos, side, stack)) return false;

        IMultipart part = new PartIngot(getPositionFromHit(hit,pos),new IngotType(stack));

        if (MultipartHelper.canAddPart(world, pos, part)) {
            if (!world.isRemote) MultipartHelper.addPart(world, pos, part);
            consumeItem(stack);

            SoundType sound = SoundType.METAL;
            if (sound != null)
                world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, sound.getVolume(), sound.getPitch());

            return true;
        }

        return false;
    }

    private void consumeItem(ItemStack stack) {
        stack.stackSize--;
    }

    private EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side,
                                       Vec3d hit) {
        if(stack == null || !canBeIngot(stack))
            return EnumActionResult.FAIL;
        double depth = ((hit.xCoord * 2 - 1) * side.getFrontOffsetX() + (hit.yCoord * 2 - 1) * side.getFrontOffsetY()
                + (hit.zCoord * 2 - 1) * side.getFrontOffsetZ());
        if (depth < 1 && place(world, pos, side, hit, stack, player)) return EnumActionResult.SUCCESS;
        if (place(world, pos.offset(side), side.getOpposite(), hit, stack, player)) return EnumActionResult.SUCCESS;
        return EnumActionResult.PASS;
    }
}