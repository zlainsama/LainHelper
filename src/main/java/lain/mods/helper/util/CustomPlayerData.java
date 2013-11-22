package lain.mods.helper.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import lain.mods.helper.LainHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class CustomPlayerData implements IExtendedEntityProperties
{

    private NBTTagCompound customData;
    private EntityPlayer player;

    public NBTTagCompound getCustomPlayerData()
    {
        if (customData == null)
            load();
        return customData;
    }

    @Override
    public void init(Entity entity, World world)
    {
        player = (EntityPlayer) entity;
    }

    public void load()
    {
        File saveDir = new File(LainHelper.proxy.getActiveSaveDirectory(), "helper");
        if (saveDir.exists() || saveDir.mkdirs())
        {
            File file = new File(saveDir, "player." + player.username + ".dat");
            try
            {
                customData = (NBTTagCompound) CompressedStreamTools.readCompressed(new FileInputStream(file));
            }
            catch (Exception e)
            {
                customData = new NBTTagCompound("CustomPlayerData");
            }
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound compound)
    {
        load();
    }

    public void save()
    {
        File saveDir = new File(LainHelper.proxy.getActiveSaveDirectory(), "helper");
        if (saveDir.exists() || saveDir.mkdirs())
        {
            File file = new File(saveDir, "player." + player.username + ".dat");
            try
            {
                CompressedStreamTools.writeCompressed(getCustomPlayerData(), new FileOutputStream(file));
            }
            catch (Exception ignored)
            {
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound compound)
    {
        save();
    }

}
