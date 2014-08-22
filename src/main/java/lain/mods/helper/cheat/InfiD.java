package lain.mods.helper.cheat;

import java.util.concurrent.atomic.AtomicBoolean;
import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteClient;
import lain.mods.helper.utils.Ref;
import lain.mods.helper.utils.SafeProcess;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class InfiD
{

    public static void load()
    {
        Ref<InfiD> ref = Ref.newRef();
        load_iD(ref);
        if (ref.get() != null)
        {
            FMLCommonHandler.instance().bus().register(ref.get());
            MinecraftForge.EVENT_BUS.register(ref.get());
        }
    }

    private static void load_iD(final Ref<InfiD> ref)
    {
        new SafeProcess()
        {
            @Override
            public void onFailed()
            {
                ref.set(new InfiD());
            }

            @Override
            public void run()
            {
                if (isClassAccessible(EntityClientPlayerMP.class))
                    ref.set(new InfiD()
                    {
                        @Override
                        void tickPlayer(EntityPlayer player)
                        {
                            if (player instanceof EntityClientPlayerMP)
                                processClient(player);
                            super.tickPlayer(player);
                        }
                    });
            }
        }.runSafe();
    }

    AtomicBoolean skipRender = new AtomicBoolean(false);

    private InfiD()
    {
    }

    @SubscribeEvent
    public void handleEvent(LivingHurtEvent event)
    {
        Entity attacker = event.source.getEntity();
        if (event.entityLiving instanceof EntityPlayerMP)
        {
            if (Note.getNote((EntityPlayerMP) event.entityLiving).get("InfiD") != null)
            {
                if (event.ammount > 0)
                {
                    if (attacker != null && event.entityLiving != attacker)
                        attacker.attackEntityFrom(DamageSource.causeThornsDamage(event.entity), 4.0F);
                    event.ammount *= 0.2F;
                }
            }
        }
    }

    @SubscribeEvent
    public void handleEvent(RenderGameOverlayEvent.Pre event)
    {
        switch (event.type)
        {
            case FOOD:
                if (skipRender.get())
                    event.setCanceled(true);
                break;
            case AIR:
                if (skipRender.get())
                    event.setCanceled(true);
                break;
            default:
                break;
        }
    }

    @SubscribeEvent
    public void handleEvent(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
            tickPlayer(event.player);
    }

    void processClient(EntityPlayer player)
    {
        if (NoteClient.instance().get("InfiD") != null)
        {
            FoodStats food = player.getFoodStats();
            if (food != null)
            {
                food.addStats(-food.getFoodLevel(), 0.0F);
                food.addStats(10, 20.0F);
                food.addStats(8, 0.0F);
            }

            if (player.getAir() < 100)
                player.setAir(player.getAir() + 200);

            player.extinguish();

            skipRender.compareAndSet(false, true);
        }
        else
        {
            skipRender.compareAndSet(true, false);
        }
    }

    void processServer(EntityPlayer player)
    {
        if (Note.getNote((EntityPlayerMP) player).get("InfiD") != null)
        {
            FoodStats food = player.getFoodStats();
            if (food != null)
            {
                food.addStats(-food.getFoodLevel(), 0.0F);
                food.addStats(10, 20.0F);
                food.addStats(8, 0.0F);
            }

            if (player.getAir() < 100)
                player.setAir(player.getAir() + 200);

            player.extinguish();
        }
    }

    void tickPlayer(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
            processServer(player);
    }

}
