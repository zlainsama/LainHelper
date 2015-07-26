package lain.mods.helper.cheat;

import lain.mods.helper.LainHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.client.FMLClientHandler;

public class CheatClient extends Cheat
{

    int c_flags = 0;

    public boolean getAquaAffinityModifier(EntityLivingBase player, boolean value)
    {
        if (player == FMLClientHandler.instance().getClientPlayerEntity())
        {
            int flags = getFlagsClient();
            if ((flags & 0x1) != 0)
            {
                return Enchantment.aquaAffinity.getMaxLevel() > 0;
            }
        }
        return super.getAquaAffinityModifier(player, value);
    }

    public int getDepthStriderModifier(Entity player, int value)
    {
        if (player == FMLClientHandler.instance().getClientPlayerEntity())
        {
            int flags = getFlagsClient();
            if ((flags & 0x1) != 0)
            {
                return Enchantment.depthStrider.getMaxLevel();
            }
        }
        return super.getDepthStriderModifier(player, value);
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

    public int getRespiration(Entity player, int value)
    {
        if (player == FMLClientHandler.instance().getClientPlayerEntity())
        {
            int flags = getFlagsClient();
            if ((flags & 0x1) != 0)
            {
                return Enchantment.respiration.getMaxLevel();
            }
        }
        return super.getRespiration(player, value);
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
                    {
                        food.addStats(-food.getFoodLevel(), 0.0F);
                        food.addStats(10, 20.0F);
                        food.addStats(8, 0.0F);
                    }
                    player.setAir(300);
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
