package lain.mods.helper;

import lain.mods.helper.cheat.Cheat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

enum Proxy {

    INSTANCE;

    void init() {
        MinecraftForge.EVENT_BUS.addListener(this::onTick);
    }

    private void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level.isClientSide) {
            Cheat.cheats.forEach(cheat -> cheat.onTick(event.player));
        }
    }

}
