package lain.mods.helper.cheat;

import ic2.api.item.ElectricItem;
import ic2.api.item.IBackupElectricItemManager;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import java.util.List;
import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteClient;
import lain.mods.helper.utils.SafeProcess;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import cofh.api.energy.IEnergyContainerItem;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class InfiD
{

    interface Proc
    {
        void doProc(ItemStack item);
    }

    public static void load()
    {
        final InfiD i = new InfiD();
        new SafeProcess()
        {
            @Override
            public void run()
            {
                i.addProc(new Proc()
                {
                    @Override
                    public void doProc(ItemStack item)
                    {
                        if (item.isItemStackDamageable() && item.getItem().isRepairable())
                            item.setItemDamage(0);
                    }
                });
            }
        }.runSafe();
        new SafeProcess()
        {
            @Override
            public void run()
            {
                if (ElectricItem.class != null && IElectricItemManager.class != null && IBackupElectricItemManager.class != null && IElectricItem.class != null)
                    i.addProc(new Proc()
                    {
                        @Override
                        public void doProc(ItemStack item)
                        {
                            if (ElectricItem.manager != null && item.getItem() instanceof IElectricItem)
                                ElectricItem.manager.charge(item, Double.MAX_VALUE, ((IElectricItem) item.getItem()).getTier(item), true, false);
                        }
                    });
            }
        }.runSafe();
        new SafeProcess()
        {
            @Override
            public void run()
            {
                if (IEnergyContainerItem.class != null)
                    i.addProc(new Proc()
                    {
                        @Override
                        public void doProc(ItemStack item)
                        {
                            if (item.getItem() instanceof IEnergyContainerItem)
                                ((IEnergyContainerItem) item.getItem()).receiveEnergy(item, Integer.MAX_VALUE, false);
                        }
                    });
            }
        }.runSafe();
        FMLCommonHandler.instance().bus().register(i);
    }

    List<Proc> sP = Lists.newArrayList();

    private InfiD()
    {
    }

    void addProc(Proc p)
    {
        sP.add(p);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            if (event.player instanceof EntityPlayerMP)
            {
                EntityPlayerMP player = (EntityPlayerMP) event.player;
                Note note = Note.getNote(player);

                if (note.get("InfiD") != null)
                {
                    for (int i = 0; i < InventoryPlayer.getHotbarSize() && i < player.inventory.mainInventory.length; i++)
                        if (player.inventory.mainInventory[i] != null)
                            for (Proc p : sP)
                                p.doProc(player.inventory.mainInventory[i]);
                    for (int i = 0; i < player.inventory.armorInventory.length; i++)
                        if (player.inventory.armorInventory[i] != null)
                            for (Proc p : sP)
                                p.doProc(player.inventory.armorInventory[i]);
                }
            }
            else if (event.player instanceof EntityClientPlayerMP)
            {
                EntityClientPlayerMP player = (EntityClientPlayerMP) event.player;
                Note note = NoteClient.instance();

                if (note.get("InfiD") != null)
                {
                    for (int i = 0; i < InventoryPlayer.getHotbarSize() && i < player.inventory.mainInventory.length; i++)
                        if (player.inventory.mainInventory[i] != null)
                            for (Proc p : sP)
                                p.doProc(player.inventory.mainInventory[i]);
                    for (int i = 0; i < player.inventory.armorInventory.length; i++)
                        if (player.inventory.armorInventory[i] != null)
                            for (Proc p : sP)
                                p.doProc(player.inventory.armorInventory[i]);
                }
            }
        }
    }

}
