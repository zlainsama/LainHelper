package lain.mods.helper.cheat;

import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CheatEvents
{

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event)
    {
        if (event.getSource().getTrueSource() != null && event.getSource().getTrueSource().hasCapability(CheatCaps.CAPABILITY_CHEAT, null))
            event.getSource().getTrueSource().getCapability(CheatCaps.CAPABILITY_CHEAT, null).forEach(cheat -> event.setAmount(cheat.modifiyDamage(event.getSource().getTrueSource(), event.getSource(), event.getAmount(), true)));
        if (event.getEntity().hasCapability(CheatCaps.CAPABILITY_CHEAT, null))
            event.getEntity().getCapability(CheatCaps.CAPABILITY_CHEAT, null).forEach(cheat -> event.setAmount(cheat.modifiyDamage(event.getEntity(), event.getSource(), event.getAmount(), false)));
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END && !event.player.world.isRemote && event.player.hasCapability(CheatCaps.CAPABILITY_CHEAT, null))
            event.player.getCapability(CheatCaps.CAPABILITY_CHEAT, null).forEach(cheat -> cheat.tick(event.player));
    }

}
