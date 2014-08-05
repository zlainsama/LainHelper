package lain.mods.helper.cheat;

import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteClient;
import lain.mods.helper.note.NoteOption;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class AutoRepair
{

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            if (event.player instanceof EntityPlayerMP)
            {
                EntityPlayerMP player = (EntityPlayerMP) event.player;
                Note note = Note.getNote(player);
                NoteOption option = note.get("AutoRepair");
                if (option != null)
                {
                    for (int i = 0; i < InventoryPlayer.getHotbarSize() && i < player.inventory.mainInventory.length; i++)
                        if (player.inventory.mainInventory[i] != null)
                            player.inventory.mainInventory[i] = repairItem(player.inventory.mainInventory[i], Integer.MAX_VALUE, 1);
                    for (int i = 0; i < player.inventory.armorInventory.length; i++)
                        if (player.inventory.armorInventory[i] != null)
                            player.inventory.armorInventory[i] = repairItem(player.inventory.armorInventory[i], Integer.MAX_VALUE, 1);
                }
            }
            else if (event.player instanceof EntityClientPlayerMP)
            {
                EntityClientPlayerMP player = (EntityClientPlayerMP) event.player;
                Note note = NoteClient.instance();
                NoteOption option = note.get("AutoRepair");
                if (option != null)
                {
                    for (int i = 0; i < InventoryPlayer.getHotbarSize() && i < player.inventory.mainInventory.length; i++)
                        if (player.inventory.mainInventory[i] != null)
                            player.inventory.mainInventory[i] = repairItem(player.inventory.mainInventory[i], Integer.MAX_VALUE, 1);
                    for (int i = 0; i < player.inventory.armorInventory.length; i++)
                        if (player.inventory.armorInventory[i] != null)
                            player.inventory.armorInventory[i] = repairItem(player.inventory.armorInventory[i], Integer.MAX_VALUE, 1);
                }
            }
        }
    }

    private ItemStack repairItem(ItemStack item, int amount, int limit)
    {
        if (item != null && item.isItemStackDamageable() && item.getItem().isRepairable())
        {
            int dmg = item.getItemDamage();
            if (dmg > limit)
            {
                dmg = dmg > amount ? dmg - amount : 0;
                if (dmg < limit)
                    dmg = limit;
                item = item.copy();
                item.setItemDamage(dmg);
            }
        }
        return item;
    }

}
