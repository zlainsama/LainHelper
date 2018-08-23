package lain.mods.helper;

import lain.mods.helper.cheat.CheatCapsProvider;
import lain.mods.helper.cheat.CheatEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = LainHelper.MODID, useMetadata = true, acceptedMinecraftVersions = "[1.12, 1.13)", certificateFingerprint = "aaaf83332a11df02406e9f266b1b65c1306f0f76")
public class LainHelper
{

    public static final String MODID = "lainhelper";

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(CheatCapsProvider.class);
        MinecraftForge.EVENT_BUS.register(CheatEvents.class);
    }

}
