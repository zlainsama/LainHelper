package lain.mods.helper.utils;

import java.io.File;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class MinecraftUtils
{

    public static File getSaveDirFile()
    {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (FMLCommonHandler.instance().getSide().isClient())
            return server.getWorld(0).getSaveHandler().getWorldDirectory();
        return server.getFile(server.getFolderName());
    }

    public static File getSaveDirFile(String filename)
    {
        return filename == null ? getSaveDirFile() : new File(getSaveDirFile(), filename);
    }

    public static File getSaveDirFile(String dirname, String filename)
    {
        File dir = getSaveDirFile(dirname);
        if (dir.exists() || dir.mkdirs())
            return new File(dir, filename);
        return null;
    }

}
