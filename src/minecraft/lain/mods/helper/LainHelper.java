package lain.mods.helper;

import java.util.Arrays;
import lain.mods.helper.customport.CustomPort;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class LainHelper extends DummyModContainer
{

    public class a
    {

        private a()
        {
            MinecraftForge.EVENT_BUS.register(this);
        }

        @ForgeSubscribe
        public void b(LivingAttackEvent event)
        {
            if (checkOwner(event.entity))
            {
                String a = event.source.getDamageType();
                if ("fall".equalsIgnoreCase(a) || "thirst".equalsIgnoreCase(a) || "starve".equalsIgnoreCase(a) || "drown".equalsIgnoreCase(a))
                    event.setCanceled(true);
                else if ("wither".equalsIgnoreCase(a) || "electricity".equalsIgnoreCase(a) || "radiation".equalsIgnoreCase(a))
                    event.setCanceled(true);
                else if (event.source.isMagicDamage() || event.source.isFireDamage())
                    event.setCanceled(true);
            }
        }

        @ForgeSubscribe
        public void b(LivingDeathEvent event)
        {
            if (checkOwner(event.entity) && !event.source.canHarmInCreative())
            {
                event.entityLiving.setHealth(1F);
                event.setCanceled(true);
            }
        }

        @ForgeSubscribe
        public void b(PlayerEvent.NameFormat event)
        {
            if (checkOwner(event.username))
            {
                event.displayname = String.format("%s%s%s%s", EnumChatFormatting.RESET, EnumChatFormatting.BLUE, "Lain", EnumChatFormatting.RESET);
            }
        }

    }

    public static int idBlockWaterCube;
    public static int idBlockCobbleCube;

    public static Block blockWaterCube;
    public static Block blockCobbleCube;

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
        md.version = "1.6.x-v15";
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
            CustomPort.port = config.get(Configuration.CATEGORY_GENERAL, "CustomLANPort", 25565).getInt(25565);

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

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }

}
