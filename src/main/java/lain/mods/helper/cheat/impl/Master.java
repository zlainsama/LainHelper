package lain.mods.helper.cheat.impl;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Set;
import java.util.UUID;

class Master {

    private static final Set<UUID> _UUID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("75a72085-414f-4ab4-b6af-4d933ca37c18"));

    static boolean test(Object obj) {
        if (obj instanceof PlayerEntity)
            return _UUID.contains(((PlayerEntity) obj).getUUID());
        if (obj instanceof UUID)
            return _UUID.contains(obj);
        return false;
    }

}
