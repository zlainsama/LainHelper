package lain.mods.helper.cheat;

import java.util.stream.Collectors;
import lain.mods.helper.LainHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
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
                    FoodStats food = player.getFoodStats();
                    if (food != null)
                        food.addStats(18 - food.getFoodLevel(), Float.MAX_VALUE);

                    int air = player.getAir();
                    if (air < 100)
                    {
                        air += 200;
                        player.setAir(air);
                    }
                    player.extinguish();

                    if (player.fallDistance > 1.0F)
                        player.fallDistance = 1.0F;

                    player.capabilities.allowFlying = true;

                    player.getActivePotionEffects().stream().filter(p -> p.getPotion().isBadEffect()).map(p -> p.getPotion()).collect(Collectors.toSet()).forEach(player::removePotionEffect);
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
