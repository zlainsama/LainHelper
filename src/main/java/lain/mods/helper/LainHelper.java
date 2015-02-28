package lain.mods.helper;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lain.mods.helper.network.NetworkManager;
import lain.mods.helper.utils.DataStorage;
import lain.mods.helper.utils.MinecraftUtils;
import lain.mods.helper.utils.PositionData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

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

    @SidedProxy(serverSide = "lain.mods.helper.network.NetworkManager", clientSide = "lain.mods.helper.network.NetworkManagerClient")
    public static NetworkManager network;

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
                throw new IllegalArgumentException("The file('" + file.toString() + "') cannot be used as DataStorage. It must be readable and writable. It must be a file, not a directory.");
            return new DataStorage(file);
        }

    });

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
        if (!event.world.isRemote && event.world.provider.dimensionId == 0)
        {
            DataStorage store = getWorldDataStorage(false);
            if (store != null)
                store.save();
        }
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

}
