package lain.mods.helper;

import java.util.EnumSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class SunlightDebuff implements ITickHandler
{

    public static void setup()
    {
        TickRegistry.registerTickHandler(new SunlightDebuff(), Side.SERVER);
    }

    @Override
    public String getLabel()
    {
        return "helper:SunlightDebuff";
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
    }

    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.PLAYER);
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
        if (type.contains(TickType.PLAYER))
        {
            EntityPlayer player = (EntityPlayer) tickData[0];
            if (player.worldObj != null && !player.worldObj.isRemote && player.worldObj.isDaytime())
            {
                float brightness = player.getBrightness(1.0F);
                if (brightness > 0.5F && player.worldObj.getTotalWorldTime() % 50L == 0L && player.worldObj.canBlockSeeTheSky(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)))
                {
                    player.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 0, true));
                    player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 100, 1, false));
                    player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 2, false));
                }
            }
        }
    }

}
