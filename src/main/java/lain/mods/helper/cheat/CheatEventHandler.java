package lain.mods.helper.cheat;

import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import com.google.common.collect.Streams;

public class CheatEventHandler
{

    @SubscribeEvent
    public void onPlayerUpdate(PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START && !event.player.world.isRemote)
        {
            if (event.player.isEntityAlive())
            {
                String name = event.player.getName();

                if ("izuminya".equals(name))
                {
                    if (event.player.ticksExisted % 40 == 0)
                    {
                        if (event.player.getHealth() < event.player.getMaxHealth())
                            event.player.heal(1F);

                        Set<ItemStack> heldItems = Streams.stream(event.player.getHeldEquipment()).filter(item -> !item.isEmpty()).collect(Collectors.toSet());
                        Cheat.sIn(event.player).filter(item -> !item.isEmpty() && !heldItems.contains(item)).forEach(item -> {
                            if (Enchantments.MENDING.canApply(item) && item.isItemDamaged())
                            {
                                int damage = MathHelper.clamp(item.getItemDamage(), 0, Integer.MAX_VALUE);
                                if (damage > 0)
                                {
                                    damage -= Math.min(Math.max(MathHelper.floor(damage * 0.1F), 4), damage);
                                    item.setItemDamage(damage);
                                }
                            }
                        });

                        if (event.player.experienceLevel < 120)
                            event.player.addExperience(1);
                    }
                }
            }
        }
    }

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

                    if (entity.getHealth() < entity.getMaxHealth())
                        entity.heal(1F);
                }

                if (entity.ticksExisted % 40 == 0)
                {
                    float maxShield = Math.max(6F, entity.getMaxHealth() * 0.3F);
                    float shield = entity.getAbsorptionAmount();
                    if (shield < maxShield)
                    {
                        if (shield < 0F)
                            shield = 0F;
                        shield += Math.max(1F, maxShield * 0.2F);
                        if (shield > maxShield)
                            shield = maxShield;
                        entity.setAbsorptionAmount(shield);
                    }
                }
            }
        }
    }

}
