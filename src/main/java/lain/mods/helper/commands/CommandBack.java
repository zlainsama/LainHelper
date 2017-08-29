package lain.mods.helper.commands;

import lain.mods.helper.PlayerData;
import lain.mods.helper.events.PlayerTeleportationEvent;
import lain.mods.helper.utils.Message;
import lain.mods.helper.utils.PositionData;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;

public class CommandBack extends GeneralHelperCommand
{

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException
    {
        if (sender instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            PositionData target = PlayerData.get(player).getLastPosition();
            if (target != null)
            {
                PositionData last = new PositionData(player);
                PlayerTeleportationEvent event = new PlayerTeleportationEvent(player, target);
                if (MinecraftForge.EVENT_BUS.post(event))
                {
                    if (event.message != null)
                        sender.sendMessage(event.message);
                    else
                        sender.sendMessage(Message.msgTeleportationFailed.convert(TextFormatting.RED));
                }
                else
                {
                    target = event.target;
                    PlayerData.get(player).setLastPosition(last);
                    if (target.teleportEntity(player))
                        sender.sendMessage(Message.msgBackDone.convert(TextFormatting.GREEN));
                    else
                        sender.sendMessage(Message.msgTeleportationFailed.convert(TextFormatting.RED));
                }
            }
            else
                sender.sendMessage(Message.msgLastPosNotFound.convert(TextFormatting.RED));
        }
        else
            sender.sendMessage(Message.msgNotPlayer.convert(TextFormatting.RED));
    }

    @Override
    public String getName()
    {
        return "back";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return Message.msgBackUsage.key;
    }

}
