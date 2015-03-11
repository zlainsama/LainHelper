package lain.mods.helper.asm;

import lain.mods.helper.cheat.Cheat;
import net.minecraft.entity.player.EntityPlayer;

public class Hooks
{

    public static void onLivingUpdate(EntityPlayer player)
    {
        Cheat.onLivingUpdate(player);
    }

}
