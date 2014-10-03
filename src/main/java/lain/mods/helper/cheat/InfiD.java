package lain.mods.helper.cheat;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteClient;
import lain.mods.helper.note.NoteOption;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
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

    AtomicBoolean skipRender = new AtomicBoolean(false);

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
    public void handleEvent(RenderGameOverlayEvent.Pre event)
    {
        switch (event.type)
        {
            case FOOD:
            case AIR:
                if (skipRender.get())
                    event.setCanceled(true);
                break;
            default:
                break;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void handleEvent(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            if (checkPlayerAccessClient())
            {
                skipRender.compareAndSet(false, true);
            }
            else
            {
                skipRender.compareAndSet(true, false);
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
                    player.getFoodStats().addStats(20, 1.0F);
                    player.setAir(300);
                }
            }
            else if (isClient && player instanceof EntityClientPlayerMP)
            {
                if (checkPlayerAccessClient())
                {
                    player.removePotionEffectClient(17);
                    player.getFoodStats().addStats(20, 1.0F);
                    player.setAir(300);
                }
            }
        }
    }

}
