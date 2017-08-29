package lain.mods.helper.events;

import lain.mods.helper.utils.PositionData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class PlayerTeleportationEvent extends PlayerEvent
{

    public PositionData target;
    public ITextComponent message;

    public PlayerTeleportationEvent(EntityPlayer player, PositionData target)
    {
        super(player);
        this.target = target;
    }

}
