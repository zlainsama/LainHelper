package lain.mods.helper;

import java.io.File;
import java.io.IOException;
import lain.mods.helper.cheat.InfiD;
import lain.mods.helper.commands.CommandBack;
import lain.mods.helper.commands.CommandHome;
import lain.mods.helper.commands.CommandSetHome;
import lain.mods.helper.commands.CommandSpawn;
import lain.mods.helper.handlers.PlayerDeathHandler;
import lain.mods.helper.handlers.PlayerExtraSavedDataHandler;
import lain.mods.helper.handlers.WorldExtraSavedDataHandler;
import lain.mods.helper.note.network.NoteSync;
import lain.mods.helper.survivalists.Survivalists;
import lain.mods.helper.utils.DataStorage;
import lain.mods.helper.utils.MinecraftUtils;
import lain.mods.helper.utils.ServerTicks;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "LainHelper", useMetadata = true)
public class LainHelper
{

    Logger logger;
    Configuration config;

    @SidedProxy(clientSide = "lain.mods.helper.note.network.NoteSyncClient", serverSide = "lain.mods.helper.note.network.NoteSync")
    public static NoteSync managerNoteSync;

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        if (Options.enableHelperCommands)
        {
            event.registerServerCommand(new CommandBack());
            event.registerServerCommand(new CommandHome());
            event.registerServerCommand(new CommandSetHome());
            event.registerServerCommand(new CommandSpawn());
        }
        if (Options.enableSharedStorage)
        {
            SharedStorage.storage = new DataStorage(MinecraftUtils.getSaveDirFile("SharedStorage.dat"));
            event.registerServerCommand(SharedStorage.createCommandOpenStorage());
        }
        if (Options.autoDisableMobGriefingAndFireTickInNewWorld)
        {
            ServerTicks.RunOnce(new Runnable()
            {

                @Override
                public void run()
                {
                    File marker = MinecraftUtils.getSaveDirFile("WorldMarker");
                    if (marker.exists())
                        return;
                    try
                    {
                        World overworld = DimensionManager.getWorld(0);
                        if (overworld != null && marker.createNewFile())
                        {
                            GameRules rules = overworld.getGameRules();
                            rules.setOrCreateGameRule("mobGriefing", "false");
                            rules.setOrCreateGameRule("doFireTick", "false");
                        }
                    }
                    catch (IOException e)
                    {
                        logger.throwing(e);
                    }
                }

            });
        }
    }

    @Mod.EventHandler
    public void preEnable(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        config = new Configuration(event.getSuggestedConfigurationFile());

        try
        {
            config.load();
            Options.loadConfig(config, logger);
            config.save();
        }
        catch (Exception e)
        {
            logger.catching(Level.ERROR, e);
        }
    }

    @Mod.EventHandler
    public void setEnabled(FMLInitializationEvent event)
    {
        if (Options.enableSurvivalists)
            Survivalists.setEnabled();

        MinecraftForge.EVENT_BUS.register(new PlayerDeathHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerExtraSavedDataHandler());
        MinecraftForge.EVENT_BUS.register(new WorldExtraSavedDataHandler());
        InfiD.load();
        managerNoteSync.setEnabled();
    }

}
