package lain.mods.helper.cheat;

import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.google.common.collect.ImmutableSet;

public class InfiD
{

    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void handleEvent(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            EntityPlayer player = event.player;
            if (player instanceof EntityPlayerMP)
            {
                if (_MYID.contains(player.getUniqueID()))
                {
                    player.removePotionEffect(17);
                    player.addPotionEffect(new PotionEffect(11, 10, 1, true, false));
                    player.addPotionEffect(new PotionEffect(12, 10, 0, true, false));
                    player.addPotionEffect(new PotionEffect(13, 10, 0, true, false));
                    player.addPotionEffect(new PotionEffect(16, 400, 0, true, false));
                }
            }
        }
    }

}
