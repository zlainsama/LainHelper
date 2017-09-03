package lain.mods.helper.cheat;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lain.mods.helper.LainHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
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
    protected static final UUID _MODIFIER = UUID.fromString("c0d922c2-2d2f-423d-9a32-57f8ea57a86a");

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
                    player.getActivePotionEffects().stream().map(pe -> pe.getPotion()).filter(p -> p.isBadEffect()).collect(Collectors.toSet()).forEach(player::removePotionEffect);

                    IntStream.range(0, player.inventory.getSizeInventory()).mapToObj(player.inventory::getStackInSlot).filter(s -> {
                        if (!s.isEmpty() && EnumEnchantmentType.BREAKABLE.canEnchantItem(s.getItem()) && s.isItemDamaged())
                            return true;
                        return false;
                    }).forEachOrdered(s -> s.setItemDamage(0));
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
