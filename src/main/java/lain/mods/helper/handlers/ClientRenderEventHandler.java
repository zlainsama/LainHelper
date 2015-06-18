package lain.mods.helper.handlers;

import lain.mods.helper.cheat.Cheat;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientRenderEventHandler
{

    @SubscribeEvent
    public void handleEvent(RenderGameOverlayEvent.Pre event)
    {
        if (event.type == RenderGameOverlayEvent.ElementType.FOOD)
        {
            int flags = Cheat.INSTANCE.getFlagsClient();
            if ((flags & 0x1) != 0)
                event.setCanceled(true);
        }
    }

}
