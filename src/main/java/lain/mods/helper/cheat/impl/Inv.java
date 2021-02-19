package lain.mods.helper.cheat.impl;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class Inv {

    private static final boolean hasCurios = ModList.get().isLoaded("curios");

    static Stream<ItemStack> stream(PlayerEntity player) {
        ItemStack heldItem = player.inventory.getCurrentItem();
        Stream<ItemStack> result = Stream.concat(
                StreamSupport.stream(player.getEquipmentAndArmor().spliterator(), false).filter(stack -> !stack.isEmpty()),
                player.inventory.mainInventory.subList(0, 9).stream().filter(stack -> !stack.isEmpty() && (heldItem.isEmpty() || heldItem != stack))
        );

        if (hasCurios) {
            try {
                List<ItemStack> curios = Lists.newArrayList();
                CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> handler.getCurios().values().stream().map(ICurioStacksHandler::getStacks).forEach(stacks -> IntStream.range(0, stacks.getSlots()).mapToObj(stacks::getStackInSlot).forEach(curios::add)));
                result = Stream.concat(result, curios.stream().filter(stack -> !stack.isEmpty()));
            } catch (Throwable ignored) {
            }
        }

        return result;
    }

}
