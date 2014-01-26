package lain.mods.helper;

import java.util.Set;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class Cheater
{

    public static final class Living
    {

        private Living()
        {
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
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

    public static final class Protection
    {

        private Protection()
        {
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onHurt(LivingHurtEvent event)
        {
            if (event.entity.worldObj == null || event.entity.worldObj.isRemote)
                return;
            if (checkShouldCheat(event.entity))
            {
                if (event.ammount > 0.1F)
                {
                    event.ammount *= 0.5F;
                    if (!event.source.isUnblockable() || event.source.isMagicDamage())
                        event.ammount *= 0.5;
                    if (event.ammount < 0.1F)
                        event.ammount = 0.1F;
                }
                else if (event.ammount > 0.0F)
                {
                    event.ammount = 0.0F;
                }
            }
        }

    }

    private static final Set<String> list = ImmutableSet.of("zlainsama");

    private static boolean checkShouldCheat(Object obj)
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

    public static void setEnabled()
    {
        MinecraftForge.EVENT_BUS.register(new Living());
        MinecraftForge.EVENT_BUS.register(new Protection());
    }

}
