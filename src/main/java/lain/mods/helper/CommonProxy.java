package lain.mods.helper;

import java.io.File;
import lain.mods.helper.command.CommandBack;
import lain.mods.helper.command.CommandHome;
import lain.mods.helper.command.CommandSetHome;
import lain.mods.helper.command.CommandSpawn;
import lain.mods.helper.tile.BlockCobbleCube;
import lain.mods.helper.tile.BlockWaterCube;
import lain.mods.helper.tile.TileCobbleCube;
import lain.mods.helper.tile.TileWaterCube;
import lain.mods.helper.util.CustomPlayerData;
import lain.mods.helper.util.PositionData;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy
{

    private static final String identifier = "fd2f83d9-eb08-4bec-864f-70c10520865b";

    public File getActiveSaveDirectory()
    {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server == null)
            return null;
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
            return new File(server.getFile("saves"), server.worldServerForDimension(0).getSaveHandler().getWorldDirectoryName());
        else
            return server.getFile(server.getFolderName());
    }

    public NBTTagCompound getCustomPlayerData(EntityPlayer player)
    {
        IExtendedEntityProperties obj = player.getExtendedProperties(identifier);
        if (obj instanceof CustomPlayerData)
            return ((CustomPlayerData) obj).getCustomPlayerData();
        return new NBTTagCompound("");
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
        MinecraftForge.EVENT_BUS.register(this);

        LainHelper.blockWaterCube = new BlockWaterCube(LainHelper.idBlockWaterCube);
        LainHelper.blockWaterCube.setUnlocalizedName("blockWaterCube");
        LanguageRegistry.addName(LainHelper.blockWaterCube, "Water Cube");
        GameRegistry.addShapedRecipe(new ItemStack(LainHelper.blockWaterCube), "PWP", "WEW", "PWP", 'P', Block.pistonBase, 'W', Item.bucketWater, 'E', Item.eyeOfEnder);

        LainHelper.blockCobbleCube = new BlockCobbleCube(LainHelper.idBlockCobbleCube);
        LainHelper.blockCobbleCube.setUnlocalizedName("blockCobbleCube");
        LanguageRegistry.addName(LainHelper.blockCobbleCube, "Cobble Cube");
        GameRegistry.addShapedRecipe(new ItemStack(LainHelper.blockCobbleCube), "SES", "WIL", "SPS", 'P', Block.pistonBase, 'W', Item.bucketWater, 'E', Item.eyeOfEnder, 'L', Item.bucketLava, 'S', Block.stone, 'I', Item.pickaxeIron);

        GameRegistry.addShapedRecipe(new ItemStack(Item.monsterPlacer, 1, 95), "BEB", "BFB", "BSB", 'B', Item.bone, 'E', Item.eyeOfEnder, 'F', Item.rottenFlesh, 'S', Block.slowSand);

        GameRegistry.registerBlock(LainHelper.blockWaterCube, "blockWaterCube");
        GameRegistry.registerTileEntity(TileWaterCube.class, "tileWaterCube");
        GameRegistry.registerBlock(LainHelper.blockCobbleCube, "blockCobbleCube");
        GameRegistry.registerTileEntity(TileCobbleCube.class, "tileCobbleCube");

        Vampirism.setup();
    }

    public void load(FMLServerStartingEvent event)
    {
        if (LainHelper.enableHelperCommands)
        {
            event.registerServerCommand(new CommandBack());
            event.registerServerCommand(new CommandHome());
            event.registerServerCommand(new CommandSetHome());
            event.registerServerCommand(new CommandSpawn());
        }
    }

    @ForgeSubscribe
    public void onPlayerConstruction(EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayerMP)
        {
            event.entity.registerExtendedProperties(identifier, new CustomPlayerData());
        }
    }

    @ForgeSubscribe
    public void onPlayerDeath(LivingDeathEvent event)
    {
        if (event.entity instanceof EntityPlayerMP)
        {
            setPlayerLastPosition((EntityPlayerMP) event.entity, new PositionData(event.entity));
        }
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

        IExtendedEntityProperties obj = player.getExtendedProperties(identifier);
        if (obj instanceof CustomPlayerData)
            ((CustomPlayerData) obj).save();
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

        IExtendedEntityProperties obj = player.getExtendedProperties(identifier);
        if (obj instanceof CustomPlayerData)
            ((CustomPlayerData) obj).save();
    }

}
