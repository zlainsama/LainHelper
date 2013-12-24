package lain.mods.helper;

import lain.mods.helper.command.CommandBack;
import lain.mods.helper.command.CommandHome;
import lain.mods.helper.command.CommandSetHome;
import lain.mods.helper.command.CommandSpawn;
import lain.mods.helper.tile.BlockCobbleCube;
import lain.mods.helper.tile.BlockWaterCube;
import lain.mods.helper.tile.TileCobbleCube;
import lain.mods.helper.tile.TileWaterCube;
import lain.mods.helper.util.DataStorage;
import lain.mods.helper.util.PositionData;
import lain.mods.helper.util.SimpleLanguageFileLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CommonProxy
{

    private DataStorage playerData;

    public NBTTagCompound getCustomPlayerData(EntityPlayer player)
    {
        return playerData.load("player." + player.username);
    }

    public PositionData getPlayerHomePosition(EntityPlayer player)
    {
        NBTTagCompound data = getCustomPlayerData(player);
        if (data.hasKey("helper"))
        {
            NBTTagCompound dir = data.getCompoundTag("helper");
            if (dir.hasKey("homePosition"))
            {
                PositionData result = new PositionData();
                result.readFromNBT(dir.getCompoundTag("homePosition"));
                return result;
            }
        }
        return null;
    }

    public PositionData getPlayerLastPosition(EntityPlayer player)
    {
        NBTTagCompound data = getCustomPlayerData(player);
        if (data.hasKey("helper"))
        {
            NBTTagCompound dir = data.getCompoundTag("helper");
            if (dir.hasKey("lastPosition"))
            {
                PositionData result = new PositionData();
                result.readFromNBT(dir.getCompoundTag("lastPosition"));
                return result;
            }
        }
        return null;
    }

    public void load(FMLInitializationEvent event)
    {
        playerData = new DataStorage("helper")
        {
            @Override
            protected void handleException(Exception e)
            {
                throw new RuntimeException(e);
            }
        };
        GameRegistry.registerPlayerTracker(new IPlayerTracker()
        {
            @Override
            public void onPlayerChangedDimension(EntityPlayer player)
            {
            }

            @Override
            public void onPlayerLogin(EntityPlayer player)
            {
                playerData.load("player." + player.username);
            }

            @Override
            public void onPlayerLogout(EntityPlayer player)
            {
                playerData.unload("player." + player.username);
            }

            @Override
            public void onPlayerRespawn(EntityPlayer player)
            {
            }
        });

        MinecraftForge.EVENT_BUS.register(this);

        if (LainHelper.idBlockWaterCube != 0)
        {
            LainHelper.blockWaterCube = new BlockWaterCube(LainHelper.idBlockWaterCube);
            LainHelper.blockWaterCube.setUnlocalizedName("blockWaterCube");
            LanguageRegistry.addName(LainHelper.blockWaterCube, "Water Cube");
            GameRegistry.addShapedRecipe(new ItemStack(LainHelper.blockWaterCube), "PWP", "WEW", "PWP", 'P', Block.pistonBase, 'W', Item.bucketWater, 'E', Item.eyeOfEnder);
            GameRegistry.registerBlock(LainHelper.blockWaterCube, "blockWaterCube");
            GameRegistry.registerTileEntity(TileWaterCube.class, "tileWaterCube");
        }

        if (LainHelper.idBlockCobbleCube != 0)
        {
            LainHelper.blockCobbleCube = new BlockCobbleCube(LainHelper.idBlockCobbleCube);
            LainHelper.blockCobbleCube.setUnlocalizedName("blockCobbleCube");
            LanguageRegistry.addName(LainHelper.blockCobbleCube, "Cobble Cube");
            GameRegistry.addShapedRecipe(new ItemStack(LainHelper.blockCobbleCube), "SES", "WIL", "SPS", 'P', Block.pistonBase, 'W', Item.bucketWater, 'E', Item.eyeOfEnder, 'L', Item.bucketLava, 'S', Block.stone, 'I', Item.pickaxeIron);
            GameRegistry.registerBlock(LainHelper.blockCobbleCube, "blockCobbleCube");
            GameRegistry.registerTileEntity(TileCobbleCube.class, "tileCobbleCube");
        }

        Vampirism.setup();

        SimpleLanguageFileLoader.loadSafe("/assets/minecraft/lang/helper/en_US.lang", "en_US");
        SimpleLanguageFileLoader.loadSafe("/assets/minecraft/lang/helper/zh_CN.lang", "zh_CN");
    }

    @ForgeSubscribe
    public void onPlayerDeath(LivingDeathEvent event)
    {
        if (event.entity instanceof EntityPlayerMP)
        {
            setPlayerLastPosition((EntityPlayerMP) event.entity, new PositionData(event.entity));
        }
    }

    public void onServerStarting(FMLServerStartingEvent event)
    {
        if (LainHelper.enableHelperCommands)
        {
            event.registerServerCommand(new CommandBack());
            event.registerServerCommand(new CommandHome());
            event.registerServerCommand(new CommandSetHome());
            event.registerServerCommand(new CommandSpawn());
        }
    }

    public void onServerStopping(FMLServerStoppingEvent event)
    {
        playerData.unloadAll();
    }

    public void setPlayerHomePosition(EntityPlayer player, PositionData position)
    {
        NBTTagCompound data = getCustomPlayerData(player);
        if (!data.hasKey("helper"))
            data.setCompoundTag("helper", new NBTTagCompound("helper"));
        NBTTagCompound dir = data.getCompoundTag("helper");
        data = new NBTTagCompound("homePosition");
        position.writeToNBT(data);
        dir.setCompoundTag("homePosition", data);
    }

    public void setPlayerLastPosition(EntityPlayer player, PositionData position)
    {
        NBTTagCompound data = getCustomPlayerData(player);
        if (!data.hasKey("helper"))
            data.setCompoundTag("helper", new NBTTagCompound("helper"));
        NBTTagCompound dir = data.getCompoundTag("helper");
        data = new NBTTagCompound("lastPosition");
        position.writeToNBT(data);
        dir.setCompoundTag("lastPosition", data);
    }

}
