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
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommandSpawn extends GeneralHelperCommand
{

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException
    {
        if (sender instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            PositionData last = new PositionData(player);
            PositionData target = new PositionData(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0).provider.getRandomizedSpawnPoint(), 0);
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
                if (target.teleportEntity(player, true))
                    sender.sendMessage(Message.msgSpawnDone.convert(TextFormatting.GREEN));
                else
                    sender.sendMessage(Message.msgTeleportationFailed.convert(TextFormatting.RED));
            }
        }
        else
            sender.sendMessage(Message.msgNotPlayer.convert(TextFormatting.RED));
    }

    @Override
    public String getName()
    {
        return "spawn";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return Message.msgSpawnUsage.key;
    }

}
