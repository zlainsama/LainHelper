package lain.mods.helper;

import lain.mods.helper.commands.GeneralHelperCommand;
import lain.mods.helper.handlers.MapSaveHandler;
import lain.mods.helper.inventory.InventoryStorage;
import lain.mods.helper.utils.DataStorage;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class SharedStorage
{

    public static DataStorage storage;
    public static InventoryStorage inventory;

    public static ICommand createCommandOpenStorage()
    {
        return new GeneralHelperCommand()
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
                if (inventory == null)
                    inventory = new InventoryStorage("LH_OpenStorage_Title", false, 54);
                if (storage != null)
                    storage.registerAttachmentObject("Inventory", inventory);
                if (inventory == null)
                    return;
                if (par1 instanceof EntityPlayerMP)
                {
                    EntityPlayerMP player = (EntityPlayerMP) par1;
                    MapSaveHandler.checkAndLoad();
                    player.displayGUIChest(inventory);
                }
                else
                    par1.addChatMessage(msgNotPlayer);
            }

        };
    }

}
