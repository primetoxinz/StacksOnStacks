package com.tierzero.stacksonstacks.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/23/17
 */
public class ItemStackUtils {
    public static List<ItemStack> flatStackList(List<ItemStack> list) {
        for (ItemStack stack : list) {
            for (ItemStack stack1 : list) {
                if (stack.isEmpty() || stack1.isEmpty())
                    continue;
                if (stack.isItemEqual(stack1) && (stack.getCount() + stack1.getCount()) < stack.getMaxStackSize()) {
                    stack.grow(stack1.getCount());
                    stack1.setCount(0);
                }
            }
        }
        return list;
    }
    public static void spawnStack(World world, BlockPos pos, ItemStack stack) {
        EntityItem item = new EntityItem(world);
        item.setPosition(pos.getX(),pos.getY(),pos.getZ());
        item.setEntityItemStack(stack);
        world.spawnEntity(item);
    }

    public static void spawnStacks(World world, BlockPos pos, List<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            spawnStack(world,pos,stack);
        }
    }
}
