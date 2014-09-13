package lain.mods.helper.handlers;

import lain.mods.helper.SharedStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;

public class MapSaveHandler extends WorldSavedData
{

    public static void checkAndLoad()
    {
        World overworld = DimensionManager.getWorld(0);
        if (overworld != null)
        {
            if (overworld.loadItemData(MapSaveHandler.class, "MapSaveHandler") == null)
                overworld.setItemData("MapSaveHandler", new MapSaveHandler("MapSaveHandler"));
        }
    }

    public MapSaveHandler(String par1)
    {
        super(par1);
    }

    @Override
    public boolean isDirty()
    {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound arg0)
    {
        if (SharedStorage.storage != null)
            SharedStorage.storage.load();
    }

    @Override
    public void writeToNBT(NBTTagCompound arg0)
    {
        if (SharedStorage.storage != null)
            SharedStorage.storage.save();
    }

}
