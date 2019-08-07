package lain.mods.helper.cheat;

import java.util.Set;
import java.util.UUID;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public enum Cheat
{

    MasterShield
    {

        @Override
        public boolean shouldObtain(Entity owner)
        {
            return isMaster(owner);
        }

        @Override
        public void tick(Entity owner)
        {
            if (!owner.isEntityAlive())
                return;

            if (owner instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) owner;

                if (player.ticksExisted % 5 == 0)
                {
                    float health = player.getHealth();
                    float maxHealth = player.getMaxHealth();
                    float shield = player.getAbsorptionAmount();
                    if (health > 0F && shield > 0F && health < shield && health < maxHealth)
                    {
                        float heal = Math.min(Math.min(1F, shield), maxHealth - health);
                        player.setHealth(health + heal);
                        player.setAbsorptionAmount(shield - heal);
                    }
                }

                if (player.ticksExisted % 15 == 0)
                {
                    float maxShield = Math.max(10F, player.getMaxHealth());
                    float shield = player.getAbsorptionAmount();
                    if (shield < maxShield)
                    {
                        if (shield < 0F)
                            shield = 0F;
                        else
                        {
                            shield += Math.max(1F, maxShield * 0.1F);
                            if (shield > maxShield)
                                shield = maxShield;
                        }
                        player.setAbsorptionAmount(shield);
                    }
                }
            }
        }

    },
    Shield
    {

        @Override
        public boolean shouldObtain(Entity owner)
        {
            return owner instanceof EntityPlayer && "izuminya".equals(owner.getName());
        }

        @Override
        public void tick(Entity owner)
        {
            if (!owner.isEntityAlive())
                return;

            if (owner instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) owner;

                if (player.ticksExisted % 5 == 0)
                {
                    float health = player.getHealth();
                    float maxHealth = player.getMaxHealth();
                    float shield = player.getAbsorptionAmount();
                    if (health > 0F && shield > 0F && health < shield && health < maxHealth)
                    {
                        float heal = Math.min(Math.min(1F, shield), maxHealth - health);
                        player.setHealth(health + heal);
                        player.setAbsorptionAmount(shield - heal);
                    }
                }

                if (player.ticksExisted % 30 == 0)
                {
                    float maxShield = Math.max(10F, player.getMaxHealth());
                    float shield = player.getAbsorptionAmount();
                    if (shield < maxShield)
                    {
                        if (shield < 0F)
                            shield = 0F;
                        else
                        {
                            shield += Math.max(1F, maxShield * 0.1F);
                            if (shield > maxShield)
                                shield = maxShield;
                        }
                        player.setAbsorptionAmount(shield);
                    }
                }
            }
        }

    };

    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));

    private static final boolean isMaster(Object obj)
    {
        if (obj instanceof EntityPlayer)
            return _MYID.contains(((EntityPlayer) obj).getUniqueID());
        if (obj instanceof UUID)
            return _MYID.contains(obj);
        return false;
    }

    public boolean shouldObtain(Entity owner)
    {
        return false;
    }

    public void tick(Entity owner)
    {
    }

}
