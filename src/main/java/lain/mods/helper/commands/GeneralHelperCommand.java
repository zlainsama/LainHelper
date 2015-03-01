package lain.mods.helper.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public abstract class GeneralHelperCommand extends CommandBase
{

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1)
    {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

}
