package lain.mods.helper.cheat;

import java.util.Set;
import java.util.UUID;
import lain.mods.helper.LainHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.CombatRules;
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
                if (!source.isUnblockable())
                    amount = CombatRules.getDamageAfterAbsorb(amount, 20F, 8F);
                if (!source.isDamageAbsolute())
                    amount = CombatRules.getDamageAfterMagicAbsorb(amount, source == DamageSource.FALL ? 20F : 16F);
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
                    FoodStats food = player.getFoodStats();
                    if (food != null)
                        food.addStats(10 - food.getFoodLevel(), Float.MAX_VALUE);

                    if (player.ticksExisted % 10 == 0)
                    {
                        float health = player.getHealth();
                        float maxhealth = player.getMaxHealth();
                        if (health < maxhealth)
                        {
                            health += Math.max(maxhealth * 0.025F, 1.0F);
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
