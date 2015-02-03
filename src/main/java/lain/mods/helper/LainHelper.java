package lain.mods.helper;

import java.util.Arrays;
import lain.mods.helper.network.NetworkManager;
import lain.mods.helper.network.NetworkManagerClient;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.google.common.eventbus.EventBus;

public class LainHelper extends DummyModContainer
{

    public static NetworkManager network;

    public LainHelper()
    {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "LainHelper";
        meta.name = "LainHelper";
        meta.version = "1.8-v1";
        meta.authorList = Arrays.asList("zlainsama");
        meta.description = "";
        meta.credits = "";
        meta.url = "https://github.com/zlainsama/lainhelper";
        meta.updateUrl = "https://github.com/zlainsama/lainhelper/releases";
    }

    @SubscribeEvent
    public void init(FMLPreInitializationEvent event)
    {
        switch (event.getSide())
        {
            case CLIENT:
                network = new NetworkManagerClient();
                break;
            case SERVER:
            default:
                network = new NetworkManager();
                break;
        }
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }

}
