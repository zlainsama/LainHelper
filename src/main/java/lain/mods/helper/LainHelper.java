package lain.mods.helper;

import lain.mods.helper.network.NetworkManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;

@Mod(modid = "LainHelper", useMetadata = true)
public class LainHelper
{

    @SidedProxy(serverSide = "lain.mods.helper.network.NetworkManager", clientSide = "lain.mods.helper.network.NetworkManagerClient")
    public static NetworkManager network;

}
