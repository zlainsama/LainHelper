package lain.mods.helper.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.EnumMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkManager
{

    public static void registerPacket(Class<? extends NetworkPacket> packetClass)
    {
        codec.addDiscriminator(packetClass.getName().hashCode(), packetClass);
    }

    private static final FMLIndexedMessageToMessageCodec<NetworkPacket> codec = new FMLIndexedMessageToMessageCodec<NetworkPacket>()
    {

        @Override
        public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, NetworkPacket msg)
        {
            msg.readFromBuffer(source);
        }

        @Override
        public void encodeInto(ChannelHandlerContext ctx, NetworkPacket msg, ByteBuf target) throws Exception
        {
            msg.writeToBuffer(target);
        }

    };

    private static final SimpleChannelInboundHandler<NetworkPacket> handler = new SimpleChannelInboundHandler<NetworkPacket>()
    {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, NetworkPacket msg) throws Exception
        {
            if (((Side) ctx.channel().attr(NetworkRegistry.CHANNEL_SOURCE).get()).isServer())
                msg.handlePacketServer(((NetHandlerPlayServer) ctx.channel().attr(NetworkRegistry.NET_HANDLER).get()).playerEntity);
            else
                msg.handlePacketClient();
        }

    };

    private EnumMap<Side, FMLEmbeddedChannel> channels;

    public NetworkManager(String channelName)
    {
        channels = NetworkRegistry.INSTANCE.newChannel(channelName, codec);
        for (FMLEmbeddedChannel channel : channels.values())
            channel.pipeline().addAfter(channel.findChannelHandlerNameForType(codec.getClass()), "NetworkPacketHandler", handler);
    }

    public void sendTo(NetworkPacket packet, EntityPlayerMP player)
    {
        if (packet != null && player != null)
        {
            ((FMLEmbeddedChannel) channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
            ((FMLEmbeddedChannel) channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
            ((FMLEmbeddedChannel) channels.get(Side.SERVER)).writeAndFlush(packet);
        }
    }

    public void sendToAll(NetworkPacket packet)
    {
        if (packet != null)
        {
            ((FMLEmbeddedChannel) channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
            ((FMLEmbeddedChannel) channels.get(Side.SERVER)).writeAndFlush(packet);
        }
    }

    public void sendToAllAround(NetworkPacket packet, TargetPoint point)
    {
        if (packet != null && point != null)
        {
            ((FMLEmbeddedChannel) channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
            ((FMLEmbeddedChannel) channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
            ((FMLEmbeddedChannel) channels.get(Side.SERVER)).writeAndFlush(packet);
        }
    }

    public void sendToDimension(NetworkPacket packet, int dimensionId)
    {
        if (packet != null)
        {
            ((FMLEmbeddedChannel) channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
            ((FMLEmbeddedChannel) channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
            ((FMLEmbeddedChannel) channels.get(Side.SERVER)).writeAndFlush(packet);
        }
    }

    public void sendToServer(NetworkPacket packet)
    {
        if (packet != null)
        {
            ((FMLEmbeddedChannel) channels.get(Side.CLIENT)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
            ((FMLEmbeddedChannel) channels.get(Side.CLIENT)).writeAndFlush(packet);
        }
    }

}
