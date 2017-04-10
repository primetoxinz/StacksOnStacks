package com.tierzero.stacksonstacks.network;

import com.tierzero.stacksonstacks.lib.LibCore;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 4/10/17
 */
public class NetworkHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(LibCore.MOD_ID);
    static {
        INSTANCE.registerMessage(PileSyncPacket.PileSyncHandler.class, PileSyncPacket.class, 0, Side.CLIENT);
    }
    public void sendToWatching(World world, BlockPos pos, IMessage message) {
        if(world instanceof WorldServer) {
            WorldServer server = (WorldServer) world;
        }
    }

}
