package lain.mods.helper.cheat;

import ic2.api.item.ElectricItem;
import ic2.api.item.IBackupElectricItemManager;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import java.util.List;
import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteClient;
import lain.mods.helper.utils.Ref;
import lain.mods.helper.utils.SafeProcess;
import mekanism.api.energy.IEnergizedItem;
import mods.battlegear2.api.core.InventoryPlayerBattle;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import universalelectricity.api.item.IEnergyItem;
import appeng.api.implementations.items.IAEItemPowerStorage;
import buildcraftAdditions.api.IKineticCapsule;
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
        Ref<InfiD> ref = Ref.newRef();
        load_iD(ref);
        if (ref.get() != null)
        {
            load_iP(ref);
            load_sP(ref);
            FMLCommonHandler.instance().bus().register(ref.get());
            MinecraftForge.EVENT_BUS.register(ref.get());
        }
    }

    private static void load_iD(final Ref<InfiD> ref)
    {
        new SafeProcess()
        {
            @Override
            public void onFailed()
            {
                ref.set(new InfiD());
            }

            @Override
            public void run()
            {
                if (isClassAccessible(EntityClientPlayerMP.class))
                    ref.set(new InfiD()
                    {
                        @Override
                        void tickPlayer(EntityPlayer player)
                        {
                            if (player instanceof EntityClientPlayerMP)
                                processClient(player);
                            super.tickPlayer(player);
                        }
                    });
            }
        }.runSafe();
    }

    private static void load_iP(final Ref<InfiD> ref)
    {
        new SafeProcess()
        {
            @Override
            public void run()
            {
                ref.get().addProbe(new Probe()
                {
                    @Override
                    public void visit(EntityPlayer player, InfiD iD)
                    {
                        for (int i = 0; /* i < InventoryPlayer.getHotbarSize() && */i < player.inventory.mainInventory.length; i++)
                            iD.runProc(player.inventory.mainInventory[i]);
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
                if (isClassAccessible(InventoryPlayerBattle.class))
                    ref.get().addProbe(new Probe()
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

    private static void load_sP(final Ref<InfiD> ref)
    {
        new SafeProcess()
        {
            @Override
            public void run()
            {
                ref.get().addProc(new Proc()
                {
                    @Override
                    public void doProc(ItemStack item)
                    {
                        if (item.isItemStackDamageable() && item.getItem().isRepairable())
                        {
                            if (item.getItemDamage() > 0)
                                item.setItemDamage(0);
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
                ref.get().addProc(new Proc()
                {
                    @Override
                    public void doProc(ItemStack item)
                    {
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
                });
            }
        }.runSafe();
        new SafeProcess()
        {
            @Override
            public void run()
            {
                if (isClassAccessible(ElectricItem.class) && isClassAccessible(IElectricItemManager.class) && isClassAccessible(IBackupElectricItemManager.class) && isClassAccessible(IElectricItem.class))
                    ref.get().addProc(new Proc()
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
                if (isClassAccessible(IEnergyContainerItem.class))
                    ref.get().addProc(new Proc()
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
                if (isClassAccessible(IEnergyItem.class))
                    ref.get().addProc(new Proc()
                    {
                        @Override
                        public void doProc(ItemStack item)
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
                    });
            }
        }.runSafe();
        new SafeProcess()
        {
            @Override
            public void run()
            {
                if (isClassAccessible(IAEItemPowerStorage.class))
                    ref.get().addProc(new Proc()
                    {
                        @Override
                        public void doProc(ItemStack item)
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
                    });
            }
        }.runSafe();
        new SafeProcess()
        {
            @Override
            public void run()
            {
                if (isClassAccessible(IEnergizedItem.class))
                    ref.get().addProc(new Proc()
                    {
                        @Override
                        public void doProc(ItemStack item)
                        {
                            if (item.getItem() instanceof IEnergizedItem)
                            {
                                IEnergizedItem iei = (IEnergizedItem) item.getItem();
                                if (iei.canReceive(item) && iei.getEnergy(item) < iei.getMaxEnergy(item))
                                    iei.setEnergy(item, iei.getMaxEnergy(item));
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
                if (isClassAccessible(IKineticCapsule.class))
                    ref.get().addProc(new Proc()
                    {
                        @Override
                        public void doProc(ItemStack item)
                        {
                            if (item.getItem() instanceof IKineticCapsule)
                            {
                                IKineticCapsule ikc = (IKineticCapsule) item.getItem();
                                if (ikc.getEnergy(item) < ikc.getCapacity())
                                    ikc.setEnergy(item, ikc.getCapacity());
                            }
                        }
                    });
            }
        }.runSafe();
    }

    List<Probe> iP = Lists.newArrayList();
    List<Proc> sP = Lists.newArrayList();

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
            for (Probe p : iP)
                p.visit(player, this);
        }
    }

    void processServer(EntityPlayer player)
    {
        if (Note.getNote((EntityPlayerMP) player).get("InfiD") != null)
        {
            for (Probe p : iP)
                p.visit(player, this);
        }
    }

    void runProc(ItemStack item)
    {
        if (item != null)
            for (Proc p : sP)
                p.doProc(item);
    }

    void tickPlayer(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
            processServer(player);
    }

}
