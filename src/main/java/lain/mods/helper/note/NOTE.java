package lain.mods.helper.note;

import java.util.Map;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.NameFormat;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public abstract class NOTE
{

    public static class Handler
    {

        private Handler()
        {
        }

        @SubscribeEvent
        public void onDeath(LivingDeathEvent event)
        {
            if (event.entity.worldObj == null || event.entity.worldObj.isRemote)
                return;
            if (!(event.entity instanceof EntityPlayer))
                return;
            NOTE N = NOTES.get(((EntityPlayer) event.entity).getCommandSenderName().toLowerCase());
            if (N == null)
                return;
            N.onDeath(event);
        }

        @SubscribeEvent
        public void onLivingHurt(LivingHurtEvent event)
        {
            if (event.entity.worldObj == null || event.entity.worldObj.isRemote)
                return;
            if (event.source.getEntity() instanceof EntityPlayer)
            {
                NOTE N = NOTES.get(((EntityPlayer) event.source.getEntity()).getCommandSenderName().toLowerCase());
                if (N != null)
                    N.onDealDamage(event);
            }
            if (!(event.entity instanceof EntityPlayer))
                return;
            NOTE N = NOTES.get(((EntityPlayer) event.entity).getCommandSenderName().toLowerCase());
            if (N == null)
                return;
            N.onTakeDamage(event);
        }

        @SubscribeEvent
        public void onLivingSetAttackTarget(LivingSetAttackTargetEvent event)
        {
            if (event.entity.worldObj == null || event.entity.worldObj.isRemote)
                return;
            if (!(event.target instanceof EntityPlayer))
                return;
            NOTE N = NOTES.get(((EntityPlayer) event.target).getCommandSenderName().toLowerCase());
            if (N == null)
                return;
            N.onTargeted(event);
        }

        @SubscribeEvent
        public void onNameFormat(NameFormat event)
        {
            if (!(event.entity instanceof EntityPlayer))
                return;
            NOTE N = NOTES.get(((EntityPlayer) event.entity).getCommandSenderName().toLowerCase());
            if (N == null)
                return;
            N.onNameFormat(event);
        }

        @SubscribeEvent
        public void onPlayerTick(PlayerTickEvent event)
        {
            if (!event.side.isServer())
                return;
            NOTE N = NOTES.get(event.player.getCommandSenderName().toLowerCase());
            if (N == null)
                return;
            N.onPlayerTick(event);
        }
    }

    private static final Map<String, NOTE> NOTES = Maps.newHashMap();
    private static Handler handler;

    public static void load()
    {
        if (handler == null)
        {
            new NOTE("zlainsama")
            {

                @Override
                public void onDealDamage(LivingHurtEvent event)
                {
                    if (event.ammount > 0.5F && event.entity instanceof IMob)
                    {
                        if (event.ammount > 2.0F)
                            event.ammount *= 1.5F;
                        else
                            event.ammount = 2.0F;
                    }
                }

                @Override
                public void onDeath(LivingDeathEvent event)
                {
                }

                @Override
                public void onNameFormat(NameFormat event)
                {
                    event.displayname = "Lain";
                }

                @Override
                public void onPlayerTick(PlayerTickEvent event)
                {
                    if (event.phase == Phase.END)
                    {
                        for (int i = 0; i < event.player.inventory.armorInventory.length; i++)
                            event.player.inventory.armorInventory[i] = repairItem(event.player.inventory.armorInventory[i], Integer.MAX_VALUE, 0);
                        for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++)
                            if (i != event.player.inventory.currentItem)
                                event.player.inventory.mainInventory[i] = repairItem(event.player.inventory.mainInventory[i], Integer.MAX_VALUE, 1);
                        if (event.player.isPotionActive(Potion.hunger.getId()))
                            event.player.removePotionEffect(Potion.hunger.getId());
                        if (event.player.getFoodStats().getFoodLevel() < 10)
                            event.player.getFoodStats().addStats(1, 1.0F);
                    }
                }

                @Override
                public void onTakeDamage(LivingHurtEvent event)
                {
                    if (event.ammount > 1.0F)
                        event.ammount = 1.0F;
                    else if (event.ammount > 0.2F)
                        event.ammount = 0.2F;
                }

                @Override
                public void onTargeted(LivingSetAttackTargetEvent event)
                {
                    if (event.entity instanceof EntityZombie)
                        ((EntityZombie) event.entity).setAttackTarget(null);
                }

            }.setEnabled();

            handler = new Handler();
            MinecraftForge.EVENT_BUS.register(handler);
            FMLCommonHandler.instance().bus().register(handler);
        }
    }

    public static ItemStack repairItem(ItemStack item, int amount, int limit)
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

    private final String name;

    protected NOTE(String name)
    {
        this.name = name.toLowerCase();
    }

    public abstract void onDealDamage(LivingHurtEvent event);

    public abstract void onDeath(LivingDeathEvent event);

    public abstract void onNameFormat(NameFormat event);

    public abstract void onPlayerTick(PlayerTickEvent event);

    public abstract void onTakeDamage(LivingHurtEvent event);

    public abstract void onTargeted(LivingSetAttackTargetEvent event);

    public final void setDisabled()
    {
        if (NOTES.get(name) == this)
            NOTES.remove(name);
    }

    public final void setEnabled()
    {
        if (!NOTES.containsKey(name) || handler == null)
            NOTES.put(name, this);
    }

}
