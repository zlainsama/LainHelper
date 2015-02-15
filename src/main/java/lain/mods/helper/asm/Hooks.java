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
            switch (potioneffect.getPotionID())
            {
                case 17:
                    return false;
            }
        }
        return result;
    }

    public static void onLivingUpdate(EntityPlayerMP player)
    {
        if (check(player))
        {
            if (player.isEntityAlive() && player.ticksExisted % 10 == 0)
                player.heal(player.getMaxHealth() * 0.05F);
        }
    }

    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"));

}
