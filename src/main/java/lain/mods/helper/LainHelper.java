package lain.mods.helper;

import lain.mods.helper.commands.CommandBack;
import lain.mods.helper.commands.CommandHome;
import lain.mods.helper.commands.CommandSetHome;
import lain.mods.helper.commands.CommandSpawn;
import lain.mods.helper.handlers.PlayerDeathHandler;
import lain.mods.helper.skills.Skill;
import lain.mods.helper.utils.DataStorage;
import lain.mods.helper.utils.MinecraftUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = "LainHelper", useMetadata = true)
public class LainHelper
{

    Logger logger;
    Configuration config;

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
            SharedStorage.storage.logger = logger;
            SharedStorage.storage.load();
            event.registerServerCommand(SharedStorage.createCommandOpenStorage());
        }
        if (Options.enableSkills)
        {
            Skill.setEnabled(event);
        }
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event)
    {
        if (Options.enableSharedStorage)
        {
            SharedStorage.storage.save();
            if (SharedStorage.inventory != null)
                SharedStorage.storage.unregisterAttachment(SharedStorage.inventory);
            SharedStorage.storage = null;
            SharedStorage.inventory = null;
        }
        if (Options.enableSkills)
        {
            Skill.setDisabled(event);
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
        MinecraftForge.EVENT_BUS.register(new PlayerDeathHandler());
    }

}
