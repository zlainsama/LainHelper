package lain.mods.helper.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.Logger;
import com.google.common.collect.Lists;

public class DataStorage
{

    public final File file;
    public Logger logger;
    public NBTTagCompound data;
    private final List<DataStorageAttachment> attachments = Lists.newLinkedList();

    public DataStorage(File file)
    {
        this.file = file;
    }

    public void load()
    {
        try
        {
            data = CompressedStreamTools.readCompressed(new FileInputStream(file));
        }
        catch (IOException e)
        {
            if (logger != null)
                logger.debug("Unable to read data, creating empty compound", e);
            data = new NBTTagCompound();
        }
        try
        {
            for (DataStorageAttachment attachment : attachments)
                attachment.loadData(data);
        }
        catch (Exception e)
        {
            if (logger != null)
                logger.error("Something goes wrong", e);
        }
    }

    public boolean registerAttachment(DataStorageAttachment attachment)
    {
        if (attachments.contains(attachment))
            return false;
        if (attachments.add(attachment))
        {
            if (data != null)
                attachment.loadData(data);
            return true;
        }
        return false;
    }

    public void save()
    {
        if (data == null)
            return;
        try
        {
            for (DataStorageAttachment attachment : attachments)
                attachment.saveData(data);
        }
        catch (Exception e)
        {
            if (logger != null)
                logger.error("Something goes wrong", e);
        }
        try
        {
            CompressedStreamTools.writeCompressed(data, new FileOutputStream(file));
        }
        catch (IOException e)
        {
            if (logger != null)
                logger.error("Unable to save data, this may cause problems", e);
        }
    }

    public DataStorage setLogger(Logger logger)
    {
        this.logger = logger;
        return this;
    }

    public boolean unregisterAttachment(DataStorageAttachment attachment)
    {
        return attachments.remove(attachment);
    }

}
