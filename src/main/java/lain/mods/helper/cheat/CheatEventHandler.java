package lain.mods.helper.cheat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class CheatEventHandler
{

    @SubscribeEvent
    public void onWorldUpdate(WorldTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START && !event.world.isRemote)
        {
            for (EntityLivingBase entity : event.world.getEntities(EntityLivingBase.class, entity -> entity.isEntityAlive() && entity instanceof IEntityOwnable && Cheat.INSTANCE.getFlags(((IEntityOwnable) entity).getOwnerId()) != 0))
            {
                if (entity.ticksExisted % 20 == 0)
                {
                    if (entity.getAir() < 100)
                        entity.setAir(300);

                    entity.extinguish();
                }

                if (entity.ticksExisted % 40 == 0)
                {
                    float maxShield = Math.max(6f, entity.getMaxHealth() * 0.3f);
                    float shield = entity.getAbsorptionAmount();
                    if (shield < maxShield)
                    {
                        if (shield < 0f)
                            shield = 0f;
                        shield += Math.max(1f, maxShield * 0.2f);
                        if (shield > maxShield)
                            shield = maxShield;
                        entity.setAbsorptionAmount(shield);
                    }

                    if (entity.getHealth() < entity.getMaxHealth())
                        entity.heal(1.0F);
                }
            }
        }
    }

}
