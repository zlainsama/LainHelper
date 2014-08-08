package lain.mods.helper.cheat;

import ic2.api.item.ElectricItem;
import ic2.api.item.IBackupElectricItemManager;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import java.util.List;
import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteClient;
import lain.mods.helper.utils.SafeProcess;
import mods.battlegear2.api.core.InventoryPlayerBattle;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cofh.api.energy.IEnergyContainerItem;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class InfiD
{

    interface Probe
    {
        void visit(EntityPlayer player, InfiD iD);
    }

    interface Proc
    {
        void doProc(ItemStack item);
    }

    public static void load()
    {
        InfiD i = new InfiD();
        load_sP(i);
        load_iP(i);
        FMLCommonHandler.instance().bus().register(i);
    }

    private static void load_iP(final InfiD iD)
    {
        new SafeProcess()
        {
            @Override
            public void run()
            {
                iD.addProbe(new Probe()
                {
                    @Override
                    public void visit(EntityPlayer player, InfiD iD)
                    {
                        for (int i = 0; i < InventoryPlayer.getHotbarSize() && i < player.inventory.mainInventory.length; i++)
                            iD.runProc(player.inventory.mainInventory[i]);
                    }
                });
            }
        }.runSafe();
        new SafeProcess()
        {
            @Override
            public void run()
            {
                iD.addProbe(new Probe()
                {
                    @Override
                    public void visit(EntityPlayer player, InfiD iD)
                    {
                        for (int i = 0; i < player.inventory.armorInventory.length; i++)
                            iD.runProc(player.inventory.armorInventory[i]);
                    }
                });
            }
        }.runSafe();
        new SafeProcess()
        {
            @Override
            public void run()
            {
                if (InventoryPlayerBattle.class != null)
                    iD.addProbe(new Probe()
                    {
                        @Override
                        public void visit(EntityPlayer player, InfiD iD)
                        {
                            if (player.inventory instanceof InventoryPlayerBattle)
                            {
                                InventoryPlayerBattle inv = (InventoryPlayerBattle) player.inventory;
                                for (int i = 0; i < inv.extraItems.length; i++)
                                    iD.runProc(inv.extraItems[i]);
                            }
                        }
                    });
            }
        }.runSafe();
    }

    private static void load_sP(final InfiD iD)
    {
        new SafeProcess()
        {
            @Override
            public void run()
            {
                iD.addProc(new Proc()
                {
                    @Override
                    public void doProc(ItemStack item)
                    {
                        if (item.isItemStackDamageable() && item.getItem().isRepairable())
                        {
                            int dmg = item.getItemDamage();
                            if (dmg > 1)
                            {
                                dmg = 1;
                                item.setItemDamage(dmg);
                            }
                        }
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
                    iD.addProc(new Proc()
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
                    iD.addProc(new Proc()
                    {
                        @Override
                        public void doProc(ItemStack item)
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
                    });
            }
        }.runSafe();
        new SafeProcess()
        {
            @Override
            public void run()
            {
                iD.addProc(new Proc()
                {
                    @Override
                    public void doProc(ItemStack item)
                    {
                        if (item.hasTagCompound())
                        {
                            NBTTagCompound data = item.getTagCompound().getCompoundTag("InfiTool");
                            if (data != null)
                            {
                                int dmg = data.getInteger("Damage");
                                if (dmg > 1)
                                {
                                    dmg = 1;
                                    data.setInteger("Damage", dmg);
                                }
                            }
                        }
                    }
                });
            }
        }.runSafe();
    }

    List<Proc> sP = Lists.newArrayList();
    List<Probe> iP = Lists.newArrayList();

    private InfiD()
    {
    }

    void addProbe(Probe p)
    {
        iP.add(p);
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
                    for (Probe p : iP)
                        p.visit(player, this);
                }
            }
            else if (event.player instanceof EntityClientPlayerMP)
            {
                EntityClientPlayerMP player = (EntityClientPlayerMP) event.player;
                Note note = NoteClient.instance();

                if (note.get("InfiD") != null)
                {
                    for (Probe p : iP)
                        p.visit(player, this);
                }
            }
        }
    }

    void runProc(ItemStack item)
    {
        if (item != null)
            for (Proc p : sP)
                p.doProc(item);
    }

}
