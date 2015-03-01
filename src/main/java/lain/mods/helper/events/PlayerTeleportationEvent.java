package lain.mods.helper.events;

import lain.mods.helper.utils.PositionData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.eventhandler.Cancelable;

@Cancelable
public class PlayerTeleportationEvent extends PlayerEvent
{

    public PositionData target;
    public IChatComponent message;

    public PlayerTeleportationEvent(EntityPlayer player, PositionData target)
    {
        super(player);
        this.target = target;
    }

}
