package lain.mods.helper.cheat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import lain.mods.helper.LainHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.common.FMLCommonHandler;
import com.google.common.collect.ImmutableSet;

public class Cheat
{

    private static boolean check(Object obj)
    {
        if (obj instanceof EntityPlayerMP)
            return _MYID.contains(((EntityPlayerMP) obj).getUniqueID());
        return false;
    }

    public static final Cheat INSTANCE = FMLCommonHandler.instance().getSide().isClient() ? new CheatClient() : new Cheat();
    protected static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));
    protected static final UUID _MODIFIER = UUID.fromString("c0d922c2-2d2f-423d-9a32-57f8ea57a86a");

    static
    {
        LainHelper.network.registerPacket(240, PacketCheatInfo.class);
    }

    public float applyDamageReduction(EntityPlayer player, DamageSource source, float amount)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags(player);
            if ((flags & 0x1) != 0)
            {
                return amount *= 0.3F;
            }
        }
        return amount;
    }

    public float getArmorVisibility(EntityPlayer player, float result)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags(player);
            if ((flags & 0x1) != 0)
            {
                return 0F;
            }
        }
        return result;
    }

    public int getFlags(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
            return check(player) ? 1 : 0;
        return 0;
    }

    public int getFlagsClient()
    {
        return 0;
    }

    public boolean isInvisible(EntityPlayer player, boolean result)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags(player);
            if ((flags & 0x1) != 0)
            {
                return true;
            }
        }
        return result;
    }

    public void onLivingUpdate(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags(player);
            if ((flags & 0x1) != 0)
            {
                if (player.isEntityAlive())
                {
                    IAttributeInstance iai = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
                    AttributeModifier am = iai.getModifier(_MODIFIER);
                    if (am != null && (am.getOperation() != 2 || am.getAmount() != -0.7D))
                    {
                        iai.removeModifier(am);
                        am = null;
                    }
                    if (am == null)
                    {
                        am = new AttributeModifier(_MODIFIER, _MODIFIER.toString(), -0.7D, 2);
                        am.setSaved(false);
                        iai.applyModifier(am);

                        float health = player.getHealth();
                        float maxhealth = player.getMaxHealth();
                        if (health > maxhealth)
                        {
                            health = maxhealth;
                            player.setHealth(health);
                        }
                    }
                    iai = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);
                    am = iai.getModifier(_MODIFIER);
                    if (am != null && (am.getOperation() != 2 || am.getAmount() != 0.3D))
                    {
                        iai.removeModifier(am);
                        am = null;
                    }
                    if (am == null)
                    {
                        am = new AttributeModifier(_MODIFIER, _MODIFIER.toString(), 0.3D, 2);
                        am.setSaved(false);
                        iai.applyModifier(am);
                    }

                    FoodStats food = player.getFoodStats();
                    if (food != null)
                        food.addStats(10 - food.getFoodLevel(), Float.MAX_VALUE);

                    int air = player.getAir();
                    if (air < 100)
                    {
                        air += 200;
                        player.setAir(air);
                    }
                    player.extinguish();

                    if (player.fallDistance > 1.0F)
                        player.fallDistance = 1.0F;

                    player.capabilities.allowFlying = true;

                    Collection<Potion> toRemovePotionEffects = new ArrayList<Potion>();
                    player.getActivePotionEffects().forEach(p -> {
                        if (p.getPotion().isBadEffect())
                            toRemovePotionEffects.add(p.getPotion());
                    });
                    toRemovePotionEffects.forEach(p -> player.removePotionEffect(p));

                    if (player.ticksExisted % 10 == 0)
                    {
                        float health = player.getHealth();
                        float maxhealth = player.getMaxHealth();
                        if (health < maxhealth)
                        {
                            health += Math.max(maxhealth * 0.05F, 1.0F);
                            if (health > maxhealth)
                                health = maxhealth;
                            player.setHealth(health);
                        }
                    }
                }
            }
        }
    }

    public void setFlags(EntityPlayer player, int flags)
    {
    }

    public void setFlagsClient(int flags)
    {
    }

}
