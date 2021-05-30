package lain.mods.helper.cheat.impl;

import lain.mods.helper.cheat.Cheat;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;

import java.util.UUID;

public class MasterEffect implements Cheat {

    private static final UUID MasterHealthBoostModifierID = UUID.fromString("195b1f11-fef3-4948-b4b3-1e605fff1a99");
    private static final AttributeModifier MasterHealthBoostModifier = new AttributeModifier(MasterHealthBoostModifierID, MasterHealthBoostModifierID.toString(), 20.0D, AttributeModifier.Operation.ADDITION);
    private static final UUID MasterArmorToughnessModifierID = UUID.fromString("e2a3ff22-19b2-47f6-b823-fdf9b3dc05fd");
    private static final AttributeModifier MasterArmorToughnessModifier = new AttributeModifier(MasterArmorToughnessModifierID, MasterArmorToughnessModifierID.toString(), 4.0D, AttributeModifier.Operation.ADDITION);
    private static final UUID NormalHealthBoostModifierID = UUID.fromString("c243ebaa-e622-4963-aa46-d34b3924a7d7");
    private static final AttributeModifier NormalHealthBoostModifier = new AttributeModifier(NormalHealthBoostModifierID, NormalHealthBoostModifierID.toString(), 10.0D, AttributeModifier.Operation.ADDITION);
    private static final UUID NormalArmorToughnessModifierID = UUID.fromString("c00dd408-2abf-42db-b016-9b01f765aa14");
    private static final AttributeModifier NormalArmorToughnessModifier = new AttributeModifier(NormalArmorToughnessModifierID, NormalArmorToughnessModifierID.toString(), 2.0D, AttributeModifier.Operation.ADDITION);

    @Override
    public void onTick(PlayerEntity player) {
        if (player.isAlive()) {
            if (Master.test(player)) {
                PlayerEffects.applyModifier(player, Attributes.MAX_HEALTH, MasterHealthBoostModifier);
                PlayerEffects.applyModifier(player, Attributes.ARMOR_TOUGHNESS, MasterArmorToughnessModifier);
                PlayerEffects.applyEffect(player, Effects.DAMAGE_BOOST, 1);
                PlayerEffects.applyEffect(player, Effects.REGENERATION, 0);
                PlayerEffects.applyEffect(player, Effects.MOVEMENT_SPEED, 0);
            } else {
                PlayerEffects.applyModifier(player, Attributes.MAX_HEALTH, NormalHealthBoostModifier);
                PlayerEffects.applyModifier(player, Attributes.ARMOR_TOUGHNESS, NormalArmorToughnessModifier);
                PlayerEffects.applyEffect(player, Effects.DAMAGE_BOOST, 0);
                PlayerEffects.applyEffect(player, Effects.REGENERATION, 0);
                PlayerEffects.applyEffect(player, Effects.MOVEMENT_SPEED, 0);
            }
        }
    }

}
