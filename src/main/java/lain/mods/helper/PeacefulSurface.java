package lain.mods.helper;

import net.minecraft.entity.monster.IMob;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PeacefulSurface
{

    static PeacefulSurface instance = new PeacefulSurface();

    public static void setDisabled()
    {
        MinecraftForge.EVENT_BUS.unregister(instance);
    }

    public static void setEnabled()
    {
        MinecraftForge.EVENT_BUS.register(instance);
    }

    @SubscribeEvent
    public void CheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        if (event.entity instanceof IMob && event.world.getSavedLightValue(EnumSkyBlock.Sky, MathHelper.floor_float(event.x), MathHelper.floor_float(event.y), MathHelper.floor_float(event.z)) > 0)
            event.setResult(Result.DENY);
    }

}
