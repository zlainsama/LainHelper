package lain.mods.helper;

import lain.mods.helper.cheat.Cheat;
import lain.mods.helper.network.NetworkManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "lainhelper", useMetadata = true, acceptedMinecraftVersions = "[1.12.1]")
public class LainHelper
{

    public static NetworkManager network = new NetworkManager("LainHelper");

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event)
    {
        if (Cheat.INSTANCE == null)
            throw new RuntimeException();
    }

}
