package lain.mods.helper.cheat.impl;

import lain.mods.helper.cheat.Cheat;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import java.util.UUID;

public class MasterEffect implements Cheat {

    private static final UUID HealthBoostModifierID = UUID.fromString("195b1f11-fef3-4948-b4b3-1e605fff1a99");
    private static final AttributeModifier HealthBoostModifier = new AttributeModifier(HealthBoostModifierID, HealthBoostModifierID.toString(), 10.0D, AttributeModifier.Operation.ADDITION);
    private static final UUID ArmorToughnessModifierID = UUID.fromString("e2a3ff22-19b2-47f6-b823-fdf9b3dc05fd");
    private static final AttributeModifier ArmorToughnessModifier = new AttributeModifier(ArmorToughnessModifierID, ArmorToughnessModifierID.toString(), 2.0D, AttributeModifier.Operation.ADDITION);

    private static void applyEffect(PlayerEntity player, Effect effect, int amplifier) {
        EffectInstance currentEffect = player.getEffect(effect);
        if (currentEffect == null || currentEffect.getAmplifier() < amplifier || currentEffect.getDuration() <= 200)
            player.addEffect(new EffectInstance(effect, 300, amplifier, false, false));
    }

    private static void applyModifier(PlayerEntity player, Attribute attribute, AttributeModifier modifier) {
        ModifiableAttributeInstance attr = player.getAttribute(attribute);
        if (attr != null) {
            AttributeModifier currentModifier = attr.getModifier(modifier.getId());
            if (!modifier.equals(currentModifier)) {
                float oldMaxHealth = player.getMaxHealth();
                if (currentModifier != null)
                    attr.removeModifier(currentModifier);
                attr.addPermanentModifier(modifier);
                if (Attributes.MAX_HEALTH.equals(attribute))
                    player.setHealth(player.getHealth() * player.getMaxHealth() / oldMaxHealth);
            }
        }
    }

    @Override
    public void onTick(PlayerEntity player) {
        if (player.isAlive() && Master.test(player)) {
            applyModifier(player, Attributes.MAX_HEALTH, HealthBoostModifier);
            applyModifier(player, Attributes.ARMOR_TOUGHNESS, ArmorToughnessModifier);
            applyEffect(player, Effects.DAMAGE_BOOST, 0);
            applyEffect(player, Effects.REGENERATION, 0);
            applyEffect(player, Effects.MOVEMENT_SPEED, 0);
        }
    }

}
