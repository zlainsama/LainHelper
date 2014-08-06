package lain.mods.helper.note.network;

import lain.mods.helper.note.NoteClient;
import lain.mods.helper.note.NoteOption;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;

public class NoteSyncClient extends NoteSync
{

    @SubscribeEvent
    public void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        NoteClient.instance().clear();
    }

    @SubscribeEvent
    public void onPacketEvent(FMLNetworkEvent.ClientCustomPacketEvent event)
    {
        NoteSyncPacket packet = new NoteSyncPacket(event.packet.payload());
        switch (packet.opcode)
        {
            case 0:
                if (packet.name.isEmpty())
                    NoteClient.instance().clear();
                break;
            case 1:
                if (!packet.name.isEmpty())
                    NoteClient.instance().remove(packet.name);
                break;
            case 2:
                if (!packet.name.isEmpty())
                    NoteClient.instance().put(new NoteOption(packet.name, packet.locked, packet.value));
                break;
        }
    }

}
