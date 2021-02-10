package lain.mods.helper.cheat.impl;

import lain.mods.helper.cheat.Cheat;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.ModList;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.api.mana.IManaItem;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MasterAura implements Cheat {

    private static final boolean hasBotania = ModList.get().isLoaded("botania");

    @Override
    public void onTick(PlayerEntity player) {
        if (player.isAlive() && Master.test(player)) {
            player.getActivePotionEffects().stream().map(EffectInstance::getPotion).filter(potion -> potion.getEffectType() == EffectType.HARMFUL).collect(Collectors.toSet()).forEach(player::removePotionEffect);

            if (player.ticksExisted % 5 == 0) {
                Set<ItemStack> heldItems = StreamSupport.stream(player.getHeldEquipment().spliterator(), false).filter(item -> !item.isEmpty()).collect(Collectors.toSet());
                Inv.stream(player).filter(item -> !item.isEmpty() && !heldItems.contains(item)).forEach(item -> {
                    if (Enchantments.MENDING.canApply(item) && item.isDamaged()) {
                        int damage = MathHelper.clamp(item.getDamage(), 0, Integer.MAX_VALUE);
                        if (damage > 0) {
                            damage -= Math.min(Math.max(MathHelper.floor(damage * 0.2F), 4), damage);
                            item.setDamage(damage);
                        }
                    }

                    item.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(cap -> {
                        if (cap.canReceive()) {
                            int energy = MathHelper.clamp(cap.getEnergyStored(), 0, Integer.MAX_VALUE);
                            int maxEnergy = MathHelper.clamp(cap.getMaxEnergyStored(), 0, Integer.MAX_VALUE);
                            int diff = maxEnergy - energy;
                            if (diff > 0)
                                cap.receiveEnergy(Math.min(Math.max(MathHelper.floor(diff * 0.2F), 4000), diff), false);
                        }
                    });

                    if (hasBotania) {
                        try {
                            if (item.getItem() instanceof IManaItem) {
                                IManaItem manaItem = (IManaItem) item.getItem();
                                if (manaItem.canReceiveManaFromItem(item, item)) {
                                    int mana = MathHelper.clamp(manaItem.getMana(item), 0, Integer.MAX_VALUE);
                                    int maxMana = MathHelper.clamp(manaItem.getMaxMana(item), 0, Integer.MAX_VALUE);
                                    int diff = maxMana - mana;
                                    if (diff > 0)
                                        manaItem.addMana(item, Math.min(Math.max(MathHelper.floor(diff * 0.2F), 4), diff));
                                }
                            }
                            if (item.getItem() instanceof IRelic) {
                                IRelic relic = (IRelic) item.getItem();
                                if (player.getUniqueID().equals((relic.getSoulbindUUID(item)))) {
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
