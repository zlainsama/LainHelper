package lain.mods.helper.command;

import lain.mods.helper.LainHelper;
import lain.mods.helper.util.PositionData;
import lain.mods.helper.util.Translator;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

public class CommandHome extends AbstractPublicCommand
{

    Translator msgNotPlayer = new Translator("LH_NotPlayer");
    Translator msgHomeNotFound = new Translator("LH_HomeNotFound");
    Translator msgHomeDone = new Translator("LH_HomeDone");

    @Override
    public String getCommandName()
    {
        return "home";
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
            PositionData home = LainHelper.proxy.getPlayerHomePosition(player);
            if (home != null)
            {
                LainHelper.proxy.setPlayerLastPosition(player, new PositionData(player));
                home.teleportEntity(player);
                msgHomeDone.sendWithColor(par1, EnumChatFormatting.DARK_RED);
            }
            else
                msgHomeNotFound.sendWithColor(par1, EnumChatFormatting.DARK_RED);
        }
        else
            msgNotPlayer.sendWithColor(par1, EnumChatFormatting.DARK_RED);
    }

}
