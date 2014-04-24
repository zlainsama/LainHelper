package lain.mods.helper.handlers;

import lain.mods.helper.ModAttributes;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraftforge.event.entity.EntityEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AttributeInstanceRegister
{

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityLivingBase)
        {
            BaseAttributeMap bam = ((EntityLivingBase) event.entity).getAttributeMap();

            bam.registerAttribute(ModAttributes.damageResistance);
        }
    }

}
