package lain.mods.helper.cheat;

import java.util.Set;
import java.util.UUID;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

public enum Cheat
{

    MasterShield
    {

        @Override
        public float modifiyDamage(Entity owner, DamageSource source, float amount, boolean attacking)
        {
            if (!attacking)
            {
                if (owner instanceof EntityPlayer && amount > 0F)
                {
                    EntityPlayer player = (EntityPlayer) owner;

                    float n = amount;

                    if (!source.isUnblockable())
                        amount = CombatRules.getDamageAfterAbsorb(amount, 20F, 8F);
                    if (!source.isDamageAbsolute())
                        amount = CombatRules.getDamageAfterMagicAbsorb(amount, source == DamageSource.FALL ? 20F : 16F);

                    n -= amount;

                    if (n > 0F)
                        player.addExperience(Math.min(20, MathHelper.floor(n)));
                }
            }

            return amount;
        }

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

                if (player.ticksExisted % 20 == 0)
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
    LesserShield
    {

        @Override
        public float modifiyDamage(Entity owner, DamageSource source, float amount, boolean attacking)
        {
            if (!attacking)
            {
                if (owner instanceof EntityPlayer && amount > 0F)
                {
                    if (!source.isUnblockable())
                        amount = CombatRules.getDamageAfterAbsorb(amount, 20F, 8F);
                }
            }

            return amount;
        }

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

                if (player.ticksExisted % 40 == 0)
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

    public float modifiyDamage(Entity owner, DamageSource source, float amount, boolean attacking)
    {
        return amount;
    }

    public boolean shouldObtain(Entity owner)
    {
        return false;
    }

    public void tick(Entity owner)
    {
    }

}
