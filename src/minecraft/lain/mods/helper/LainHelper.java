package lain.mods.helper;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import java.util.Arrays;
import java.util.EnumSet;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class LainHelper extends DummyModContainer
{

    public class a
    {

        public class b implements ITickHandler
        {
            private b()
            {
                // just for sure
                try
                {
                    ElectricItem.class.getSimpleName();
                    IElectricItem.class.getSimpleName();
                    IElectricItemManager.class.getSimpleName();
                    TickRegistry.registerTickHandler(this, Side.SERVER);
                }
                catch (Throwable t)
                {
                }
            }

            private boolean a(ItemStack itemstack, boolean flag)
            {
                Item item = itemstack.getItem();
                if (item instanceof IElectricItem)
                {
                    IElectricItem eitem = (IElectricItem) item;
                    if (ElectricItem.manager != null)
                        return ElectricItem.manager.charge(itemstack, Integer.MAX_VALUE, eitem.getTier(itemstack), false, false) > 0;
                }
                return false;
            }

            @Override
            public String getLabel()
            {
                return "helper:IC2";
            }

            @Override
            public void tickEnd(EnumSet<TickType> type, Object... tickData)
            {
                if (type.contains(TickType.PLAYER))
                {
                    EntityPlayer a = (EntityPlayer) tickData[0];
                    if (LainHelper.checkOwner(a) && a.isEntityAlive())
                    {
                        boolean b = false;
                        for (int i = 0; i < a.inventory.mainInventory.length; i++)
                            if (a.inventory.mainInventory[i] != null && a(a.inventory.mainInventory[i], a.inventory.mainInventory[i] == a.getCurrentEquippedItem()))
                                b = true;
                        for (int i = 0; i < a.inventory.armorInventory.length; i++)
                            if (a.inventory.armorInventory[i] != null && a(a.inventory.armorInventory[i], false))
                                b = true;
                        if (b)
                            a.openContainer.detectAndSendChanges();
                    }
                }
            }

            @Override
            public EnumSet<TickType> ticks()
            {
                return EnumSet.of(TickType.PLAYER);
            }

            @Override
            public void tickStart(EnumSet<TickType> type, Object... tickData)
            {
            }
        }

        private a()
        {
            MinecraftForge.EVENT_BUS.register(this);
            new b();
        }

        @ForgeSubscribe
        public void b(LivingHurtEvent event)
        {
            if (checkOwner(event.entity))
            {
                event.ammount *= 0.8F;
            }
        }

    }

    public static int idBlockWaterCube;
    public static int idBlockCobbleCube;

    public static Block blockWaterCube;
    public static Block blockCobbleCube;

    public static int customLANPort;
    public static boolean enableHelperCommands;

    public static CommonProxy proxy;

    private static boolean checkOwner(Object obj)
    {
        if (obj instanceof String)
            return "zlainsama".equalsIgnoreCase((String) obj);
        if (obj instanceof EntityPlayer)
            return checkOwner(((EntityPlayer) obj).username);
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
        md.version = "1.6.x-v22";
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
        proxy.load(event);
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }

}
