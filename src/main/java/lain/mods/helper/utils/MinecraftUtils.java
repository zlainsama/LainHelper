package lain.mods.helper.utils;

import java.io.File;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class MinecraftUtils
{

    public static File getSaveDirFile() // Need active server instance
    {
        MinecraftServer server = getServer();
        if (server == null)
            return null;
        switch (getSide())
        {
            case CLIENT:
                return new File(server.getFile("saves"), server.worldServerForDimension(0).getSaveHandler().getWorldDirectoryName());
            case SERVER:
            default:
                return server.getFile(server.getFolderName());
        }
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

    public static MinecraftServer getServer()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    public static Side getSide()
    {
        return FMLCommonHandler.instance().getSide();
    }

}
