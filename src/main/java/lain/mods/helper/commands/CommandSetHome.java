package lain.mods.helper.commands;

import lain.mods.helper.Options;
import lain.mods.helper.PlayerData;
import lain.mods.helper.utils.PositionData;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CommandSetHome extends GeneralHelperCommand
{

    IChatComponent msgNotPlayer = new ChatComponentTranslation("LH_NotPlayer", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));
    IChatComponent msgSetHomeDone = new ChatComponentTranslation("LH_SetHomeDone", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));
    IChatComponent msgOverworldHomeOnly = new ChatComponentTranslation("LH_OverworldHomeOnly", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));

    @Override
    public String getCommandName()
    {
        return "sethome";
    }

    @Override
    public String getCommandUsage(ICommandSender par1)
    {
        return "LH_SetHome_Usage";
    }

    @Override
    public void processCommand(ICommandSender par1, String[] par2)
    {
        if (par1 instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) par1;
            PositionData pos = new PositionData(player)/* .align() */;
            if (!Options.overworldHomeOnly || pos.dimension == 0)
            {
                PlayerData.get(player).setHomePosition(pos);
                par1.addChatMessage(msgSetHomeDone);
            }
            else
                par1.addChatMessage(msgOverworldHomeOnly);
        }
        else
            par1.addChatMessage(msgNotPlayer);
    }

}
