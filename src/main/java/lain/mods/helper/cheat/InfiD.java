package lain.mods.helper.cheat;

import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class InfiD
{

    public static void setup()
    {
        InfiD i = new InfiD();
        MinecraftForge.EVENT_BUS.register(i);
    }

    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));

    private InfiD()
    {
    }

    public boolean check(Entity entity)
    {
        if (entity instanceof EntityPlayerMP)
            return _MYID.contains(entity.getUniqueID());
        return false;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void proc(LivingHealEvent event)
    {
        if (check(event.entityLiving))
            event.amount *= 2.0F;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void proc(LivingHurtEvent event)
    {
        if (check(event.entityLiving))
            event.ammount *= 0.5F;
    }

}
