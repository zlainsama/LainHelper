package lain.mods.helper.handlers;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.UUID;
import lain.mods.helper.utils.DataStorage;
import lain.mods.helper.utils.MinecraftUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.player.PlayerEvent;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerExtraSavedDataHandler
{

    private static class Handler extends DataStorage implements IExtendedEntityProperties
    {

        public Handler(File file)
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
        }

        @Override
        public void saveNBTData(NBTTagCompound arg0)
        {
        }

    }

    private static final String _ID = "92c98451-a0c6-4899-bce6-c5cc0f75e447";
    private static final Map<UUID, WeakReference<Handler>> caches = Maps.newHashMap();

    public static void cleanup()
    {
        caches.clear();
    }

    public static DataStorage get(EntityPlayerMP p)
    {
        UUID id = p.getUniqueID();
        Handler handler = caches.get(id) != null ? caches.get(id).get() : null;
        if (handler == null)
            caches.put(id, new WeakReference<Handler>(handler = new Handler(MinecraftUtils.getSaveDirFile("playerdata", id.toString() + ".pesd"))));
        if (p.getExtendedProperties(_ID) != handler)
            p.registerExtendedProperties(_ID, handler);
        return handler;
    }

    @SubscribeEvent
    public void handleEvent(PlayerEvent.SaveToFile event)
    {
        if (event.entityPlayer instanceof EntityPlayerMP)
            PlayerExtraSavedDataHandler.get((EntityPlayerMP) event.entityPlayer).save();
    }

}
