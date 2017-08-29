package lain.mods.helper.commands;

import lain.mods.helper.SharedStorage;
import lain.mods.helper.utils.Message;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

public class CommandSharedStorage extends GeneralHelperCommand
{

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException
    {
        if (sender instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            player.displayGUIChest(SharedStorage.getInventory());
        }
        else
            sender.sendMessage(Message.msgNotPlayer.convert(TextFormatting.RED));
    }

    @Override
    public String getName()
    {
        return "sharedstorage";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return Message.msgSharedStorageUsage.key;
    }

}
