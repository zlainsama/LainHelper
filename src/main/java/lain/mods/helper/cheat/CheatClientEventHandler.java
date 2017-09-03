package lain.mods.helper.cheat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class CheatClientEventHandler
{

    @SubscribeEvent
    public void handleEvent(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        Cheat.INSTANCE.setFlagsClient(-1);
    }

    @SubscribeEvent
    public void handleEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        Cheat.INSTANCE.setFlagsClient(0);
    }

}
