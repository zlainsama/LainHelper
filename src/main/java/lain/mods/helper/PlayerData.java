package lain.mods.helper;

import lain.mods.helper.utils.PositionData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public final class PlayerData
{

    private static final String _ID = "92c98451-a0c6-4899-bce6-c5cc0f75e447";

    private static NBTTagCompound _GetOrCreateCompound(NBTTagCompound base, String name)
    {
        if (!base.hasKey(name))
            base.setTag(name, new NBTTagCompound());
        return base.getCompoundTag(name);
    }

    private static NBTTagCompound _GetPlayerPersistedData(EntityPlayer p)
    {
        return _GetOrCreateCompound(p.getEntityData(), EntityPlayer.PERSISTED_NBT_TAG);
    }

    private static NBTTagCompound _GetRawData(EntityPlayer p)
    {
        return _GetOrCreateCompound(_GetPlayerPersistedData(p), _ID);
    }

    public static PlayerData get(EntityPlayer p)
    {
        return new PlayerData(_GetRawData(p));
    }

    private final NBTTagCompound d;

    private PlayerData(NBTTagCompound data)
    {
        d = data;
    }

    public PositionData getHomePosition()
    {
        if (d.hasKey("homePosition"))
        {
            PositionData loc = new PositionData();
            loc.readFromNBT(d.getCompoundTag("homePosition"));
            return loc;
        }
        return null;
    }

    public PositionData getLastPosition()
    {
        if (d.hasKey("lastPosition"))
        {
            PositionData loc = new PositionData();
            loc.readFromNBT(d.getCompoundTag("lastPosition"));
            return loc;
        }
        return null;
    }

    public void setHomePosition(PositionData pos)
    {
        NBTTagCompound data = new NBTTagCompound();
        pos.writeToNBT(data);
        d.setTag("homePosition", data);
    }

    public void setLastPosition(PositionData pos)
    {
        NBTTagCompound data = new NBTTagCompound();
        pos.writeToNBT(data);
        d.setTag("lastPosition", data);
    }

}
