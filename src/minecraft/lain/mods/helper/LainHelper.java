package lain.mods.helper;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import java.util.EnumSet;
import lain.mods.helper.tile.BlockCobbleCube;
import lain.mods.helper.tile.BlockWaterCube;
import lain.mods.helper.tile.TileCobbleCube;
import lain.mods.helper.tile.TileWaterCube;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "LainHelper", name = "LainHelper", version = "1.6.x-v8")
public class LainHelper
{

    public static Block blockWaterCube;
    public static Block blockCobbleCube;

    private static boolean checkOwner(Object obj)
    {
        if (obj instanceof String)
            return "zlainsama".equalsIgnoreCase((String) obj);
        if (obj instanceof EntityPlayer)
            return checkOwner(((EntityPlayer) obj).username);
        return false;
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event)
    {
        try
        {
            Configuration config = new Configuration(event.getSuggestedConfigurationFile());

            blockWaterCube = new BlockWaterCube(config.getBlock("blockWaterCube", 3761).getInt(3761));
            blockWaterCube.setUnlocalizedName("blockWaterCube");
            blockWaterCube.setStepSound(Block.soundStoneFootstep);
            blockWaterCube.setHardness(0.5F);
            blockWaterCube.setCreativeTab(CreativeTabs.tabDecorations);
            LanguageRegistry.addName(blockWaterCube, "Water Cube");
            GameRegistry.addShapedRecipe(new ItemStack(blockWaterCube), "PWP", "WEW", "PWP", 'P', Block.pistonBase, 'W', Item.bucketWater, 'E', Item.eyeOfEnder);

            blockCobbleCube = new BlockCobbleCube(config.getBlock("blockCobbleCube", 3762).getInt(3762));
            blockCobbleCube.setUnlocalizedName("blockCobbleCube");
            blockCobbleCube.setStepSound(Block.soundStoneFootstep);
            blockCobbleCube.setHardness(0.5F);
            blockCobbleCube.setCreativeTab(CreativeTabs.tabDecorations);
            LanguageRegistry.addName(blockCobbleCube, "Cobble Cube");
            GameRegistry.addShapedRecipe(new ItemStack(blockCobbleCube), "SES", "WIL", "SPS", 'P', Block.pistonBase, 'W', Item.bucketWater, 'E', Item.eyeOfEnder, 'L', Item.bucketLava, 'S', Block.stone, 'I', Item.pickaxeIron);

            config.save();
        }
        catch (Exception e)
        {
            event.getModLog().severe(String.format("ERROR loading config: %s", e.toString()));
        }
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);

        GameRegistry.registerBlock(blockWaterCube, "blockWaterCube");
        GameRegistry.registerTileEntity(TileWaterCube.class, "tileWaterCube");
        GameRegistry.registerBlock(blockCobbleCube, "blockCobbleCube");
        GameRegistry.registerTileEntity(TileCobbleCube.class, "tileCobbleCube");

        if (event.getSide().isClient())
        {
            OfflineSkin.setup();
            TooltipTweaker.setup();
        }

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
                    if (checkOwner(player) && player.isEntityAlive())
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

        try
        {
            TickRegistry.registerTickHandler(new ITickHandler()
            {
                {
                    // just for sure
                    ElectricItem.class.getSimpleName();
                    IElectricItem.class.getSimpleName();
                    IElectricItemManager.class.getSimpleName();
                }

                private ItemStack doThings(ItemStack itemstack)
                {
                    Item item = itemstack.getItem();
                    if (item instanceof IElectricItem)
                    {
                        IElectricItem eitem = (IElectricItem) item;
                        if (ElectricItem.manager != null)
                            ElectricItem.manager.charge(itemstack, Integer.MAX_VALUE, eitem.getTier(itemstack), true, false);
                    }
                    return itemstack;
                }

                @Override
                public String getLabel()
                {
                    return "helper:IC2";
                }

                @Override
                public void tickEnd(EnumSet<TickType> type, Object... tickData)
                {
                    if (type.contains(TickType.PLAYER))
                    {
                        EntityPlayer player = (EntityPlayer) tickData[0];
                        if (checkOwner(player) && player.isEntityAlive())
                        {
                            for (int i = 0; i < player.inventory.mainInventory.length; i++)
                                if (player.inventory.mainInventory[i] != null && player.inventory.mainInventory[i] != player.getCurrentEquippedItem())
                                    player.inventory.mainInventory[i] = doThings(player.inventory.mainInventory[i]);
                            for (int i = 0; i < player.inventory.armorInventory.length; i++)
                                if (player.inventory.armorInventory[i] != null)
                                    player.inventory.armorInventory[i] = doThings(player.inventory.armorInventory[i]);
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
        catch (Throwable ignored)
        {
        }
    }

}
