package lain.mods.helper.commands;

import lain.mods.helper.PlayerData;
import lain.mods.helper.events.PlayerTeleportationEvent;
import lain.mods.helper.utils.PositionData;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;

public class CommandBack extends GeneralHelperCommand
{

    IChatComponent msgNotPlayer = new ChatComponentTranslation("LH_NotPlayer", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));
    IChatComponent msgLastPosNotFound = new ChatComponentTranslation("LH_LastPosNotFound", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));
    IChatComponent msgBackDone = new ChatComponentTranslation("LH_BackDone", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));

    @Override
    public void execute(ICommandSender par1, String[] par2)
    {
        if (par1 instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) par1;
            PositionData target = PlayerData.get(player).getLastPosition();
            if (target != null)
            {
                PositionData last = new PositionData(player);
                PlayerTeleportationEvent event = new PlayerTeleportationEvent(player, target);
                if (MinecraftForge.EVENT_BUS.post(event))
                {
                    if (event.message != null)
                        par1.addChatMessage(event.message);
                }
                else
                {
                    target = event.target;
                    PlayerData.get(player).setLastPosition(last);
                    target.teleportEntity(player);
                    par1.addChatMessage(msgBackDone);
                }
            }
            else
                par1.addChatMessage(msgLastPosNotFound);
        }
        else
            par1.addChatMessage(msgNotPlayer);
    }

    @Override
    public String getCommandUsage(ICommandSender par1)
    {
        return "LH_Back_Usage";
    }

    @Override
    public String getName()
    {
        return "back";
    }

}
