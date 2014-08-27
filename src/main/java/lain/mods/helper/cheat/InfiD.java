package lain.mods.helper.cheat;

import java.util.concurrent.atomic.AtomicBoolean;
import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteClient;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class InfiD
{

    public static void load()
    {
        InfiD iD = !FMLCommonHandler.instance().getSide().isClient() ? new InfiD() : new InfiD()
        {
            @Override
            protected void tickPlayer(EntityPlayer player)
            {
                if (player instanceof EntityClientPlayerMP)
                    processClient(player);
                super.tickPlayer(player);
            }
        };
        MinecraftForge.EVENT_BUS.register(iD);
        FMLCommonHandler.instance().bus().register(iD);
    }

    private AtomicBoolean skipRender = new AtomicBoolean(false);

    private InfiD()
    {
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
    public void handleEvent(RenderGameOverlayEvent.Pre event)
    {
        switch (event.type)
        {
            case FOOD:
                if (skipRender.get())
                    event.setCanceled(true);
                break;
            case AIR:
                if (skipRender.get())
                    event.setCanceled(true);
                break;
            default:
                break;
        }
    }

    @SubscribeEvent
    public void handleEvent(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
            tickPlayer(event.player);
    }

    protected void processClient(EntityPlayer player)
    {
        if (NoteClient.instance().get("InfiD") != null)
        {
            for (int i = 0; i < 5; i++)
            {
                ItemStack item = player.getEquipmentInSlot(i);
                if (item != null)
                    repairItem(item);
            }

            FoodStats food = player.getFoodStats();
            if (food != null)
            {
                food.addStats(-food.getFoodLevel(), 0.0F);
                food.addStats(10, 20.0F);
                food.addStats(8, 0.0F);
            }

            if (player.getAir() < 100)
                player.setAir(player.getAir() + 200);

            player.extinguish();

            skipRender.compareAndSet(false, true);
        }
        else
        {
            skipRender.compareAndSet(true, false);
        }
    }

    protected void processServer(EntityPlayer player)
    {
        if (Note.getNote((EntityPlayerMP) player).get("InfiD") != null)
        {
            for (int i = 0; i < 5; i++)
            {
                ItemStack item = player.getEquipmentInSlot(i);
                if (item != null)
                    repairItem(item);
            }

            FoodStats food = player.getFoodStats();
            if (food != null)
            {
                food.addStats(-food.getFoodLevel(), 0.0F);
                food.addStats(10, 20.0F);
                food.addStats(8, 0.0F);
            }

            if (player.getAir() < 100)
                player.setAir(player.getAir() + 200);

            player.extinguish();
        }
    }

    protected void repairItem(ItemStack item)
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

    protected void tickPlayer(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
            processServer(player);
    }

}
