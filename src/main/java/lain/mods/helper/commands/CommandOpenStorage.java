package lain.mods.helper.commands;

import lain.mods.helper.SharedStorage;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CommandOpenStorage extends GeneralHelperCommand
{

    IChatComponent msgNotPlayer = new ChatComponentTranslation("LH_NotPlayer", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));

    @Override
    public String getCommandName()
    {
        return "openstorage";
    }

    @Override
    public String getCommandUsage(ICommandSender par1)
    {
        return "LH_OpenStorage_Usage";
    }

    @Override
    public void processCommand(ICommandSender par1, String[] par2)
    {
        if (par1 instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) par1;
            player.displayGUIChest(SharedStorage.getInventory());
        }
        else
            par1.addChatMessage(msgNotPlayer);
    }

}
