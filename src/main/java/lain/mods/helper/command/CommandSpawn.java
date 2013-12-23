package lain.mods.helper.command;

import lain.mods.helper.LainHelper;
import lain.mods.helper.util.PositionData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.FMLCommonHandler;

public class CommandSpawn extends CommandBase
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
        return "spawn";
    }

    @Override
    public String getCommandUsage(ICommandSender par1)
    {
        return null;
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender par1, String[] par2)
    {
        EntityPlayerMP player = getCommandSenderAsPlayer(par1);
        LainHelper.proxy.setPlayerLastPosition(player, new PositionData(player));
        new PositionData(FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0).provider.getRandomizedSpawnPoint(), 0).teleportEntity(player, true);
    }

}
