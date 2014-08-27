package lain.mods.helper.cheat;

import java.util.Set;
import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteClient;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class InfiD
{

    enum immuneDamages
    {
        drown,
        wither,
        suffocate,
        oxygenSuffocation,
        radiation;

        static Set<String> c;
        static
        {
            Set<String> tmp = Sets.newHashSet();
            for (immuneDamages e : immuneDamages.values())
                tmp.add(e.type);
            c = ImmutableSet.copyOf(tmp);
        }

        final String type;

        immuneDamages()
        {
            this.type = name();
        }
    }

    enum immunePotions
    {
        moveSlowdown(2),
        digSlowdown(4),
        confusion(9),
        blindness(15),
        hunger(17),
        weakness(18),
        poison(19),
        wither(20),
        radiation(24);

        static Set<Integer> c;
        static
        {
            Set<Integer> tmp = Sets.newHashSet();
            for (immunePotions e : immunePotions.values())
                tmp.add(e.id);
            c = ImmutableSet.copyOf(tmp);
        }

        final int id;

        immunePotions(int id)
        {
            this.id = id;
        }
    }

    public static void load()
    {
        InfiD iD = !FMLCommonHandler.instance().getSide().isClient() ? new InfiD() : new InfiD()
        {
            @Override
            void tickPlayer(EntityPlayer player)
            {
                if (player instanceof EntityClientPlayerMP)
                    processClient(player);
                super.tickPlayer(player);
            }
        };
        MinecraftForge.EVENT_BUS.register(iD);
        FMLCommonHandler.instance().bus().register(iD);
    }

    private InfiD()
    {
    }

    @SubscribeEvent
    public void handleEvent(LivingAttackEvent event)
    {
        if (event.entityLiving instanceof EntityPlayerMP)
        {
            if (Note.getNote((EntityPlayerMP) event.entityLiving).get("InfiD") != null)
            {
                if (immuneDamages.c.contains(event.source.getDamageType()))
                    event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void handleEvent(LivingHurtEvent event)
    {
        if (event.entityLiving instanceof EntityPlayerMP)
        {
            if (Note.getNote((EntityPlayerMP) event.entityLiving).get("InfiD") != null)
            {
                if (event.ammount > 0)
                    event.ammount *= 0.5F;
            }
        }
    }

    @SubscribeEvent
    public void handleEvent(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
            tickPlayer(event.player);
    }

    void processClient(EntityPlayer player)
    {
        if (NoteClient.instance().get("InfiD") != null)
        {
            for (int i = 0; i < 5; i++)
            {
                ItemStack item = player.getEquipmentInSlot(i);
                if (item != null)
                    repairItem(item);
            }

            for (int p : immunePotions.c)
                player.removePotionEffectClient(p);

            player.extinguish();
        }
    }

    void processServer(EntityPlayer player)
    {
        if (Note.getNote((EntityPlayerMP) player).get("InfiD") != null)
        {
            for (int i = 0; i < 5; i++)
            {
                ItemStack item = player.getEquipmentInSlot(i);
                if (item != null)
                    repairItem(item);
            }

            for (int p : immunePotions.c)
                player.removePotionEffect(p);

            player.extinguish();
        }
    }

    void repairItem(ItemStack item)
    {
        if (item.isItemStackDamageable() && item.getItem().isRepairable())
        {
            if (item.getItemDamage() > 0)
                item.setItemDamage(0);
        }
        if (item.hasTagCompound() && item.getTagCompound().hasKey("InfiTool"))
        {
            NBTTagCompound data = item.getTagCompound().getCompoundTag("InfiTool");
            if (!data.hasKey("Energy"))
            {
                if (data.getBoolean("Broken"))
                    data.setBoolean("Broken", false);
                if (data.getInteger("Damage") > 0)
                    data.setInteger("Damage", 0);
                if (item.getItemDamage() > 0) // visual
                    item.setItemDamage(0);
            }
        }
    }

    void tickPlayer(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
            processServer(player);
    }

}
