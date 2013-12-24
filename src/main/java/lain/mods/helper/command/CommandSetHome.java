package lain.mods.helper.command;

import lain.mods.helper.LainHelper;
import lain.mods.helper.util.PositionData;
import lain.mods.helper.util.Translator;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

public class CommandSetHome extends AbstractPublicCommand
{

    Translator msgNotPlayer = new Translator("LH_NotPlayer");
    Translator msgSetHomeDone = new Translator("LH_SetHomeDone");

    @Override
    public String getCommandName()
    {
        return "sethome";
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
            LainHelper.proxy.setPlayerHomePosition(player, new PositionData(player).align());
            msgSetHomeDone.sendWithColor(par1, EnumChatFormatting.DARK_RED);
        }
        else
            msgNotPlayer.sendWithColor(par1, EnumChatFormatting.DARK_RED);
    }

}
