package com.primetoxinz.stacksonstacks;

import com.primetoxinz.stacksonstacks.render.RenderIngot;
import lib.utils.RenderUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;
import java.util.HashMap;

public class IngotType {
    public static HashMap<DummyStack, Integer> colorCache = new HashMap<>();
    public static HashMap<DummyStack,String> spriteCache = new HashMap<>();
    public ItemStack stack;
    public int color;


    public String sprite;
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
                sprite = RenderIngot.DEFAULT_TEXTURE.toString();
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
            sprite = spriteCache.get(dummy);
        } else {
            ItemStack compress = IngotPlacer.getCompressIngotBlock(stack);
            if(compress != null) {
                sprite = RenderUtils.getSprite(compress).getIconName();
                colorCache.put(dummy,0);
            } else {
                sprite = RenderUtils.textureGetter.apply(RenderIngot.DEFAULT_TEXTURE).getIconName();
            }
            System.out.println(sprite);
            spriteCache.put(dummy,sprite);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if(stack != null)
            stack.writeToNBT(tag);
        tag.setInteger("color",color);
        if(sprite != null)
        tag.setString("sprite",sprite);
        return tag;
    }
    public static IngotType readFromNBT(NBTTagCompound tag) {
        ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
        IngotType type = new IngotType(stack);
        type.color = tag.getInteger("color");
        type.sprite = tag.getString("sprite");
        return type;
    }

    public void writeUpdatePacket(PacketBuffer buf) {
        buf.writeItemStackToBuffer(stack);
        buf.writeInt(color);
        buf.writeInt(sprite.length());
        buf.writeString(sprite);
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
        int l = buf.readInt();
        type.sprite = buf.readStringFromBuffer(l);

        return type;
    }
    public int getColor() {
        return color;
    }

    public String getSprite() {
        return sprite;
    }

    public DummyStack getDummy() {
        return new DummyStack(stack);
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
