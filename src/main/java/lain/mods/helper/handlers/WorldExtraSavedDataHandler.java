package lain.mods.helper.handlers;

import lain.mods.helper.SharedStorage;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class WorldExtraSavedDataHandler
{

    @SubscribeEvent
    public void handleEvent(WorldEvent.Save event)
    {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0)
        {
            if (SharedStorage.storage != null)
                SharedStorage.storage.save();
        }
    }

}
