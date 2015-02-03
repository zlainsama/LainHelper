package lain.mods.helper.asm;

import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.collect.ImmutableSet;

public class Hooks
{

    private static boolean check(EntityPlayerMP player)
    {
        return _MYID.contains(player.getUniqueID());
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

    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));

}
