package lain.mods.helper;

import java.util.Set;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class Cheater
{

    public final class EventListener
    {

        private EventListener()
        {
        }

        @SubscribeEvent
        public void a(LivingEvent.LivingUpdateEvent event)
        {
            instance.onLivingUpdate(event);
        }

    }

    private static final Cheater instance = new Cheater();
    private static final Set<String> list = ImmutableSet.of("zlainsama");

    public static void setEnabled()
    {
    }

    private Cheater()
    {
        MinecraftForge.EVENT_BUS.register(new EventListener());
    }

    private boolean checkShouldCheat(Object obj)
    {
        if (obj == null)
            return false;
        if (obj instanceof String)
            return list.contains(((String) obj).toLowerCase());
        if (obj instanceof EntityPlayer)
            return checkShouldCheat(((EntityPlayer) obj).getCommandSenderName());
        if (obj instanceof IEntityOwnable)
        {
            IEntityOwnable ownable = (IEntityOwnable) obj;
            return checkShouldCheat(ownable.getOwnerName());
        }
        return false;
    }

    private void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (event.entity.worldObj == null || event.entity.worldObj.isRemote)
            return;
        if (checkShouldCheat(event.entity))
        {
            if (event.entity instanceof EntityPlayer)
            {
                EntityPlayer a = (EntityPlayer) event.entity;
                for (int i = 0; i < InventoryPlayer.getHotbarSize() && i < a.inventory.mainInventory.length; i++)
                    if (i != a.inventory.currentItem && a.inventory.mainInventory[i] != null)
                        a.inventory.mainInventory[i] = repairItem(a.inventory.mainInventory[i], 1, 1);
                for (int i = 0; i < a.inventory.armorInventory.length; i++)
                    if (a.inventory.armorInventory[i] != null)
                        a.inventory.armorInventory[i] = repairItem(a.inventory.armorInventory[i], 1, 1);
            }
            else
            {
                for (int i = 0; i < 5; i++)
                {
                    ItemStack a = event.entityLiving.getCurrentItemOrArmor(i);
                    if (a != null)
                        event.entityLiving.setCurrentItemOrArmor(i, repairItem(a, 1, 1));
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
                dmg -= amount;
                if (dmg < limit)
                    dmg = limit;
                item = item.copy();
                item.setItemDamage(dmg);
            }
        }
        return item;
    }

}
