package lain.mods.helper.handlers;

import lain.mods.helper.Options;
import lain.mods.helper.PlayerData;
import lain.mods.helper.events.PlayerTeleportationEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class AntiMobTeleportationHandler
{

    public static void load()
    {
        AntiMobTeleportationHandler handler = new AntiMobTeleportationHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);
    }

    @SubscribeEvent
    public void handleEvent(LivingAttackEvent event)
    {
        Entity attacker = event.source.getEntity();
        if (event.entityLiving instanceof EntityPlayerMP)
        {
            if (PlayerData.get((EntityPlayerMP) event.entityLiving).getAntiMobTicks() > 0)
            {
                event.setCanceled(true);
                if (!(attacker instanceof EntityLivingBase) || attacker instanceof IBossDisplayData)
                    return;
                attacker.setDead();
            }
        }
    }

    @SubscribeEvent
    public void handleEvent(PlayerTeleportationEvent event)
    {
        if (event.entityPlayer instanceof EntityPlayerMP)
        {
            PlayerData.get((EntityPlayerMP) event.entityPlayer).setAntiMobTicks(Options.ticksAntiMobProtection);
        }
    }

    @SubscribeEvent
    public void handleEvent(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            if (event.player instanceof EntityPlayerMP)
            {
                PlayerData data = PlayerData.get((EntityPlayerMP) event.player);
                if (data.getAntiMobTicks() > 0)
                    data.setAntiMobTicks(data.getAntiMobTicks() - 1);
            }
        }
    }

}
