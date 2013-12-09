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

public class Vampirism implements ITickHandler
{

    public static void setup()
    {
        if (LainHelper.sunlightDebuff || LainHelper.vampirePower)
            TickRegistry.registerTickHandler(new Vampirism(), Side.SERVER);
    }

    @Override
    public String getLabel()
    {
        return "helper:Vampirism";
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
            if (player.worldObj != null && !player.worldObj.isRemote && !player.capabilities.isCreativeMode)
            {
                if (player.worldObj.getTotalWorldTime() % 40L == 0L)
                {
                    boolean flag = player.worldObj.isDaytime() && player.getBrightness(1.0F) > 0.5F && player.worldObj.canBlockSeeTheSky(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY + player.getEyeHeight()), MathHelper.floor_double(player.posZ));

                    if (flag)
                    {
                        if (LainHelper.sunlightDebuff)
                        {
                            player.addPotionEffect(new PotionEffect(Potion.blindness.id, 200, 0, true));
                            if (LainHelper.vampirePower)
                            {
                                player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 200, 2, true));
                                player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 2, true));
                                player.addPotionEffect(new PotionEffect(Potion.weakness.id, 200, 2, true));
                            }
                        }
                    }
                    else
                    {
                        if (LainHelper.vampirePower)
                        {
                            player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 100, 0, true));
                        }
                    }
                }
            }
        }
    }

}
