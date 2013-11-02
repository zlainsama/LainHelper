package lain.mods.helper;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "LainHelper", name = "LainHelper", version = "1.6.x-v13")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class LainHelper
{

    public static int idBlockWaterCube;
    public static int idBlockCobbleCube;

    public static Block blockWaterCube;
    public static Block blockCobbleCube;

    @SidedProxy(serverSide = "lain.mods.helper.CommonProxy", clientSide = "lain.mods.helper.ClientProxy")
    public static CommonProxy proxy;

    protected static boolean checkOwner(Object obj)
    {
        if (obj instanceof String)
            return "zlainsama".equalsIgnoreCase((String) obj);
        if (obj instanceof EntityPlayer)
            return checkOwner(((EntityPlayer) obj).username);
        return false;
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event)
    {
        try
        {
            Configuration config = new Configuration(event.getSuggestedConfigurationFile());

            idBlockWaterCube = config.getBlock("blockWaterCube", 3761).getInt(3761);
            idBlockCobbleCube = config.getBlock("blockCobbleCube", 3762).getInt(3762);

            config.save();
        }
        catch (Exception e)
        {
            event.getModLog().severe(String.format("ERROR loading config: %s", e.toString()));
        }
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        proxy.load(event);
    }

}
