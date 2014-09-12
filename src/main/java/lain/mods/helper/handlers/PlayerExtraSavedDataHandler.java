package lain.mods.helper.handlers;

import java.io.File;
import lain.mods.helper.utils.DataStorage;
import lain.mods.helper.utils.MinecraftUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerExtraSavedDataHandler extends DataStorage implements IExtendedEntityProperties
{

    private static final String _ID = "92c98451-a0c6-4899-bce6-c5cc0f75e447";

    public static DataStorage get(EntityPlayerMP player)
    {
        IExtendedEntityProperties obj = player.getExtendedProperties(_ID);
        if (obj instanceof DataStorage)
            return (DataStorage) obj;
        player.registerExtendedProperties(_ID, obj = new PlayerExtraSavedDataHandler(MinecraftUtils.getSaveDirFile("playerdata", player.getUniqueID().toString() + ".pesd")));
        return (DataStorage) obj;
    }

    public PlayerExtraSavedDataHandler(File file)
    {
        super(file);
    }

    @Override
    public void init(Entity arg0, World arg1)
    {
    }

    @Override
    public void loadNBTData(NBTTagCompound arg0)
    {
        load();
    }

    @Override
    public void saveNBTData(NBTTagCompound arg0)
    {
        save();
    }

}
