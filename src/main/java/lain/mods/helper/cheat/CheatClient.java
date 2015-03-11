package lain.mods.helper.cheat;

import lain.mods.helper.LainHelper;
import lain.mods.helper.utils.ItemCharger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.client.FMLClientHandler;

public class CheatClient extends Cheat
{

    int c_flags = 0;

    @Override
    public int getFlagsClient()
    {
        return c_flags;
    }

    @Override
    public void onLivingUpdate(EntityPlayer player)
    {
        super.onLivingUpdate(player);

        if (player == FMLClientHandler.instance().getClientPlayerEntity())
        {
            int flags = getFlagsClient();
            if (flags < 0)
            {
                LainHelper.network.sendToServer(new PacketCheatInfo(flags));
                setFlagsClient(flags = 0);
            }
            if ((flags & 0x1) != 0)
            {
                for (int i = 0; i < 5; i++)
                {
                    ItemStack stack = player.getEquipmentInSlot(i);
                    if (stack == null)
                        continue;
                    for (ItemCharger charger : ItemCharger.chargers)
                    {
                        if (charger.canHandle(stack))
                            charger.chargeItem(stack, Double.MAX_VALUE, true, false);
                    }
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
