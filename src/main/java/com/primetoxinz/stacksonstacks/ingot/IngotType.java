package com.primetoxinz.stacksonstacks.ingot;

import com.primetoxinz.stacksonstacks.Config;
import com.primetoxinz.stacksonstacks.logic.OreDictUtil;
import com.primetoxinz.stacksonstacks.render.RenderIngot;
import lib.utils.RenderUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;
import java.util.HashMap;

public class IngotType {
    public static HashMap<DummyStack, Integer> colorCache = new HashMap<>();
    public static HashMap<DummyStack,TextureAtlasSprite> spriteCache = new HashMap<>();
    public ItemStack stack;
    public int color;

    public String spriteName;
    public IngotType(ItemStack stack) {
        if(stack != null) {
            stack = stack.copy();
            stack.stackSize=1;
        }

        this.stack = stack;
        if(stack != null) {
            findColor();
            if(Config.useIngotBlockTexture)
                findTexture();
            else
                spriteName = RenderIngot.DEFAULT_TEXTURE.toString();
        }
    }

    public void findColor() {
        DummyStack dummy = getDummy();
        if (colorCache.containsKey(dummy)) {
            color = colorCache.get(dummy);
        } else {
            color = RenderUtils.getAverageColor(stack);
            colorCache.put(dummy, color);
        }
    }

    public void findTexture() {

        DummyStack dummy = getDummy();
        if(spriteCache.containsKey(dummy)) {
            spriteName = spriteCache.get(dummy).getIconName();
        } else {
            ItemStack compress = OreDictUtil.getCompressIngotBlock(stack);
            TextureAtlasSprite sprite;
            if(compress != null) {
                sprite = RenderUtils.getSprite(compress);
                colorCache.put(dummy,0);
            } else {
                sprite = RenderUtils.textureGetter.apply(RenderIngot.DEFAULT_TEXTURE);
            }
            spriteCache.put(dummy,sprite);
            spriteName = sprite.getIconName();
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

    public TextureAtlasSprite getSprite() {
        return spriteCache.get(getDummy());
    }

    public DummyStack getDummy() {
        return new DummyStack(stack);
    }

}
