package lain.mods.helper.cheat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CheatEvents
{

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END && !event.player.world.isRemote && event.player.hasCapability(CheatCaps.CAPABILITY_CHEAT, null))
            event.player.getCapability(CheatCaps.CAPABILITY_CHEAT, null).forEach(cheat -> cheat.tick(event.player));
    }

}
