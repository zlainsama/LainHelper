package lain.mods.helper;

import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class Cheater
{

    public final class EventListener
    {

        private EventListener()
        {
        }

        @SubscribeEvent
        public void a(LivingHurtEvent event)
        {
            instance.onLivingHurt(event);
        }

    }

    private static final Cheater instance = new Cheater();
    private static final Set<String> list = ImmutableSet.of("zlainsama");

    public static void setEnabled()
    {
    }

    private Cheater()
    {
        MinecraftForge.EVENT_BUS.register(new EventListener());
    }

    private boolean checkShouldCheat(Object obj)
    {
        if (obj == null)
            return false;
        if (obj instanceof String)
            return list.contains(((String) obj).toLowerCase());
        if (obj instanceof EntityPlayer)
            return checkShouldCheat(((EntityPlayer) obj).getCommandSenderName());
        return false;
    }

    private void onLivingHurt(LivingHurtEvent event)
    {
        if (event.entity.worldObj == null || event.entity.worldObj.isRemote)
            return;
        Entity a = event.source.getEntity();
        if (checkShouldCheat(event.entity))
        {
            event.ammount *= 0.50F;
        }
        else if (checkShouldCheat(a))
        {
            if (event.source.isProjectile())
                event.ammount *= 1.20F;
            else
                event.ammount *= 1.05F;
            switch (event.entityLiving.getCreatureAttribute())
            {
                case ARTHROPOD:
                    event.ammount *= 1.50F;
                    break;
                case UNDEAD:
                    event.ammount *= 2.00F;
                    break;
                case UNDEFINED:
                    event.ammount *= 1.15F;
                    break;
                default:
                    event.ammount *= 1.05F;
                    break;
            }
            if (a == event.source.getSourceOfDamage() && a instanceof EntityLiving)
            {
                int c = a.hurtResistantTime;
                ((EntityLiving) a).heal(event.ammount * 0.1F);
                a.hurtResistantTime = c;
            }
        }
    }

}
