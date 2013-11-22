package lain.mods.helper.customport;

import java.io.IOException;
import java.net.ServerSocket;
import lain.mods.helper.LainHelper;
import com.google.common.io.Closeables;

public class CustomPort
{

    private static boolean checkPort(int port) throws IOException
    {
        if (port <= 0)
            return false;
        ServerSocket serversocket = null;
        int i = -1;
        try
        {
            serversocket = new ServerSocket(port);
            i = serversocket.getLocalPort();
        }
        catch (IOException e)
        {
            i = -1;
        }
        finally
        {
            Closeables.close(serversocket, true);
        }
        return i == port;
    }

    public static int getCustomPort() throws IOException
    {
        return checkPort(LainHelper.customLANPort) ? LainHelper.customLANPort : getRandomPort();
    }

    private static int getRandomPort() throws IOException
    {
        ServerSocket serversocket = null;
        int i = -1;
        try
        {
            serversocket = new ServerSocket(0);
            i = serversocket.getLocalPort();
        }
        finally
        {
            Closeables.close(serversocket, true);
        }
        return i;
    }

}
