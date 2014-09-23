package lain.mods.helper.cheat;

import java.util.Set;
import java.util.UUID;
import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteClient;
import lain.mods.helper.note.NoteOption;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class InfiD
{

    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));

    public static void load()
    {
        InfiD iD = !FMLCommonHandler.instance().getSide().isClient() ? new InfiD() : new InfiD()
        {
            @Override
            void tickPlayer(EntityPlayer player)
            {
                if (player instanceof EntityClientPlayerMP)
                    processClient((EntityClientPlayerMP) player);
                super.tickPlayer(player);
            }
        };
        FMLCommonHandler.instance().bus().register(iD);
    }

    private InfiD()
    {
    }

    final boolean checkPlayerAccess(EntityPlayerMP player)
    {
        Note note = Note.getNote(player);
        NoteOption option = note.get("InfiD");
        if (option == null && _MYID.contains(player.getUniqueID()))
            note.put(option = new NoteOption("InfiD", false, "DifnI"));
        return option != null && !option.value.isEmpty();
    }

    final boolean checkPlayerAccessClient()
    {
        NoteOption option = NoteClient.instance().get("InfiD");
        return option != null && !option.value.isEmpty();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void handleEvent(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
            tickPlayer(event.player);
    }

    @SideOnly(Side.CLIENT)
    void processClient(EntityClientPlayerMP player)
    {
        if (checkPlayerAccessClient())
        {
            player.removePotionEffectClient(17);
        }
    }

    void processServer(EntityPlayerMP player)
    {
        if (checkPlayerAccess(player))
        {
            player.removePotionEffect(17);
        }
    }

    void tickPlayer(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
            processServer((EntityPlayerMP) player);
    }

}
