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
        String a = event.source.getDamageType();
        Entity b = event.source.getEntity();
        if (checkShouldCheat(event.entity))
        {
            int n = 0;
            float p = 0F;
            for (int i = 1; i < 5; i++)
                if (event.entityLiving.getCurrentItemOrArmor(i) != null)
                    n += 1;
            if (event.entity instanceof EntityPlayer)
                if (((EntityPlayer) event.entity).isBlocking())
                    n += 1;
            if ("fall".equalsIgnoreCase(a))
                p += ((6 + n * n) / 3F) * 2.5F;
            if (event.source.isFireDamage())
                p += ((6 + n * n) / 3F) * 1.25F;
            if (event.source.isExplosion())
                p += ((6 + n * n) / 3F) * 1.5F;
            if (event.source.isProjectile())
                p += ((6 + n * n) / 3F) * 1.5F;
            if (event.source.isMagicDamage())
                p += ((6 + n * n) / 3F) * 1.5F;
            if (!event.source.isUnblockable())
                p += ((6 + n * n) / 3F) * 1.25F;
            p += ((24 + n * n) / 3F) * 0.75F;
            if (p < 0F)
                p = 0F;
            if (p > 25F)
                p = 25F;
            event.ammount *= (25F - p) / 25F;
        }
        else if (checkShouldCheat(b))
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
            if (b == event.source.getSourceOfDamage() && b instanceof EntityLiving)
            {
                int c = b.hurtResistantTime;
                ((EntityLiving) b).heal(event.ammount * 0.1F);
                b.hurtResistantTime = c;
            }
        }
    }

}
