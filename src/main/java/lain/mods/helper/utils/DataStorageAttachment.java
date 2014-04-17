package lain.mods.helper.utils;

import net.minecraft.nbt.NBTTagCompound;

public interface DataStorageAttachment
{

    void loadData(NBTTagCompound data);

    void saveData(NBTTagCompound data);

}
