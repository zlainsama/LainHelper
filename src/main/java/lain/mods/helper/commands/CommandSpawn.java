package lain.mods.helper.commands;

import lain.mods.helper.PlayerData;
import lain.mods.helper.events.PlayerTeleportationEvent;
import lain.mods.helper.utils.PositionData;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommandSpawn extends GeneralHelperCommand
{

    IChatComponent msgNotPlayer = new ChatComponentTranslation("LH_NotPlayer", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));
    IChatComponent msgSpawnDone = new ChatComponentTranslation("LH_SpawnDone", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED));

    @Override
    public void execute(ICommandSender par1, String[] par2)
    {
        if (par1 instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) par1;
            PositionData last = new PositionData(player);
            PositionData target = new PositionData(FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0).provider.getRandomizedSpawnPoint(), 0);
            PlayerTeleportationEvent event = new PlayerTeleportationEvent(player, target);
            if (MinecraftForge.EVENT_BUS.post(event))
            {
                if (event.message != null)
                    par1.addChatMessage(event.message);
            }
            else
            {
                target = event.target;
                PlayerData.get(player).setLastPosition(last);
                target.teleportEntity(player, true);
                par1.addChatMessage(msgSpawnDone);
            }
        }
        else
            par1.addChatMessage(msgNotPlayer);
    }

    @Override
    public String getCommandUsage(ICommandSender par1)
    {
        return "LH_Spawn_Usage";
    }

    @Override
    public String getName()
    {
        return "spawn";
    }

}
