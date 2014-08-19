package lain.mods.helper.cheat;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteClient;
import lain.mods.helper.utils.Ref;
import lain.mods.helper.utils.SafeProcess;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import com.google.common.collect.ImmutableSet;
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
    }

    List<Probe> iP = Lists.newArrayList();
    List<Proc> sP = Lists.newArrayList();

    AtomicBoolean skipRender = new AtomicBoolean(false);
    Set<String> immuneList = ImmutableSet.of("drown", "starve", "fall", "thorns", "electricity", "radiation", "oxygenSuffocation", "thermal", "heatstroke", "organfailure", "bleedout", "suffocate", "frostbite", "dehydrate");

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
    public void handleEvent(LivingAttackEvent event)
    {
        if (event.entityLiving instanceof EntityPlayerMP)
        {
            if (Note.getNote((EntityPlayerMP) event.entityLiving).get("InfiD") != null)
            {
                if (event.source.isFireDamage() || immuneList.contains(event.source.getDamageType()))
                    event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void handleEvent(LivingHurtEvent event)
    {
        Entity attacker = event.source.getEntity();
        if (event.entityLiving instanceof EntityPlayerMP)
        {
            if (Note.getNote((EntityPlayerMP) event.entityLiving).get("InfiD") != null)
            {
                if (attacker != null && event.entity != attacker)
                    attacker.attackEntityFrom(DamageSource.causeThornsDamage(event.entity), 8.0F);
                if (event.source == DamageSource.outOfWorld && event.entityLiving.posY < -512.0F)
                    ;
                else if (event.ammount > 0)
                    event.ammount *= 0.05F;
                if (immuneList.contains(event.source.getDamageType()))
                    event.setCanceled(true);
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

    void processClient(EntityPlayer player)
    {
        if (NoteClient.instance().get("InfiD") != null)
        {
            for (Probe p : iP)
                p.visit(player, this);

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

    void processServer(EntityPlayer player)
    {
        if (Note.getNote((EntityPlayerMP) player).get("InfiD") != null)
        {
            for (Probe p : iP)
                p.visit(player, this);

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
