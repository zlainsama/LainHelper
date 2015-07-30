package lain.mods.helper.cheat;

import java.util.Set;
import java.util.UUID;
import lain.mods.helper.LainHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));

    static
    {
        LainHelper.network.registerPacket(240, PacketCheatInfo.class);
    }

    public boolean getAquaAffinityModifier(EntityLivingBase player, boolean value)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags((EntityPlayerMP) player);
            if ((flags & 0x1) != 0)
            {
                return Enchantment.aquaAffinity.getMaxLevel() > 0;
            }
        }
        return value;
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

    public int getRespiration(Entity player, int value)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags((EntityPlayerMP) player);
            if ((flags & 0x1) != 0)
            {
                return Enchantment.respiration.getMaxLevel();
            }
        }
        return value;
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
                    player.getFoodStats().addStats(1, 1.0F);
                    if (player.isInWater())
                        player.setAir(300);
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
