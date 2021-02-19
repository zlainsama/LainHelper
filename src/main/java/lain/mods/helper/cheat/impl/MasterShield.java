package lain.mods.helper.cheat.impl;

import lain.mods.helper.cheat.Cheat;
import net.minecraft.entity.player.PlayerEntity;

public class MasterShield implements Cheat {

    @Override
    public void onTick(PlayerEntity player) {
        if (player.isAlive() && Master.test(player)) {
            if (player.ticksExisted % 15 == 0) {
                float maxShield = Math.max(10F, player.getMaxHealth());
                float shield = player.getAbsorptionAmount();
                if (shield < maxShield) {
                    if (shield < 0F)
                        shield = 0F;
                    else {
                        shield += Math.max(1F, maxShield * 0.1F);
                        if (shield > maxShield)
                            shield = maxShield;
                    }
                    player.setAbsorptionAmount(shield);
                }
            }
        }
    }

}
