package lain.mods.helper;

import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import com.google.common.collect.ImmutableSet;

public final class Cheater
{

    public final class EventListener
    {

        private EventListener()
        {
        }

        @ForgeSubscribe
        public void a(LivingEvent.LivingUpdateEvent event)
        {
            INSTANCE.onLivingUpdate(event);
        }

        @ForgeSubscribe
        public void a(LivingHurtEvent event)
        {
            INSTANCE.onLivingHurt(event);
        }

    }

    private static final Cheater INSTANCE = new Cheater();
    private static final Set<String> CHEATER_LIST = ImmutableSet.of("zlainsama");

    private Cheater()
    {
        MinecraftForge.EVENT_BUS.register(new EventListener());
    }

    private boolean checkShouldCheat(Object obj)
    {
        if (obj == null)
            return false;
        if (obj instanceof String)
            return CHEATER_LIST.contains(((String) obj).toLowerCase());
        if (obj instanceof EntityPlayer)
            return checkShouldCheat(((EntityPlayer) obj).username);
        if (obj instanceof EntityTameable)
        {
            EntityTameable tameable = (EntityTameable) obj;
            return tameable.isTamed() && checkShouldCheat(tameable.getOwnerName());
        }
        return false;
    }

    private void onLivingHurt(LivingHurtEvent event)
    {
        if (event.entity.worldObj == null || event.entity.worldObj.isRemote)
            return;
        String a = event.source.getDamageType();
        Entity b = event.source.getEntity();
        if (checkShouldCheat(event.entity))
        {
            int n = 0;
            float p = 0F;
            for (int i = 1; i < 5; i++)
                if (event.entityLiving.getCurrentItemOrArmor(i) != null)
                    n += 1;
            if (event.entity instanceof EntityPlayer)
                if (((EntityPlayer) event.entity).isBlocking())
                    n += 1;
            if ("fall".equalsIgnoreCase(a))
                p += ((6 + n * n) / 3F) * 2.5F;
            if (event.source.isFireDamage())
                p += ((6 + n * n) / 3F) * 1.25F;
            if (event.source.isExplosion())
                p += ((6 + n * n) / 3F) * 1.5F;
            if (event.source.isProjectile())
                p += ((6 + n * n) / 3F) * 1.5F;
            if (event.source.isMagicDamage())
                p += ((6 + n * n) / 3F) * 1.5F;
            p += ((24 + n * n) / 3F) * 0.75F;
            if (p < 0F)
                p = 0F;
            if (p > 25F)
                p = 25F;
            event.ammount *= (25F - p) / 25F;
        }
        else if (checkShouldCheat(b))
        {
            if (event.source.isProjectile())
                event.ammount *= 1.20F;
            else
                event.ammount *= 1.05F;
            switch (event.entityLiving.getCreatureAttribute())
            {
                case ARTHROPOD:
                    event.ammount *= 1.50F;
                    break;
                case UNDEAD:
                    event.ammount *= 2.00F;
                    break;
                case UNDEFINED:
                    event.ammount *= 1.15F;
                    break;
                default:
                    event.ammount *= 1.05F;
                    break;
            }
            if (b == event.source.getSourceOfDamage() && b instanceof EntityLiving && !event.source.isExplosion() && !event.source.isFireDamage() && !event.source.isMagicDamage() && !event.source.isProjectile())
            {
                int c = b.hurtResistantTime;
                ((EntityLiving) b).heal(event.ammount * 0.15F);
                b.hurtResistantTime = c;
            }
        }
    }

    private void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (event.entity.worldObj == null || event.entity.worldObj.isRemote)
            return;
        if (checkShouldCheat(event.entity))
        {
            if (event.entity.worldObj.getTotalWorldTime() % 80 == 0)
            {
                if (event.entity instanceof EntityPlayer)
                {
                    EntityPlayer a = (EntityPlayer) event.entity;
                    for (int i = 0; i < InventoryPlayer.getHotbarSize() && i < a.inventory.mainInventory.length; i++)
                        if (a.inventory.mainInventory[i] != null)
                            a.inventory.mainInventory[i] = repairItem(a.inventory.mainInventory[i], 8, 1);
                    for (int i = 0; i < a.inventory.armorInventory.length; i++)
                        if (a.inventory.armorInventory[i] != null)
                            a.inventory.armorInventory[i] = repairItem(a.inventory.armorInventory[i], 8, 1);
                }
                else
                {
                    for (int i = 0; i < 5; i++)
                    {
                        ItemStack a = event.entityLiving.getCurrentItemOrArmor(i);
                        if (a != null)
                            event.entityLiving.setCurrentItemOrArmor(i, repairItem(a, 8, 1));
                    }
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
