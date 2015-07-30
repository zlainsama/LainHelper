package lain.mods.helper.network;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Sharable
public class NetworkPacketHandler extends SimpleChannelInboundHandler<NetworkPacket>
{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NetworkPacket msg) throws Exception
    {
        if (ctx.channel().attr(NetworkRegistry.CHANNEL_SOURCE).get().isServer())
            msg.handlePacketServer(((NetHandlerPlayServer) ctx.channel().attr(NetworkRegistry.NET_HANDLER).get()).playerEntity);
        else
            msg.handlePacketClient();
    }

}
