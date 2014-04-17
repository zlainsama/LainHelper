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

    private static MinecraftServer getServer()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    private static Side getSide()
    {
        return FMLCommonHandler.instance().getSide();
    }

}
