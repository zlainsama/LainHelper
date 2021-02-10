package lain.mods.helper.cheat;

import com.google.common.collect.ImmutableList;
import lain.mods.helper.cheat.impl.MasterAura;
import lain.mods.helper.cheat.impl.MasterShield;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public interface Cheat {

    List<Cheat> cheats = ImmutableList.of(
            new MasterShield(),
            new MasterAura()
    );

    void onTick(PlayerEntity player);

}
