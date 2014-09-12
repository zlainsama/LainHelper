package lain.mods.helper;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.UUID;
import lain.mods.helper.handlers.PlayerExtraSavedDataHandler;
import lain.mods.helper.utils.DataStorageAttachment;
import lain.mods.helper.utils.PositionData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import com.google.common.collect.Maps;

public final class PlayerData implements DataStorageAttachment
{

    private static final Map<UUID, WeakReference<PlayerData>> caches = Maps.newHashMap();

    public static PlayerData get(EntityPlayerMP p)
    {
        UUID id = p.getUniqueID();
        PlayerData data = caches.get(id) != null ? caches.get(id).get() : null;
        if (data == null)
        {
            PlayerExtraSavedDataHandler.get(p).registerAttachmentObject("PlayerData", data = new PlayerData());
            caches.put(id, new WeakReference<PlayerData>(data));
        }
        return data;
    }

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
