package com.primetoxinz.stacksonstacks;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import pl.asie.charset.lib.utils.RenderUtils;

import java.io.IOException;

/**
 * Created by tyler on 6/4/16.
 */
public class IngotType {
    public int color;
    public ItemStack stack;
    public IngotType(ItemStack stack, int color) {
        if(stack != null) {
            stack = stack.copy();
            stack.stackSize=1;
        }
        this.stack = stack;
        this.color = color;
    }

    public IngotType(ItemStack stack) {
        this(stack, createColor(stack));
    }
    private static int createColor(ItemStack stack) {
        TextureAtlasSprite ingotTexture;
        if(stack == null || stack.getItem() == Items.IRON_INGOT)
            return -1;
        else
            ingotTexture = RenderUtils.getSprite(stack);
        int renderColorIngot = RenderUtils.getAverageColor(ingotTexture, RenderUtils.AveragingMode.FULL);
        int renderColor = renderColorIngot == -1 ? renderColorIngot :
                (0xFF000000 | (renderColorIngot & 0x00FF00) | ((renderColorIngot & 0xFF0000) >> 16) | ((renderColorIngot & 0x0000FF) << 16));
        return renderColor*2;
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
        return new IngotType(stack,color);
    }

    public void writeUpdatePacket(PacketBuffer buf) {
        buf.writeInt(color);
        buf.writeItemStackToBuffer(stack);
    }

    public static IngotType readUpdatePacket(PacketBuffer buf) {
        int color = buf.readInt();
        ItemStack stack = null;
        try {
            stack = buf.readItemStackFromBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new IngotType(stack,color);
    }

    public int getColor() {
        return color;
    }

}
