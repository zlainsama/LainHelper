package lain.mods.helper.cheat;

import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.google.common.collect.ImmutableSet;

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

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void proc(LivingSetAttackTargetEvent event)
    {
        if (event.target == null)
            return;
        if (check(event.target))
        {
            // if (event.entityLiving.getAITarget() == event.target)
            // event.entityLiving.setRevengeTarget(null);
            if (event.entityLiving instanceof EntityLiving)
            {
                EntityLiving entityLiving = (EntityLiving) event.entityLiving;
                if (entityLiving.getAttackTarget() == event.target)
                    entityLiving.setAttackTarget(null);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void proc(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START && check(event.player))
        {
            event.player.removePotionEffect(17);
        }
    }

}
