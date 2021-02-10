package lain.mods.helper.cheat.impl;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Set;
import java.util.UUID;

class Master {

    private static final Set<UUID> _UUID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));

    static boolean test(Object obj) {
        if (obj instanceof PlayerEntity)
            return _UUID.contains(((PlayerEntity) obj).getUniqueID());
        if (obj instanceof UUID)
            return _UUID.contains(obj);
        return false;
    }

}
