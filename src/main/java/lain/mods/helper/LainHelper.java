package lain.mods.helper;

import lain.mods.helper.note.NOTE;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "LainHelper", useMetadata = true)
public class LainHelper
{

    Logger logger;
    Configuration config;

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
        NOTE.load();
    }

}
