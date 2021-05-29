package lain.mods.helper.cheat.impl;

import lain.mods.helper.cheat.Cheat;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.ModList;
import vazkii.botania.api.item.IRelic;

public class MasterAura implements Cheat {

    private static final boolean hasBotania = ModList.get().isLoaded("botania");

    @Override
    public void onTick(PlayerEntity player) {
        if (player.isAlive() && Master.test(player)) {
            if (player.tickCount % 5 == 0) {
                Inv.stream(player).forEach(item -> {
                    if (Enchantments.MENDING.canEnchant(item) && item.isDamaged()) {
                        int damage = MathHelper.clamp(item.getDamageValue(), 0, Integer.MAX_VALUE);
                        if (damage > 0) {
                            damage -= Math.min(Math.max(MathHelper.floor(damage * 0.2F), 4), damage);
                            item.setDamageValue(damage);
                        }
                    }

                    if (hasBotania) {
                        try {
                            if (item.getItem() instanceof IRelic) {
                                IRelic relic = (IRelic) item.getItem();
                                if (player.getUUID().equals((relic.getSoulbindUUID(item)))) {
                                    CompoundNBT tags = item.getOrCreateTag();
                                    if (!tags.getBoolean("Botania_keepIvy"))
                                        tags.putBoolean("Botania_keepIvy", true);
                                }
                            }
                        } catch (Throwable ignored) {
                        }
                    }
                });
            }
        }
    }

}
