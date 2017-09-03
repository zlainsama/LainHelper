package lain.mods.helper.cheat;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lain.mods.helper.LainHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;

public class CheatClient extends Cheat
{

    int c_flags = 0;

    static
    {
        MinecraftForge.EVENT_BUS.register(new CheatClientEventHandler());
    }

    @Override
    public int getFlagsClient()
    {
        if (c_flags < 0)
        {
            LainHelper.network.sendToServer(new PacketCheatInfo(c_flags));
            c_flags = 0;
        }
        return c_flags;
    }

    @Override
    public void onLivingUpdate(EntityPlayer player)
    {
        super.onLivingUpdate(player);

        if (player == FMLClientHandler.instance().getClientPlayerEntity())
        {
            int flags = getFlagsClient();
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

    @Override
    public void setFlagsClient(int flags)
    {
        c_flags = flags;
    }

}
