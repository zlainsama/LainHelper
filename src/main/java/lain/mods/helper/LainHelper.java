package lain.mods.helper;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import lain.mods.helper.cheat.InfiD;
import lain.mods.helper.network.NetworkManager;

@Mod(modid = "LainHelper", useMetadata = true)
public class LainHelper
{

    @SidedProxy(clientSide = "lain.mods.helper.network.NetworkManagerClient", serverSide = "lain.mods.helper.network.NetworkManager")
    public static NetworkManager network;

    @Mod.EventHandler
    public void handleEvent(FMLInitializationEvent event)
    {
        InfiD.setup();
    }

}
