package lain.mods.helper.cheat;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CheatClientRenderHooks
{

    @SubscribeEvent
    public void handleEvent(RenderGameOverlayEvent.Pre event)
    {
        int flags = Cheat.INSTANCE.getFlagsClient();
        if ((flags & 0x1) != 0)
        {
            switch (event.type)
            {
                case FOOD:
                    event.setCanceled(true);
                    break;
                case AIR:
                    if (FMLClientHandler.instance().getClientPlayerEntity().isInWater())
                        event.setCanceled(true);
                    break;
                default:
                    break;
            }
        }
    }

}
