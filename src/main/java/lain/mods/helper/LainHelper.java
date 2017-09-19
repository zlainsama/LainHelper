package lain.mods.helper;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lain.mods.helper.cheat.Cheat;
import lain.mods.helper.commands.CommandBack;
import lain.mods.helper.commands.CommandHome;
import lain.mods.helper.commands.CommandSetHome;
import lain.mods.helper.commands.CommandSharedStorage;
import lain.mods.helper.commands.CommandSpawn;
import lain.mods.helper.network.NetworkManager;
import lain.mods.helper.utils.DataStorage;
import lain.mods.helper.utils.MinecraftUtils;
import lain.mods.helper.utils.PositionData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

@Mod(modid = "lainhelper", useMetadata = true, acceptedMinecraftVersions = "[1.12, 1.13)", certificateFingerprint = "aaaf83332a11df02406e9f266b1b65c1306f0f76")
public class LainHelper
{

    protected static DataStorage getPlayerDataStorage(UUID uuid)
    {
        return getPlayerDataStorage(uuid, true);
    }

    protected static DataStorage getPlayerDataStorage(UUID uuid, boolean doLoad)
    {
        File file = MinecraftUtils.getSaveDirFile("playerdata", uuid.toString() + ".lhds");
        if (!doLoad)
            return stores.getIfPresent(file);
        return stores.getUnchecked(file);
    }

    protected static DataStorage getWorldDataStorage()
    {
        return getWorldDataStorage(true);
    }

    protected static DataStorage getWorldDataStorage(boolean doLoad)
    {
        File file = MinecraftUtils.getSaveDirFile("WorldDataStorage.lhds");
        if (!doLoad)
            return stores.getIfPresent(file);
        return stores.getUnchecked(file);
    }

    public static NetworkManager network = new NetworkManager("LainHelper");

    public static int ticksKeepEndLoaded = 0;

    private static final LoadingCache<File, DataStorage> stores = CacheBuilder.newBuilder().expireAfterAccess(30L, TimeUnit.MINUTES).removalListener(new RemovalListener<File, DataStorage>()
    {

        @Override
        public void onRemoval(RemovalNotification<File, DataStorage> notification)
        {
            DataStorage store = notification.getValue();
            if (store != null)
                store.save();
        }

    }).build(new CacheLoader<File, DataStorage>()
    {

        @Override
        public DataStorage load(File file) throws Exception
        {
            if (file == null)
                throw new NullPointerException("The file to be used as DataStorage must not be null.");
            if (file.exists() && (file.isDirectory() || !file.canRead() || !file.canWrite()))
                throw new IllegalArgumentException("The file('" + file.getAbsolutePath() + "') cannot be used as DataStorage. It must be readable and writable. It must be a file, not a directory.");
            return new DataStorage(file);
        }

    });

    @Mod.EventHandler
    public void handleEvent(FMLServerStartingEvent event)
    {
        if (Options.enableHelperCommands)
        {
            event.registerServerCommand(new CommandBack());
            event.registerServerCommand(new CommandHome());
            event.registerServerCommand(new CommandSetHome());
            event.registerServerCommand(new CommandSpawn());
            event.registerServerCommand(new CommandSharedStorage());
        }
    }

    @Mod.EventHandler
    public void handleEvent(FMLServerStoppingEvent event)
    {
        stores.invalidateAll();
    }

    @SubscribeEvent
    public void handleEvent(LivingDeathEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayerMP)
        {
            PlayerData.get((EntityPlayerMP) event.getEntityLiving()).setLastPosition(new PositionData(event.getEntityLiving()));
        }
    }

    @SubscribeEvent
    public void handleEvent(PlayerEvent.SaveToFile event)
    {
        if (event.getEntityPlayer() instanceof EntityPlayerMP)
        {
            DataStorage store = getPlayerDataStorage(event.getEntityPlayer().getUniqueID(), false);
            if (store != null)
                store.save();
        }
    }

    @SubscribeEvent
    public void handleEvent(ServerTickEvent event)
    {
        if (ticksKeepEndLoaded > 0)
        {
            ticksKeepEndLoaded -= 1;

            WorldServer w = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(1);
            if (w != null)
            {
                for (int i = -8; i <= 8; ++i)
                {
                    for (int j = -8; j <= 8; ++j)
                    {
                        w.getChunkFromChunkCoords(i, j);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void handleEvent(WorldEvent.Save event)
    {
        if (!event.getWorld().isRemote && event.getWorld().provider.getDimension() == 0)
        {
            DataStorage store = getWorldDataStorage(false);
            if (store != null)
                store.save();
        }
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event)
    {
        Options.loadConfig(new Configuration(event.getSuggestedConfigurationFile()), event.getModLog());

        MinecraftForge.EVENT_BUS.register(this);
        // FMLCommonHandler.instance().bus().register(this);

        if (Cheat.INSTANCE == null)
            throw new RuntimeException();
    }

}
