package lain.mods.helper.handlers;

import lain.mods.helper.Options;
import lain.mods.helper.PlayerData;
import lain.mods.helper.utils.PositionData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerDeathHandler
{

    IChatComponent msgBackDeath = new ChatComponentTranslation("LH_BackDeath", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event)
    {
        if (event.entity instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) event.entity;
            if (Options.enableHelperCommands)
            {
                PlayerData.get(player).setLastPosition(new PositionData(event.entity));
                player.addChatMessage(msgBackDeath);
            }
        }
    }

}
