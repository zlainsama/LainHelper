package lain.mods.helper.survivalists;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class Survivalists
{

    public static Item cooked_seeds = new ItemFood(1, 0.2F, false).setUnlocalizedName("seeds_cooked").setTextureName("helper:seeds_cooked");

    public static void setEnabled()
    {
        GameRegistry.registerItem(cooked_seeds, cooked_seeds.getUnlocalizedName());
        GameRegistry.addSmelting(Items.wheat_seeds, new ItemStack(cooked_seeds), 0.0F);
    }

}
