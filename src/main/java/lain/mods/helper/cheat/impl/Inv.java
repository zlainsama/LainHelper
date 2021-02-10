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

class Inv {

    private static final boolean hasCurios = ModList.get().isLoaded("curios");

    static Stream<ItemStack> stream(PlayerEntity player) {
        Stream<ItemStack> result = IntStream.range(0, player.inventory.getSizeInventory()).mapToObj(player.inventory::getStackInSlot);

        if (hasCurios) {
            try {
                List<ItemStack> curios = Lists.newArrayList();
                CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> handler.getCurios().values().stream().map(ICurioStacksHandler::getStacks).forEach(stacks -> IntStream.range(0, stacks.getSlots()).mapToObj(stacks::getStackInSlot).forEach(curios::add)));
                result = Stream.concat(result, curios.stream());
            } catch (Throwable ignored) {
            }
        }

        return result;
    }

}
