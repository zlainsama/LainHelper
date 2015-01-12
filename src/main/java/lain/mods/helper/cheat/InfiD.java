package lain.mods.helper.cheat;

import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class InfiD
{

    public static void setup()
    {
        if (INSTANCE == null)
            throw new RuntimeException();
    }

    private static final InfiD INSTANCE = new InfiD();
    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));

    private InfiD()
    {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    public boolean check(Entity entity)
    {
        if (entity instanceof EntityPlayerMP)
            return _MYID.contains(entity.getUniqueID());
        return false;
    }

    public NBTTagCompound getData(Entity entity)
    {
        NBTTagCompound root = entity.getEntityData();
        if (!root.hasKey("InfiD", 10))
            root.setTag("InfiD", new NBTTagCompound());
        return root.getCompoundTag("InfiD");
    }

    public int getIntOrCreate(NBTTagCompound compound, String name, int defaultvalue)
    {
        if (!compound.hasKey(name, 3))
            compound.setInteger(name, defaultvalue);
        return compound.getInteger(name);
    }

    public int getTimeRegen(Entity entity, int defaultvalue)
    {
        return getIntOrCreate(getData(entity), "timeRegen", defaultvalue);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void proc(LivingHurtEvent event)
    {
        if (check(event.entityLiving))
            setTimeRegen(event.entityLiving, 100);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void proc(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START && check(event.player))
        {
            int timeRegen = getTimeRegen(event.player, 20);
            if (!event.player.isEntityAlive())
                timeRegen = 20;
            else if (--timeRegen < 0)
                timeRegen = 0;
            if (timeRegen == 0)
            {
                event.player.heal(event.player.getMaxHealth() * 0.05F);
                timeRegen = 40;
            }
            setTimeRegen(event.player, timeRegen);
        }
    }

    public void setTimeRegen(Entity entity, int value)
    {
        getData(entity).setInteger("timeRegen", value);
    }

}
