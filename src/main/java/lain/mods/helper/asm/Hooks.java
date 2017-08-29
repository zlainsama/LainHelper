package lain.mods.helper.asm;

import lain.mods.helper.cheat.Cheat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public class Hooks
{

    public static float applyDamageReduction(EntityPlayer player, DamageSource source, float amount)
    {
        return Cheat.INSTANCE.applyDamageReduction(player, source, amount);
    }

    public static float getArmorVisibility(EntityPlayer player, float result)
    {
        return Cheat.INSTANCE.getArmorVisibility(player, result);
    }

    public static boolean isInvisible(EntityPlayer player, boolean result)
    {
        return Cheat.INSTANCE.isInvisible(player, result);
    }

    public static void onLivingUpdate(EntityPlayer player)
    {
        Cheat.INSTANCE.onLivingUpdate(player);
    }

}
