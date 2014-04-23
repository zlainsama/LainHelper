package lain.mods.helper.handlers;

import lain.mods.helper.note.NOTE;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class PlayerTickHandler
{

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        switch (event.phase)
        {
            case START:
                if (event.player instanceof EntityPlayerMP)
                    NOTE.get(event.player).applySpecialAttributes(event.player);
                break;
            case END:
                break;
        }
    }

}
