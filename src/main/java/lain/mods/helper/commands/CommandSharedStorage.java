package lain.mods.helper.commands;

import lain.mods.helper.SharedStorage;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CommandSharedStorage extends GeneralHelperCommand
{

    IChatComponent msgNotPlayer = new ChatComponentTranslation("LH_NotPlayer", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));

    @Override
    public void execute(ICommandSender par1, String[] par2)
    {
        if (par1 instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) par1;
            player.displayGUIChest(SharedStorage.getInventory());
        }
        else
            par1.addChatMessage(msgNotPlayer);
    }

    @Override
    public String getCommandUsage(ICommandSender par1)
    {
        return "LH_SharedStorage_Usage";
    }

    @Override
    public String getName()
    {
        return "sharedstorage";
    }

}
