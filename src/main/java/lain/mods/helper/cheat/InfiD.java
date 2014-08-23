package lain.mods.helper.cheat;

import lain.mods.helper.note.Note;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class InfiD
{

    public static void load()
    {
        MinecraftForge.EVENT_BUS.register(new InfiD());
    }

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

}
