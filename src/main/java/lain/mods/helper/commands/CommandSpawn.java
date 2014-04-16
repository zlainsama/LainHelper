package lain.mods.helper.commands;

import lain.mods.helper.note.NOTE;
import lain.mods.helper.utils.PositionData;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import cpw.mods.fml.common.FMLCommonHandler;

public class CommandSpawn extends GeneralHelperCommand
{

    IChatComponent msgNotPlayer = new ChatComponentTranslation("LH_NotPlayer", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));
    IChatComponent msgSpawnDone = new ChatComponentTranslation("LH_SpawnDone", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));

    @Override
    public String getCommandName()
    {
        return "spawn";
    }

    @Override
    public String getCommandUsage(ICommandSender par1)
    {
        return "LH_Spawn_Usage";
    }

    @Override
    public void processCommand(ICommandSender par1, String[] par2)
    {
        if (par1 instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) par1;
            NOTE.get(player).setLastPosition(new PositionData(player));
            new PositionData(FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0).provider.getRandomizedSpawnPoint(), 0).teleportEntity(player, true);
            par1.addChatMessage(msgSpawnDone);
        }
        else
            par1.addChatMessage(msgNotPlayer);
    }

}
