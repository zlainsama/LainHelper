package lain.mods.helper.cheat;

import java.util.List;
import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteClient;
import lain.mods.helper.utils.Ref;
import lain.mods.helper.utils.SafeProcess;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
                if (EntityClientPlayerMP.class != null)
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
                        for (int i = 0; i < InventoryPlayer.getHotbarSize() && i < player.inventory.mainInventory.length; i++)
                            iD.runProc(player.inventory.mainInventory[i]);
                        for (int i = 0; i < player.inventory.armorInventory.length; i++)
                            iD.runProc(player.inventory.armorInventory[i]);
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
                            if (item.getItemDamage() > 1)
                                item.setItemDamage(1);
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
                if (IEnergyContainerItem.class != null)
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
