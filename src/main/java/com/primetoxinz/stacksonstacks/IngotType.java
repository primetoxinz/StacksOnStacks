package com.primetoxinz.stacksonstacks;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import pl.asie.charset.lib.utils.RenderUtils;

import java.io.IOException;

import static pl.asie.charset.lib.utils.RenderUtils.AveragingMode.FULL;

/**
 * Created by tyler on 6/4/16.
 */
public class IngotType {

    public ItemStack stack;
    public int color;
    public IngotType(ItemStack stack) {
        if(stack != null) {
            stack = stack.copy();
            stack.stackSize=1;
        }
        this.stack = stack;
        if(stack != null) {
            TextureAtlasSprite sprite = RenderUtils.getSprite(stack);
            if (sprite != null)
                setColor(RenderUtils.getAverageColor(sprite, FULL));
        }
    }

    public IngotType setColor(int color) {
        this.color = color;
        return this;
    }
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if(stack != null)
            stack.writeToNBT(tag);
        tag.setInteger("color",color);
        return tag;
    }
    public static IngotType readFromNBT(NBTTagCompound tag) {
        ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
        int color = tag.getInteger("color");
        return new IngotType(stack).setColor(color);
    }

    public void writeUpdatePacket(PacketBuffer buf) {
        buf.writeItemStackToBuffer(stack);
    }

    public static IngotType readUpdatePacket(PacketBuffer buf) {
        ItemStack stack = null;
        try {
            stack = buf.readItemStackFromBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new IngotType(stack);
    }

    public int getColor() {
        return color;
    }
}
