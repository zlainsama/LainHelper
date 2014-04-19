package lain.mods.helper.handlers;

import lain.mods.helper.Options;
import lain.mods.helper.skills.Skill;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class LivingHurtHandler
{

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event)
    {
        if (Options.enableSkills)
        {
            for (Skill skill : Skill.values())
                skill.onLivingHurt(event);
        }
    }

}
