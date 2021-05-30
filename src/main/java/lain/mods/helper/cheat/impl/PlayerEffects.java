package lain.mods.helper.cheat.impl;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

class PlayerEffects {

    static void applyEffect(PlayerEntity player, Effect effect, int amplifier) {
        EffectInstance currentEffect = player.getEffect(effect);
        if (currentEffect == null || currentEffect.getAmplifier() < amplifier || currentEffect.getDuration() <= 400)
            player.addEffect(new EffectInstance(effect, 600, amplifier, true, false));
    }

    static void applyModifier(PlayerEntity player, Attribute attribute, AttributeModifier modifier) {
        ModifiableAttributeInstance attr = player.getAttribute(attribute);
        if (attr != null) {
            AttributeModifier currentModifier = attr.getModifier(modifier.getId());
            if (!isModifierEqual(modifier, currentModifier)) {
                float oldMaxHealth = player.getMaxHealth();
                if (currentModifier != null)
                    attr.removeModifier(currentModifier);
                attr.addPermanentModifier(modifier);
                if (Attributes.MAX_HEALTH.equals(attribute))
                    player.setHealth(player.getHealth() * player.getMaxHealth() / oldMaxHealth);
            }
        }
    }

    private static boolean isModifierEqual(AttributeModifier a, AttributeModifier b) {
        if (a == b)
            return true;
        return a != null && a.equals(b) && a.getOperation() == b.getOperation() && a.getAmount() == b.getAmount();
    }

}
