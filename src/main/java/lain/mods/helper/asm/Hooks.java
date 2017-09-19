package lain.mods.helper.asm;

import lain.mods.helper.cheat.Cheat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.Loader;

public class Hooks
{

    public static float applyDamageReduction(EntityPlayer player, DamageSource source, float amount)
    {
        if (DISABLED)
            return amount;
        return Cheat.INSTANCE.applyDamageReduction(player, source, amount);
    }

    public static float getArmorVisibility(EntityPlayer player, float result)
    {
        if (DISABLED)
            return result;
        return Cheat.INSTANCE.getArmorVisibility(player, result);
    }

    public static boolean isInvisible(EntityPlayer player, boolean result)
    {
        if (DISABLED)
            return result;
        return Cheat.INSTANCE.isInvisible(player, result);
    }

    public static void onLivingUpdate(EntityPlayer player)
    {
        if (DISABLED)
            return;
        Cheat.INSTANCE.onLivingUpdate(player);
    }

    private static final boolean DISABLED = !Loader.isModLoaded("lainhelper");

}
