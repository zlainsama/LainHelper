package lain.mods.helper.asm;

import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.collect.ImmutableSet;

public class Hooks
{

    public static boolean check(Object obj)
    {
        if (obj instanceof EntityPlayerMP)
            return _MYID.contains(((EntityPlayerMP) obj).getUniqueID());
        return false;
    }

    public static void onLivingUpdate(EntityPlayerMP player)
    {
        if (check(player))
        {
        }
    }

    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));

}
