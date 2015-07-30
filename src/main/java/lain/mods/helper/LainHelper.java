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
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

@Mod(modid = "LainHelper", useMetadata = true)
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

    @SubscribeEvent
    public void handleEvent(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        Cheat.INSTANCE.setFlagsClient(-1);
    }

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
        if (event.entityLiving instanceof EntityPlayerMP)
        {
            PlayerData.get((EntityPlayerMP) event.entityLiving).setLastPosition(new PositionData(event.entityLiving));
        }
    }

    @SubscribeEvent
    public void handleEvent(PlayerEvent.SaveToFile event)
    {
        if (event.entityPlayer instanceof EntityPlayerMP)
        {
            DataStorage store = getPlayerDataStorage(event.entityPlayer.getUniqueID(), false);
            if (store != null)
                store.save();
        }
    }

    @SubscribeEvent
    public void handleEvent(WorldEvent.Save event)
    {
        if (!event.world.isRemote && event.world.provider.getDimensionId() == 0)
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
        FMLCommonHandler.instance().bus().register(this);

        if (Cheat.INSTANCE == null)
            throw new RuntimeException();
    }

}
