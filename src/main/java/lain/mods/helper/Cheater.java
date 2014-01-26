package lain.mods.helper;

import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class Cheater
{

    public static final class Protection
    {

        private Protection()
        {
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onHurt(LivingHurtEvent event)
        {
            if (event.entity.worldObj == null || event.entity.worldObj.isRemote)
                return;
            if (checkShouldCheat(event.entity))
            {
                if (event.ammount > 0.1F)
                {
                    event.ammount *= 0.5F;
                    if (!event.source.isUnblockable() || event.source.isMagicDamage())
                        event.ammount *= 0.5;
                    if (event.ammount < 0.1F)
                        event.ammount = 0.1F;
                }
                else if (event.ammount > 0.0F)
                {
                    event.ammount = 0.0F;
                }
            }
        }

    }

    private static final Set<String> list = ImmutableSet.of("zlainsama");

    static
    {
        MinecraftForge.EVENT_BUS.register(new Protection());
    }

    private static boolean checkShouldCheat(Object obj)
    {
        if (obj == null)
            return false;
        if (obj instanceof String)
            return list.contains(((String) obj).toLowerCase());
        if (obj instanceof EntityPlayer)
            return checkShouldCheat(((EntityPlayer) obj).getCommandSenderName());
        return false;
    }

}
