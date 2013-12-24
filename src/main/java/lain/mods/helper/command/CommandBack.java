package lain.mods.helper.command;

import lain.mods.helper.LainHelper;
import lain.mods.helper.util.PositionData;
import lain.mods.helper.util.Translator;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

public class CommandBack extends AbstractPublicCommand
{

    Translator msgNotPlayer = new Translator("LH_NotPlayer");
    Translator msgLastPosNotFound = new Translator("LH_LastPosNotFound");
    Translator msgBackDone = new Translator("LH_BackDone");

    @Override
    public String getCommandName()
    {
        return "back";
    }

    @Override
    public String getCommandUsage(ICommandSender par1)
    {
        return null;
    }

    @Override
    public void processCommand(ICommandSender par1, String[] par2)
    {
        if (par1 instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) par1;
            PositionData loc = LainHelper.proxy.getPlayerLastPosition(player);
            if (loc != null)
            {
                LainHelper.proxy.setPlayerLastPosition(player, new PositionData(player));
                loc.teleportEntity(player);
                msgBackDone.sendWithColor(par1, EnumChatFormatting.DARK_RED);
            }
            else
                msgLastPosNotFound.sendWithColor(par1, EnumChatFormatting.DARK_RED);
        }
        else
            msgNotPlayer.sendWithColor(par1, EnumChatFormatting.DARK_RED);
    }
}
