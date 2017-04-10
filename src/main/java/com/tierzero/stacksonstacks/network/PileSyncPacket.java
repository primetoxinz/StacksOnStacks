package com.tierzero.stacksonstacks.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 4/10/17
 */
public class PileSyncPacket implements IMessage{
    private ItemStack stack;
    private int slot;

    public PileSyncPacket(ItemStack stack, int slot) {
        this.stack = stack;
        this.slot = slot;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf,stack);
        buf.writeByte(slot);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        slot = buf.readByte();
        stack = ByteBufUtils.readItemStack(buf);
    }


    public static class PileSyncHandler implements IMessageHandler<PileSyncPacket,IMessage> {
        @Override
        public IMessage onMessage(PileSyncPacket message, MessageContext ctx) {
            //WHAT DO I DO HERE?

            return message;
        }
    }
}
