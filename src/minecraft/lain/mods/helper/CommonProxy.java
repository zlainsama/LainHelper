package lain.mods.helper;

import lain.mods.helper.tile.BlockCobbleCube;
import lain.mods.helper.tile.BlockWaterCube;
import lain.mods.helper.tile.TileCobbleCube;
import lain.mods.helper.tile.TileWaterCube;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CommonProxy
{

    public void load(FMLInitializationEvent event)
    {
        LainHelper.blockWaterCube = new BlockWaterCube(LainHelper.idBlockWaterCube);
        LainHelper.blockWaterCube.setUnlocalizedName("blockWaterCube");
        LanguageRegistry.addName(LainHelper.blockWaterCube, "Water Cube");
        GameRegistry.addShapedRecipe(new ItemStack(LainHelper.blockWaterCube), "PWP", "WEW", "PWP", 'P', Block.pistonBase, 'W', Item.bucketWater, 'E', Item.eyeOfEnder);

        LainHelper.blockCobbleCube = new BlockCobbleCube(LainHelper.idBlockCobbleCube);
        LainHelper.blockCobbleCube.setUnlocalizedName("blockCobbleCube");
        LanguageRegistry.addName(LainHelper.blockCobbleCube, "Cobble Cube");
        GameRegistry.addShapedRecipe(new ItemStack(LainHelper.blockCobbleCube), "SES", "WIL", "SPS", 'P', Block.pistonBase, 'W', Item.bucketWater, 'E', Item.eyeOfEnder, 'L', Item.bucketLava, 'S', Block.stone, 'I', Item.pickaxeIron);

        GameRegistry.registerBlock(LainHelper.blockWaterCube, "blockWaterCube");
        GameRegistry.registerTileEntity(TileWaterCube.class, "tileWaterCube");
        GameRegistry.registerBlock(LainHelper.blockCobbleCube, "blockCobbleCube");
        GameRegistry.registerTileEntity(TileCobbleCube.class, "tileCobbleCube");
    }

}
