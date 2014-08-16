package lain.mods.helper.cheat;

import java.util.concurrent.atomic.AtomicBoolean;
import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteClient;
import lain.mods.helper.utils.Ref;
import lain.mods.helper.utils.SafeProcess;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class InfiD
{

    class BonusDamage extends EntityDamageSource
    {

        public BonusDamage(Entity source)
        {
            super("generic", source);
            setDamageBypassesArmor();
            setMagicDamage();
        }

        @Override
        public IChatComponent func_151519_b(EntityLivingBase entity)
        {
            return new ChatComponentTranslation("death.attack.indirectMagic", entity.func_145748_c_(), damageSourceEntity.func_145748_c_());
        }

        @Override
        public boolean isDifficultyScaled()
        {
            return false;
        }

    }

    public static void load()
    {
        Ref<InfiD> ref = Ref.newRef();
        load_iD(ref);
        if (ref.get() != null)
        {
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

    AtomicBoolean renderFoodBar = new AtomicBoolean(true);

    private InfiD()
    {
    }

    @SubscribeEvent
    public void handleEvent(LivingHurtEvent event)
    {
        Entity attacker = event.source.getEntity();
        String type = event.source.getDamageType();
        if (attacker instanceof EntityPlayerMP)
        {
            if (Note.getNote((EntityPlayerMP) attacker).get("InfiD") != null)
            {
                if (event.entity != attacker && !(event.source instanceof BonusDamage))
                {
                    int t = event.entity.hurtResistantTime;
                    event.entity.hurtResistantTime = 0;
                    event.entity.attackEntityFrom(new BonusDamage(attacker), event.ammount * 0.5F);
                    event.entity.hurtResistantTime = t;
                }
            }
        }
        if (event.entityLiving instanceof EntityPlayerMP)
        {
            if (Note.getNote((EntityPlayerMP) event.entityLiving).get("InfiD") != null)
            {
                if (attacker != null && event.entity != attacker)
                {
                    int t = attacker.hurtResistantTime;
                    attacker.hurtResistantTime = 0;
                    attacker.attackEntityFrom(DamageSource.causeThornsDamage(event.entity), 2.0F);
                    attacker.hurtResistantTime = t;
                }
                if (!event.source.isDamageAbsolute())
                {
                    if (event.ammount > 0)
                        event.ammount *= 0.1F;
                }
            }
        }
    }

    @SubscribeEvent
    public void handleEvent(RenderGameOverlayEvent.Pre event)
    {
        switch (event.type)
        {
            case FOOD:
                if (!renderFoodBar.get())
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
            FoodStats food = player.getFoodStats();
            if (food != null)
            {
                food.addStats(-food.getFoodLevel(), 0.0F);
                food.addStats(10, 20.0F);
                food.addStats(8, 0.0F);
            }

            if (player.isBurning())
                player.extinguish();

            renderFoodBar.compareAndSet(true, false);
        }
        else
        {
            renderFoodBar.compareAndSet(false, true);
        }
    }

    void processServer(EntityPlayer player)
    {
        if (Note.getNote((EntityPlayerMP) player).get("InfiD") != null)
        {
            FoodStats food = player.getFoodStats();
            if (food != null)
            {
                food.addStats(-food.getFoodLevel(), 0.0F);
                food.addStats(10, 20.0F);
                food.addStats(8, 0.0F);
            }

            if (player.isBurning())
                player.extinguish();
        }
    }

    void tickPlayer(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
            processServer(player);
    }

}
