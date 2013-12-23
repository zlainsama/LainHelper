package lain.mods.helper.command;

import lain.mods.helper.LainHelper;
import lain.mods.helper.util.PositionData;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.FMLCommonHandler;

public class CommandSpawn extends AbstractPublicCommand
{

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
        new PositionData(FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0).provider.getRandomizedSpawnPoint(), 0).teleportEntity(player, true);
    }

}
