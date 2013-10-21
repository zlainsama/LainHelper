package lain.mods.helper;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import java.util.EnumSet;
import lain.mods.helper.tile.BlockCobbleCube;
import lain.mods.helper.tile.BlockWaterCube;
import lain.mods.helper.tile.TileCobbleCube;
import lain.mods.helper.tile.TileWaterCube;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "LainHelper", name = "LainHelper", version = "1.6.x-v6")
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

            blockWaterCube = new BlockWaterCube(config.getBlock("blockWaterCube", 3761).getInt(3761), Material.iron);
            blockWaterCube.setUnlocalizedName("blockWaterCube");
            blockWaterCube.setTextureName("helper:blockWaterCube");
            blockWaterCube.setStepSound(Block.soundStoneFootstep);
            blockWaterCube.setHardness(0.5F);
            blockWaterCube.setCreativeTab(CreativeTabs.tabDecorations);
            LanguageRegistry.addName(blockWaterCube, "Water Cube");
            GameRegistry.addShapedRecipe(new ItemStack(blockWaterCube), "PWP", "WEW", "PWP", 'P', Block.pistonBase, 'W', Item.bucketWater, 'E', Item.eyeOfEnder);

            blockCobbleCube = new BlockCobbleCube(config.getBlock("blockCobbleCube", 3762).getInt(3762), Material.iron);
            blockCobbleCube.setUnlocalizedName("blockCobbleCube");
            blockCobbleCube.setTextureName("helper:blockCobbleCube");
            blockCobbleCube.setStepSound(Block.soundStoneFootstep);
            blockCobbleCube.setHardness(0.5F);
            blockCobbleCube.setCreativeTab(CreativeTabs.tabDecorations);
            LanguageRegistry.addName(blockCobbleCube, "Cobble Cube");
            GameRegistry.addShapedRecipe(new ItemStack(blockCobbleCube), "SES", "WNL", "SPS", 'P', Block.pistonBase, 'W', Item.bucketWater, 'E', Item.eyeOfEnder, 'L', Item.bucketLava, 'S', Block.stone);

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

        try
        {
            TickRegistry.registerScheduledTickHandler(new IScheduledTickHandler()
            {
                {
                    // just for sure
                    ElectricItem.class.getSimpleName();
                    IElectricItem.class.getSimpleName();
                    IElectricItemManager.class.getSimpleName();
                    ISpecialElectricItem.class.getSimpleName();
                }

                private ItemStack doThings(ItemStack itemstack)
                {
                    Item item = itemstack.getItem();
                    if (item instanceof IElectricItem)
                    {
                        IElectricItem eitem = (IElectricItem) item;
                        IElectricItemManager eitemman = (eitem instanceof ISpecialElectricItem) ? ((ISpecialElectricItem) eitem).getManager(itemstack) : ElectricItem.manager;
                        if (eitemman != null)
                            eitemman.charge(itemstack, Integer.MAX_VALUE, eitem.getTier(itemstack), false, false);
                    }
                    return itemstack;
                }

                @Override
                public String getLabel()
                {
                    return "Helper:IC2";
                }

                @Override
                public int nextTickSpacing()
                {
                    return 20;
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

    @ForgeSubscribe
    public void onDamage(LivingHurtEvent event)
    {
        if (checkOwner(event.entity))
            if (!event.source.isUnblockable())
                event.ammount *= 0.8;
            else if ("fall".equalsIgnoreCase(event.source.getDamageType()))
                event.ammount *= 0.5;
    }

    @ForgeSubscribe
    public void preDamage(LivingAttackEvent event)
    {
        if (checkOwner(event.entity))
        {
            String type = event.source.getDamageType();
            if ("starve".equalsIgnoreCase(type) || "drown".equalsIgnoreCase(type))
                event.setCanceled(true);
            else if ("wither".equalsIgnoreCase(type) || "electricity".equalsIgnoreCase(type) || "radiation".equalsIgnoreCase(type))
                event.setCanceled(true);
            else if (event.source.isMagicDamage() || event.source.isFireDamage())
                event.setCanceled(true);
        }
    }

}
