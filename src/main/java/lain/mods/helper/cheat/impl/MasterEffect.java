package lain.mods.helper.cheat.impl;

import lain.mods.helper.cheat.Cheat;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;

import java.util.UUID;

public class MasterEffect implements Cheat {

    private static final UUID HealthBoostModifierID = UUID.fromString("6283c886-a389-47e8-93d0-9688e492a5fb");
    private static final UUID ArmorToughnessModifierID = UUID.fromString("9a789618-6657-409d-a03f-0e1676444d18");
    private static final UUID ArmorModifierID = UUID.fromString("34662888-411d-48f3-8772-8f2f2dba8db0");

    private static final AttributeModifier MasterHealthBoostModifier = new AttributeModifier(HealthBoostModifierID, HealthBoostModifierID.toString(), 20.0D, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier MasterArmorToughnessModifier = new AttributeModifier(ArmorToughnessModifierID, ArmorToughnessModifierID.toString(), 4.0D, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier MasterArmorModifier = new AttributeModifier(ArmorModifierID, ArmorModifierID.toString(), 12.0D, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier NormalHealthBoostModifier = new AttributeModifier(HealthBoostModifierID, HealthBoostModifierID.toString(), 20.0D, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier NormalArmorToughnessModifier = new AttributeModifier(ArmorToughnessModifierID, ArmorToughnessModifierID.toString(), 4.0D, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier NormalArmorModifier = new AttributeModifier(ArmorModifierID, ArmorModifierID.toString(), 12.0D, AttributeModifier.Operation.ADDITION);

    @Override
    public void onTick(PlayerEntity player) {
        if (player.isAlive()) {
            if (Master.test(player)) {
                PlayerEffects.applyModifier(player, Attributes.MAX_HEALTH, MasterHealthBoostModifier);
                PlayerEffects.applyModifier(player, Attributes.ARMOR_TOUGHNESS, MasterArmorToughnessModifier);
                PlayerEffects.applyModifier(player, Attributes.ARMOR, MasterArmorModifier);
                PlayerEffects.applyEffect(player, Effects.DAMAGE_BOOST, 1);
                PlayerEffects.applyEffect(player, Effects.REGENERATION, 0);
                PlayerEffects.applyEffect(player, Effects.MOVEMENT_SPEED, 0);
                PlayerEffects.applyEffect(player, Effects.DAMAGE_RESISTANCE, 1);
            } else {
                PlayerEffects.applyModifier(player, Attributes.MAX_HEALTH, NormalHealthBoostModifier);
                PlayerEffects.applyModifier(player, Attributes.ARMOR_TOUGHNESS, NormalArmorToughnessModifier);
                PlayerEffects.applyModifier(player, Attributes.ARMOR, NormalArmorModifier);
                PlayerEffects.applyEffect(player, Effects.DAMAGE_BOOST, 0);
                PlayerEffects.applyEffect(player, Effects.REGENERATION, 0);
                PlayerEffects.applyEffect(player, Effects.MOVEMENT_SPEED, 0);
                PlayerEffects.applyEffect(player, Effects.DAMAGE_RESISTANCE, 0);
            }
        }
    }

}
