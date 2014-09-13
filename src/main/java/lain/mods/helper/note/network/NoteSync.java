package lain.mods.helper.note.network;

import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteOption;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import com.google.common.base.Strings;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

public class NoteSync
{

    public static final String CHANNELNAME = "NoteSyncChannel";

    private static FMLEventChannel channel;
    private boolean isEnabled;

    public NoteSync()
    {
        if (channel == null)
            channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(CHANNELNAME);
    }

    public boolean isEnabled()
    {
        return isEnabled;
    }

    @SubscribeEvent
    public void onPacketEvent(FMLNetworkEvent.ServerCustomPacketEvent event)
    {
        NoteSyncPacket packet = new NoteSyncPacket(event.packet.payload());
        switch (packet.opcode)
        {
            case 0:
                break;
            case 1:
                break;
            case 2:
                if (!packet.name.isEmpty())
                {
                    Note note = Note.getNote(((NetHandlerPlayServer) event.handler).playerEntity);
                    if (note != null)
                    {
                        NoteOption option = note.get(packet.name);
                        if (option != null)
                        {
                            if (option.locked == packet.locked == false)
                                note.put(new NoteOption(packet.name, packet.locked, packet.value));
                        }
                    }
                }
                break;
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
            sendNoteOption((EntityPlayerMP) event.player, null);
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
            sendNoteOption((EntityPlayerMP) event.player, null);
    }

    @SubscribeEvent
    public void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
            sendNoteOption((EntityPlayerMP) event.player, null);
    }

    public void sendNoteOption(EntityPlayerMP player, String name)
    {
        Note note = Note.getNote(player);
        if (note == null) // wont happen
            channel.sendTo(new NoteSyncPacket("", 0, false, "").createPacket(), player);
        else
        {
            if (Strings.isNullOrEmpty(name)) // sync all options
            {
                channel.sendTo(new NoteSyncPacket("", 0, false, "").createPacket(), player); // clear first
                for (String n : note.names())
                {
                    NoteOption option = note.get(n);
                    channel.sendTo(new NoteSyncPacket(option.name, 2, option.locked, option.value).createPacket(), player);
                }
            }
            else
            {
                NoteOption option = note.get(name);
                if (option == null)
                    channel.sendTo(new NoteSyncPacket(name, 1, false, "").createPacket(), player);
                else
                    channel.sendTo(new NoteSyncPacket(option.name, 2, option.locked, option.value).createPacket(), player);
            }
        }
    }

    public void setDisabled()
    {
        if (!isEnabled)
            return;
        channel.unregister(this);
        FMLCommonHandler.instance().bus().unregister(this);
        isEnabled = false;
    }

    public void setEnabled()
    {
        if (isEnabled)
            return;
        channel.register(this);
        FMLCommonHandler.instance().bus().register(this);
        isEnabled = true;
    }

}
