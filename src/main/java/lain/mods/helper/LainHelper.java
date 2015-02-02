package lain.mods.helper;

import lain.mods.helper.cheat.InfiD;
import lain.mods.helper.network.NetworkManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

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
