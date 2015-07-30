package lain.mods.helper.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;

public class NetworkPacketCodec extends FMLIndexedMessageToMessageCodec<NetworkPacket>
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

}
