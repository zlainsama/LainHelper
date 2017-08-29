package lain.mods.helper.cheat;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
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
    public void handleEvent(RenderGameOverlayEvent.Pre event)
    {
        int flags = Cheat.INSTANCE.getFlagsClient();
        if ((flags & 0x1) != 0)
        {
            switch (event.getType())
            {
                case FOOD:
                case AIR:
                    event.setCanceled(true);
                    break;
                default:
                    break;
            }
        }
    }

}
