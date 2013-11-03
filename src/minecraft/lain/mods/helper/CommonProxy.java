package lain.mods.helper;

import java.util.EnumSet;
import lain.mods.helper.tile.BlockCobbleCube;
import lain.mods.helper.tile.BlockWaterCube;
import lain.mods.helper.tile.TileCobbleCube;
import lain.mods.helper.tile.TileWaterCube;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

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

        TickRegistry.registerTickHandler(new ITickHandler()
        {
            @Override
            public String getLabel()
            {
                return "helper";
            }

            @Override
            public void tickEnd(EnumSet<TickType> type, Object... tickData)
            {
                if (type.contains(TickType.PLAYER))
                {
                    EntityPlayer player = (EntityPlayer) tickData[0];
                    if (LainHelper.checkOwner(player) && player.isEntityAlive())
                    {
                        if (player.shouldHeal())
                        {
                            float f = Math.max(0.0F, Math.min(player.getHealth() / player.getMaxHealth(), 1.0F));
                            int t = player.hurtResistantTime;
                            if (player.ticksExisted % 50 == 0)
                                player.heal(1.0F);
                            if (player.ticksExisted % 10 == 0 && f < 0.4F)
                                player.heal(1.0F);
                            player.hurtResistantTime = t;
                        }
                    }
                }
            }

            @Override
            public EnumSet<TickType> ticks()
            {
                return EnumSet.of(TickType.PLAYER);
            }

            @Override
            public void tickStart(EnumSet<TickType> type, Object... tickData)
            {
            }
        }, Side.SERVER);
    }

}
