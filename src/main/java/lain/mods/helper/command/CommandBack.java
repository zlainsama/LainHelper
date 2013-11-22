package lain.mods.helper.command;

import lain.mods.helper.LainHelper;
import lain.mods.helper.util.PositionData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandBack extends CommandBase
{

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1)
    {
        return true;
    }

    @Override
    public int compareTo(Object arg0)
    {
        if (arg0 instanceof ICommand)
            return getCommandName().compareTo(((ICommand) arg0).getCommandName());
        return 0;
    }

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
        EntityPlayerMP player = getCommandSenderAsPlayer(par1);
        PositionData loc = LainHelper.proxy.getPlayerLastPosition(player);
        if (loc != null)
        {
            LainHelper.proxy.setPlayerLastPosition(player, new PositionData(player));
            loc.teleportEntity(player);
        }
    }

}
