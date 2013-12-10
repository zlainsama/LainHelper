package lain.mods.helper.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class DataStorage
{

    protected static File getSaveDirFile()
    {
        MinecraftServer server = getServer();
        if (server == null)
            return null;
        if (getSide() == Side.CLIENT)
            return new File(server.getFile("saves"), server.worldServerForDimension(0).getSaveHandler().getWorldDirectoryName());
        else
            return server.getFile(server.getFolderName());
    }

    protected static File getSaveDirFile(String dirName)
    {
        return dirName == null ? getSaveDirFile() : new File(getSaveDirFile(), dirName);
    }

    private static MinecraftServer getServer()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    private static Side getSide()
    {
        return FMLCommonHandler.instance().getSide();
    }

    private final Map<String, NBTTagCompound> map = Maps.newHashMap();

    public final String name;

    public DataStorage(String name)
    {
        this.name = name;
    }

    protected String getBaseDir()
    {
        return null;
    }

    protected File getDataFolder()
    {
        File dir = new File(getSaveDirFile(getBaseDir()), name);
        if (!dir.exists())
            dir.mkdirs();
        return dir;
    }

    protected void handleException(Exception e)
    {
    }

    public NBTTagCompound load(String id)
    {
        NBTTagCompound data = map.get(id);
        if (data == null)
        {
            try
            {
                map.put(id, CompressedStreamTools.readCompressed(new FileInputStream(new File(getDataFolder(), id + ".dat"))));
            }
            catch (FileNotFoundException ignored)
            {
                map.put(id, new NBTTagCompound());
            }
            catch (IOException e)
            {
                handleException(e);
                reset(id);
            }
        }
        return data;
    }

    public boolean reset(String id)
    {
        map.put(id, new NBTTagCompound());
        return save(id);
    }

    public boolean save(String id)
    {
        NBTTagCompound data = map.get(id);
        if (data != null)
        {
            try
            {
                CompressedStreamTools.writeCompressed(data, new FileOutputStream(new File(getDataFolder(), id + ".dat")));
            }
            catch (IOException e)
            {
                handleException(e);
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean unload(String id)
    {
        if (!save(id))
            return false;
        return map.remove(id) != null;
    }

    public boolean unloadAll()
    {
        boolean res = true;
        for (String id : map.keySet().toArray(new String[0]))
            if (!unload(id))
                res = false;
        return res;
    }

}
