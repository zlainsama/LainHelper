package lain.mods.helper.command;

import lain.mods.helper.LainHelper;
import lain.mods.helper.util.PositionData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandSpawn extends CommandBase
{

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1)
    {
        return true;
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
    public void processCommand(ICommandSender par1, String[] par2)
    {
        EntityPlayerMP player = getCommandSenderAsPlayer(par1);
        LainHelper.proxy.setPlayerLastPosition(player, new PositionData(player));
        if (player.dimension != 0)
            player.travelToDimension(0);
        new PositionData(player.worldObj.provider.getRandomizedSpawnPoint()).teleportEntity(player, true);
    }

}
