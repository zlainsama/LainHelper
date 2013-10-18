package lain.mods.helper;

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
import net.minecraft.potion.Potion;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "LainHelper", name = "LainHelper", version = "1.6.x-v4")
public class LainHelper implements ITickHandler
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

    @Override
    public String getLabel()
    {
        return "LainHelper";
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
        TickRegistry.registerTickHandler(this, Side.SERVER);

        GameRegistry.registerBlock(blockWaterCube, "blockWaterCube");
        GameRegistry.registerTileEntity(TileWaterCube.class, "tileWaterCube");
        GameRegistry.registerBlock(blockCobbleCube, "blockCobbleCube");
        GameRegistry.registerTileEntity(TileCobbleCube.class, "tileCobbleCube");

        if (event.getSide().isClient())
        {
            OfflineSkin.setup();
            TooltipTweaker.setup();
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
            else if (event.source.isMagicDamage())
                event.setCanceled(true);
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        if (type.contains(TickType.PLAYER))
        {
            EntityPlayer plr = (EntityPlayer) tickData[0];
            if (checkOwner(plr))
                if (plr.isEntityAlive())
                {
                    if (plr.ticksExisted % 20 == 10)
                        for (int i = 0; i < 5; i++)
                        {
                            ItemStack item = plr.getCurrentItemOrArmor(i);
                            if (item != null && item.getItem().isRepairable() && item.isItemDamaged())
                                item.setItemDamage(item.getItemDamage() - 1);
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
        if (type.contains(TickType.PLAYER))
        {
            EntityPlayer plr = (EntityPlayer) tickData[0];
            if (checkOwner(plr))
                if (plr.isEntityAlive())
                {
                    if (plr.isPotionActive(Potion.hunger.getId()))
                        plr.removePotionEffect(Potion.hunger.getId());
                    if (plr.isPotionActive(Potion.poison.getId()))
                        plr.removePotionEffect(Potion.poison.getId());
                }
        }
    }

}
