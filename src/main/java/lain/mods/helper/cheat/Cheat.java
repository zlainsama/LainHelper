package lain.mods.helper.cheat;

import java.util.Set;
import java.util.UUID;
import lain.mods.helper.network.NetworkManager;
import lain.mods.helper.utils.ItemCharger;
import lain.mods.helper.utils.ItemRepairer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.common.FMLCommonHandler;

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
        NetworkManager.registerPacket(PacketCheatInfo.class);
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

    public void onLivingUpdate(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags(player);
            if ((flags & 0x1) != 0)
            {
                if (player.isEntityAlive())
                {
                    for (int i = 0; i < 5; i++)
                    {
                        ItemStack stack = player.getEquipmentInSlot(i);
                        if (stack == null)
                            continue;
                        boolean handled = false;
                        for (ItemCharger charger : ItemCharger.chargers)
                        {
                            if (charger.canHandle(stack))
                            {
                                charger.chargeItem(stack, Double.MAX_VALUE, true, false);
                                handled = true;
                            }
                        }
                        if (!handled)
                        {
                            for (ItemRepairer repairer : ItemRepairer.repairers)
                            {
                                if (repairer.canHandle(stack))
                                {
                                    repairer.repairItem(stack, Double.MAX_VALUE, false);
                                    handled = true;
                                }
                            }
                        }
                    }
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
