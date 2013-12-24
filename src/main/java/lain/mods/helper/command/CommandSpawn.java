package lain.mods.helper.command;

import lain.mods.helper.LainHelper;
import lain.mods.helper.util.PositionData;
import lain.mods.helper.util.Translator;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.FMLCommonHandler;

public class CommandSpawn extends AbstractPublicCommand
{

    Translator msgNotPlayer = new Translator("LH_NotPlayer");
    Translator msgSpawnDone = new Translator("LH_SpawnDone");

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
        if (par1 instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) par1;
            LainHelper.proxy.setPlayerLastPosition(player, new PositionData(player));
            new PositionData(FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0).provider.getRandomizedSpawnPoint(), 0).teleportEntity(player, true);
            msgSpawnDone.sendWithColor(par1, EnumChatFormatting.DARK_RED);
        }
        else
            msgNotPlayer.sendWithColor(par1, EnumChatFormatting.DARK_RED);
    }

}
