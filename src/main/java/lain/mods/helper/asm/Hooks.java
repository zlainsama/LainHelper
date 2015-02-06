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

    public static float getArmorVisibility(EntityPlayerMP player, float result)
    {
        if (check(player))
            return 0F;
        return result;
    }

    public static boolean isInvisible(EntityPlayerMP player, boolean result)
    {
        if (check(player))
            return true;
        return result;
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

    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"));

}
