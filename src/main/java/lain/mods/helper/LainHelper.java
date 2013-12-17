package lain.mods.helper;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

public class LainHelper extends DummyModContainer
{

    public class a
    {

        private final Random r;

        private a()
        {
            r = new Random();
            MinecraftForge.EVENT_BUS.register(this);
        }

        public ItemStack b(ItemStack s)
        {
            if (s.isItemStackDamageable() && s.getItem().isRepairable())
            {
                int a = s.getItemDamage();
                if (a > 1)
                {
                    a -= r.nextInt(8) + 1;
                    if (a < 1)
                        a = 1;
                    s.setItemDamage(a);
                }
            }
            return s;
        }

        @ForgeSubscribe
        public void b(LivingEvent.LivingUpdateEvent event)
        {
            if (event.entity.worldObj == null || event.entity.worldObj.isRemote)
                return;
            if (checkOwner(event.entity))
            {
                if (event.entity.ticksExisted % 80 == 0)
                {
                    if (event.entity instanceof EntityPlayer)
                    {
                        EntityPlayer a = (EntityPlayer) event.entity;
                        for (int i = 0; i < InventoryPlayer.getHotbarSize() && i < a.inventory.mainInventory.length; i++)
                            if (a.inventory.mainInventory[i] != null)
                                a.inventory.mainInventory[i] = b(a.inventory.mainInventory[i]);
                        for (int i = 0; i < a.inventory.armorInventory.length; i++)
                            if (a.inventory.armorInventory[i] != null)
                                a.inventory.armorInventory[i] = b(a.inventory.armorInventory[i]);
                    }
                    else
                    {
                        for (int i = 0; i < 5; i++)
                        {
                            ItemStack a = event.entityLiving.getCurrentItemOrArmor(i);
                            if (a != null)
                                event.entityLiving.setCurrentItemOrArmor(i, b(a));
                        }
                    }
                }
            }
        }

        @ForgeSubscribe
        public void b(LivingHurtEvent event)
        {
            if (event.entity.worldObj == null || event.entity.worldObj.isRemote)
                return;
            String a = event.source.getDamageType();
            Entity b = event.source.getEntity();
            if (checkOwner(event.entity))
            {
                int n = 0;
                float p = 0F;
                for (int i = 1; i < 5; i++)
                    if (event.entityLiving.getCurrentItemOrArmor(i) != null)
                        n += 1;
                if (event.entity instanceof EntityPlayer)
                    if (((EntityPlayer) event.entity).isBlocking())
                        n += 1;
                if ("fall".equalsIgnoreCase(a))
                    p += ((6 + n * n) / 3F) * 2.5F;
                if (event.source.isFireDamage())
                    p += ((6 + n * n) / 3F) * 1.25F;
                if (event.source.isExplosion())
                    p += ((6 + n * n) / 3F) * 1.5F;
                if (event.source.isProjectile())
                    p += ((6 + n * n) / 3F) * 1.5F;
                p += ((6 + n * n) / 3F) * 0.75F;
                if (p < 0F)
                    p = 0F;
                if (p > 25F)
                    p = 25F;
                event.ammount *= (25F - p) / 25F;
            }
            else if (checkOwner(b))
            {
                if (event.source.isProjectile())
                    event.ammount *= 1.20F;
                else
                    event.ammount *= 1.05F;
                switch (event.entityLiving.getCreatureAttribute())
                {
                    case ARTHROPOD:
                        event.ammount *= 1.50F;
                        break;
                    case UNDEAD:
                        event.ammount *= 2.00F;
                        break;
                    case UNDEFINED:
                        event.ammount *= 1.15F;
                        break;
                    default:
                        event.ammount *= 1.05F;
                        break;
                }
            }
        }

    }

    public static int idBlockWaterCube;
    public static int idBlockCobbleCube;

    public static Block blockWaterCube;
    public static Block blockCobbleCube;

    public static int customLANPort;
    public static boolean enableHelperCommands;
    public static boolean sunlightDebuff;
    public static boolean vampirePower;

    public static CommonProxy proxy;

    private static boolean checkOwner(Object obj)
    {
        if (obj == null)
            return false;
        if (obj instanceof String)
            return "zlainsama".equalsIgnoreCase((String) obj);
        if (obj instanceof EntityPlayer)
            return checkOwner(((EntityPlayer) obj).username);
        if (obj instanceof EntityTameable)
        {
            EntityTameable tameable = (EntityTameable) obj;
            return tameable.isTamed() && checkOwner(tameable.getOwnerName());
        }
        return false;
    }

    public LainHelper()
    {
        super(new ModMetadata());
        getMetadata();
    }

    @Override
    public ModMetadata getMetadata()
    {
        ModMetadata md = super.getMetadata();
        md.modId = "LainHelper";
        md.name = "LainHelper";
        md.version = "1.6.x-v34";
        md.authorList = Arrays.asList("zlainsama");
        md.autogenerated = false;
        return md;
    }

    @Subscribe
    public void init(FMLPreInitializationEvent event)
    {
        proxy = event.getSide().isClient() ? new ClientProxy() : new CommonProxy();
        try
        {
            Configuration config = new Configuration(event.getSuggestedConfigurationFile());

            idBlockWaterCube = config.getBlock("blockWaterCube", 3761).getInt(3761);
            idBlockCobbleCube = config.getBlock("blockCobbleCube", 3762).getInt(3762);
            customLANPort = config.get(Configuration.CATEGORY_GENERAL, "CustomLANPort", 25565).getInt(25565);
            enableHelperCommands = config.get(Configuration.CATEGORY_GENERAL, "EnableHelperCommands", true).getBoolean(true);
            sunlightDebuff = config.get(Configuration.CATEGORY_GENERAL, "SunlightDebuff", false).getBoolean(false);
            vampirePower = config.get(Configuration.CATEGORY_GENERAL, "VampirePower", false).getBoolean(false);

            config.save();
        }
        catch (Exception e)
        {
            event.getModLog().severe(String.format("ERROR loading config: %s", e.toString()));
        }
    }

    @Subscribe
    public void load(FMLInitializationEvent event)
    {
        new a();
        proxy.load(event);
    }

    @Subscribe
    public void onServerStarting(FMLServerStartingEvent event)
    {
        proxy.onServerStarting(event);
    }

    @Subscribe
    public void onServerStopping(FMLServerStoppingEvent event)
    {
        proxy.onServerStopping(event);
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }

}
