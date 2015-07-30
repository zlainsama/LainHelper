package lain.mods.helper.cheat;

import io.netty.buffer.ByteBuf;
import lain.mods.helper.LainHelper;
import lain.mods.helper.network.NetworkPacket;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketCheatInfo extends NetworkPacket
{

    int flags;

    public PacketCheatInfo()
    {
        this(-1);
    }

    public PacketCheatInfo(int flags)
    {
        this.flags = flags;
    }

    @Override
    public void handlePacketClient()
    {
        Cheat.INSTANCE.setFlagsClient(flags);
    }

    @Override
    public void handlePacketServer(EntityPlayerMP player)
    {
        if (flags == -1)
            LainHelper.network.sendTo(new PacketCheatInfo(Cheat.INSTANCE.getFlags(player)), player);
    }

    @Override
    public void readFromBuffer(ByteBuf buf)
    {
        flags = buf.readInt();
    }

    @Override
    public void writeToBuffer(ByteBuf buf)
    {
        buf.writeInt(flags);
    }

}