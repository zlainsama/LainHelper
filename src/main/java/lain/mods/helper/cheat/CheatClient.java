package lain.mods.helper.cheat;

import lain.mods.helper.LainHelper;
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
                    player.capabilities.allowFlying = true;
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
