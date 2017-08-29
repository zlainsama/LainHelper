package lain.mods.helper.commands;

import lain.mods.helper.Options;
import lain.mods.helper.PlayerData;
import lain.mods.helper.utils.Message;
import lain.mods.helper.utils.PositionData;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

public class CommandSetHome extends GeneralHelperCommand
{

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException
    {
        if (sender instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            PositionData pos = new PositionData(player)/* .align() */;
            if (!Options.overworldHomeOnly || pos.dimension == 0)
            {
                PlayerData.get(player).setHomePosition(pos);
                sender.sendMessage(Message.msgSetHomeDone.convert(TextFormatting.GREEN));
            }
            else
                sender.sendMessage(Message.msgOverworldHomeOnly.convert(TextFormatting.RED));
        }
        else
            sender.sendMessage(Message.msgNotPlayer.convert(TextFormatting.RED));
    }

    @Override
    public String getName()
    {
        return "sethome";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return Message.msgSetHomeUsage.key;
    }

}
