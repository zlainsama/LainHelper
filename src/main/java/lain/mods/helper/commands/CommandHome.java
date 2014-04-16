package lain.mods.helper.commands;

import lain.mods.helper.note.NOTE;
import lain.mods.helper.utils.PositionData;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CommandHome extends GeneralHelperCommand
{

    IChatComponent msgNotPlayer = new ChatComponentTranslation("LH_NotPlayer", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));
    IChatComponent msgHomeNotFound = new ChatComponentTranslation("LH_HomeNotFound", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));
    IChatComponent msgHomeDone = new ChatComponentTranslation("LH_HomeDone", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));

    @Override
    public String getCommandName()
    {
        return "home";
    }

    @Override
    public String getCommandUsage(ICommandSender par1)
    {
        return "LH_Home_Usage";
    }

    @Override
    public void processCommand(ICommandSender par1, String[] par2)
    {
        if (par1 instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) par1;
            PositionData home = NOTE.get(player).getHomePosition();
            if (home != null)
            {
                NOTE.get(player).setLastPosition(new PositionData(player));
                home.teleportEntity(player);
                par1.addChatMessage(msgHomeDone);
            }
            else
                par1.addChatMessage(msgHomeNotFound);
        }
        else
            par1.addChatMessage(msgNotPlayer);
    }

}
