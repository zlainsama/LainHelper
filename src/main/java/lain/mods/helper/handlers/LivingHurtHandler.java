package lain.mods.helper.handlers;

import lain.mods.helper.ModAttributes;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class LivingHurtHandler
{

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event)
    {
        if (!event.source.canHarmInCreative() && !event.source.isDamageAbsolute())
        {
            double var1 = event.entityLiving.getAttributeMap().getAttributeInstance(ModAttributes.damageResistance).getAttributeValue();
            event.ammount *= (1.0D - var1);
        }
    }

}
