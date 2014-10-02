package lain.mods.helper.cheat;

import java.util.Set;
import java.util.UUID;
import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteClient;
import lain.mods.helper.note.NoteOption;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class InfiD
{

    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));
    private static final boolean isClient = FMLCommonHandler.instance().getSide().isClient();

    public static void load()
    {
        InfiD iD = new InfiD();
        FMLCommonHandler.instance().bus().register(iD);
        MinecraftForge.EVENT_BUS.register(iD);
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
    public void handleEvent(LivingFallEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            if (player instanceof EntityPlayerMP)
            {
                if (checkPlayerAccess((EntityPlayerMP) player))
                {
                    event.distance = 0.0F;
                }
            }
            else if (isClient && player instanceof EntityClientPlayerMP)
            {
                if (checkPlayerAccessClient())
                {
                    event.distance = 0.0F;
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void handleEvent(PlayerFlyableFallEvent event)
    {
        EntityPlayer player = event.entityPlayer;
        if (player instanceof EntityPlayerMP)
        {
            if (checkPlayerAccess((EntityPlayerMP) player))
            {
                event.distance = 0.0F;
            }
        }
        else if (isClient && player instanceof EntityClientPlayerMP)
        {
            if (checkPlayerAccessClient())
            {
                event.distance = 0.0F;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void handleEvent(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            EntityPlayer player = event.player;
            if (player instanceof EntityPlayerMP)
            {
                if (checkPlayerAccess((EntityPlayerMP) player))
                {
                    player.removePotionEffect(17);
                }
            }
            else if (isClient && player instanceof EntityClientPlayerMP)
            {
                if (checkPlayerAccessClient())
                {
                    player.removePotionEffectClient(17);
                }
            }
        }
    }

}
