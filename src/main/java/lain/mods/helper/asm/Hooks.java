package lain.mods.helper.asm;

import lain.mods.helper.cheat.Cheat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class Hooks
{

    public static boolean getAquaAffinityModifier(EntityLivingBase player, boolean value)
    {
        return Cheat.INSTANCE.getAquaAffinityModifier(player, value);
    }

    public static int getDepthStriderModifier(Entity player, int value)
    {
        return Cheat.INSTANCE.getDepthStriderModifier(player, value);
    }

    public static int getRespiration(Entity player, int value)
    {
        return Cheat.INSTANCE.getRespiration(player, value);
    }

    public static void onLivingUpdate(EntityPlayer player)
    {
        Cheat.INSTANCE.onLivingUpdate(player);
    }

}
