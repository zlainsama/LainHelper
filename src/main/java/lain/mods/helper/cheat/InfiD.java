package lain.mods.helper.cheat;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteClient;
import mekanism.api.energy.IEnergizedItem;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.api.item.IEnergyItem;
import appeng.api.implementations.items.IAEItemPowerStorage;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class InfiD
{

    enum ModCompat
    {

        IC2("IC2"),
        COFH("CoFHCore"),
        UE("UniversalElectricity"),
        AE2("appliedenergistics2"),
        Mekanism("Mekanism");

        public final String modId;
        public final boolean available;

        ModCompat(String par1)
        {
            available = Loader.isModLoaded(modId = par1);
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

    void chargeItem(ItemStack item)
    {
        if (ModCompat.IC2.available)
        {
            if (ElectricItem.manager != null && item.getItem() instanceof IElectricItem)
                ElectricItem.manager.charge(item, Double.MAX_VALUE, ((IElectricItem) item.getItem()).getTier(item), true, false);
        }
        if (ModCompat.COFH.available)
        {
            if (item.getItem() instanceof IEnergyContainerItem)
            {
                int n = Integer.MAX_VALUE;
                while (n > 0)
                {
                    int a = ((IEnergyContainerItem) item.getItem()).receiveEnergy(item, n, false);
                    if (a > 0)
                        n -= a;
                    else
                        break;
                }
            }
        }
        if (ModCompat.UE.available)
        {
            if (item.getItem() instanceof IEnergyItem)
            {
                double n = Double.MAX_VALUE;
                while (n > 0)
                {
                    double a = ((IEnergyItem) item.getItem()).recharge(item, n, true);
                    if (a > 0)
                        n -= a;
                    else
                        break;
                }
            }
        }
        if (ModCompat.AE2.available)
        {
            if (item.getItem() instanceof IAEItemPowerStorage)
            {
                double n = Double.MAX_VALUE;
                while (n > 0)
                {
                    double a = n - ((IAEItemPowerStorage) item.getItem()).injectAEPower(item, n);
                    if (a > 0)
                        n -= a;
                    else
                        break;
                }
            }
        }
        if (ModCompat.UE.available)
        {
            if (item.getItem() instanceof IEnergizedItem)
            {
                IEnergizedItem iei = (IEnergizedItem) item.getItem();
                if (iei.canReceive(item) && iei.getEnergy(item) < iei.getMaxEnergy(item))
                    iei.setEnergy(item, iei.getMaxEnergy(item));
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
            for (int i = 0; i < player.inventory.armorInventory.length; i++)
                if (player.inventory.armorInventory[i] != null)
                    chargeItem(player.inventory.armorInventory[i]);
            for (int i = 0; i < player.inventory.mainInventory.length; i++)
                if (player.inventory.mainInventory[i] != null)
                    chargeItem(player.inventory.mainInventory[i]);
        }
    }

    void processServer(EntityPlayer player)
    {
        if (Note.getNote((EntityPlayerMP) player).get("InfiD") != null)
        {
            for (int i = 0; i < player.inventory.armorInventory.length; i++)
                if (player.inventory.armorInventory[i] != null)
                    chargeItem(player.inventory.armorInventory[i]);
            for (int i = 0; i < player.inventory.mainInventory.length; i++)
                if (player.inventory.mainInventory[i] != null)
                    chargeItem(player.inventory.mainInventory[i]);
        }
    }

    void tickPlayer(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
            processServer(player);
    }

}
