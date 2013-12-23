package lain.mods.helper.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public abstract class AbstractPublicCommand extends CommandBase
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
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

}
