package com.primetoxinz.stacksonstacks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by tyler on 5/28/16.
 */
public class IngotPlacer {

    @SubscribeEvent(priority = EventPriority.HIGH)

    public void placeIngot(PlayerInteractEvent.RightClickBlock e) {

        EntityPlayer player = e.getEntityPlayer();
        BlockPos pos = e.getPos();
        EnumFacing side = e.getFace();
        EnumHand hand = e.getHand();
        World world = e.getWorld();
        Vec3d hit = e.getHitVec();
        ItemStack stack = e.getItemStack();

        if (stack != null) {
            if(canBeIngot(stack))
                new ItemIngotPlacer(getIngot(stack)).onItemUse(stack, player, world, pos, hand, side, (float) hit.xCoord, (float) hit.yCoord, (float) hit.zCoord);
        }
    }

    private Ingot getIngot(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.stackSize = 1;
        return new Ingot(copy);
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
}
