package lain.mods.helper.commands;

import lain.mods.helper.PlayerData;
import lain.mods.helper.utils.PositionData;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CommandBack extends GeneralHelperCommand
{

    IChatComponent msgNotPlayer = new ChatComponentTranslation("LH_NotPlayer", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));
    IChatComponent msgLastPosNotFound = new ChatComponentTranslation("LH_LastPosNotFound", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));
    IChatComponent msgBackDone = new ChatComponentTranslation("LH_BackDone", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));

    @Override
    public String getCommandName()
    {
        return "back";
    }

    @Override
    public String getCommandUsage(ICommandSender par1)
    {
        return "LH_Back_Usage";
    }

    @Override
    public void processCommand(ICommandSender par1, String[] par2)
    {
        if (par1 instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) par1;
            PositionData loc = PlayerData.get(player).getLastPosition();
            if (loc != null)
            {
                PlayerData.get(player).setLastPosition(new PositionData(player));
                loc.teleportEntity(player);
                par1.addChatMessage(msgBackDone);
            }
            else
                par1.addChatMessage(msgLastPosNotFound);
        }
        else
            par1.addChatMessage(msgNotPlayer);
    }

}
