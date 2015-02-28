package lain.mods.helper.asm;

import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import com.google.common.collect.ImmutableSet;

public class Hooks
{

    public static boolean check(Object obj)
    {
        if (obj instanceof EntityPlayerMP)
            return _MYID.contains(((EntityPlayerMP) obj).getUniqueID());
        return false;
    }

    public static boolean isPotionApplicable(EntityPlayerMP player, PotionEffect potioneffect, boolean result)
    {
        if (check(player))
        {
            if (_IMMUNE.contains(potioneffect.getEffectName()))
                return false;
        }
        return result;
    }

    public static void onLivingUpdate(EntityPlayerMP player)
    {
        if (check(player))
        {
            if (player.isEntityAlive() && player.ticksExisted % 3 == 0)
                player.heal(Math.max(1F, player.getMaxHealth() * 0.05F));
        }
    }

    public static boolean shouldHeal(EntityPlayerMP player, boolean result)
    {
        if (check(player))
            return false;
        return result;
    }

    private static final Set<String> _IMMUNE = ImmutableSet.of("potion.hunger", "potion.poison", "potion.wither", "ic2.potion.radiation");
    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));

}
