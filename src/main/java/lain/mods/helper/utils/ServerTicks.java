package lain.mods.helper.utils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ServerTicks
{

    public static class RunOnceExecutor
    {

        private final Runnable runnable;

        private RunOnceExecutor(Runnable runnable)
        {
            this.runnable = runnable;
            FMLCommonHandler.instance().bus().register(this);
        }

        @SubscribeEvent
        public void handleEvent(TickEvent.ServerTickEvent event)
        {
            if (event.phase == TickEvent.Phase.START)
            {
                runnable.run();
                FMLCommonHandler.instance().bus().unregister(this);
            }
        }

    }

    public static void RunOnce(Runnable runnable)
    {
        new RunOnceExecutor(runnable);
    }

}
