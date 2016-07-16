package com.primetoxinz.stacksonstacks;

import lib.utils.RenderUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;
import java.util.HashMap;

import static lib.utils.RenderUtils.AveragingMode.FULL;

public class IngotType {
    public static HashMap<DummyStack, Integer> colorCache = new HashMap<>();
    public ItemStack stack;
    public int color;
    public IngotType(ItemStack stack) {
        if(stack != null) {
            stack = stack.copy();
            stack.stackSize=1;
        }
        this.stack = stack;

        if(stack != null) {

            DummyStack dummy = new DummyStack(stack);
            if (colorCache.containsKey(dummy)) {
                color = colorCache.get(dummy);
            } else {
                TextureAtlasSprite sprite = RenderUtils.getSprite(stack);
                color = RenderUtils.getAverageColor(sprite, FULL);
                colorCache.put(dummy, color);
            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if(stack != null)
            stack.writeToNBT(tag);
        tag.setInteger("color",color);

        return tag;
    }
    public static IngotType readFromNBT(NBTTagCompound tag) {
        ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
        IngotType type = new IngotType(stack);
        type.color = tag.getInteger("color");

        return type;
    }

    public void writeUpdatePacket(PacketBuffer buf) {
        buf.writeItemStackToBuffer(stack);
        buf.writeInt(color);
    }

    public static IngotType readUpdatePacket(PacketBuffer buf) {
        ItemStack stack = null;
        try {
            stack = buf.readItemStackFromBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        IngotType type = new IngotType(stack);
        type.color = buf.readInt();
        return type;
    }
    public int getColor() {
        return color;
    }


    public static class DummyStack {
        Item item;
        int meta;
        public DummyStack(ItemStack stack) {
            this(stack.getItem(), stack.getMetadata());
        }
        public  DummyStack(Item item, int meta) {
            this.item = item;
            this.meta = meta;
        }

        @Override
        public boolean equals(Object o) {
            return ((DummyStack) o).item == this.item &&
            ((DummyStack) o).meta == this.meta;
        }

        @Override
        public int hashCode() {
            return Item.getIdFromItem(item)*meta;
        }
    }
}
