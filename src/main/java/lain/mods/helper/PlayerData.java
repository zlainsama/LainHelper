package lain.mods.helper;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lain.mods.helper.utils.DataStorageAttachment;
import lain.mods.helper.utils.PositionData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public final class PlayerData implements DataStorageAttachment
{

    public static PlayerData get(EntityPlayerMP p)
    {
        return caches.getUnchecked(p.getUniqueID());
    }

    private static final LoadingCache<UUID, PlayerData> caches = CacheBuilder.newBuilder().expireAfterAccess(10L, TimeUnit.MINUTES).build(new CacheLoader<UUID, PlayerData>()
    {

        @Override
        public PlayerData load(UUID key) throws Exception
        {
            PlayerData newData = new PlayerData();
            LainHelper.getPlayerDataStorage(key).registerAttachmentObject("PlayerData", newData);
            return newData;
        }

    });

    private PositionData homePos;
    private PositionData lastPos;

    private PlayerData()
    {
    }

    public PositionData getHomePosition()
    {
        return homePos;
    }

    public PositionData getLastPosition()
    {
        return lastPos;
    }

    @Override
    public void loadData(NBTTagCompound data)
    {
        if (data.hasKey("homePosition"))
            (homePos = new PositionData()).readFromNBT(data.getCompoundTag("homePosition"));
        if (data.hasKey("lastPosition"))
            (lastPos = new PositionData()).readFromNBT(data.getCompoundTag("lastPosition"));
    }

    @Override
    public void saveData(NBTTagCompound data)
    {
        if (homePos != null)
        {
            NBTTagCompound item = new NBTTagCompound();
            homePos.writeToNBT(item);
            data.setTag("homePosition", item);
        }
        if (lastPos != null)
        {
            NBTTagCompound item = new NBTTagCompound();
            lastPos.writeToNBT(item);
            data.setTag("lastPosition", item);
        }
    }

    public void setHomePosition(PositionData pos)
    {
        homePos = pos;
    }

    public void setLastPosition(PositionData pos)
    {
        lastPos = pos;
    }

}
