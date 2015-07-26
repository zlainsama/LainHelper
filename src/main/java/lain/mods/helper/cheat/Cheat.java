package lain.mods.helper.cheat;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;
import lain.mods.helper.asm.Plugin;
import lain.mods.helper.network.NetworkManager;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.common.FMLCommonHandler;
import com.google.common.collect.ImmutableSet;

public class Cheat
{

    private static boolean check(Object obj)
    {
        if (obj instanceof EntityPlayerMP)
            return _MYID.contains(((EntityPlayerMP) obj).getUniqueID());
        return false;
    }

    public static final Cheat INSTANCE = FMLCommonHandler.instance().getSide().isClient() ? new CheatClient() : new Cheat();
    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));

    static
    {
        NetworkManager.registerPacket(PacketCheatInfo.class);
    }

    float swimSpeed = 1.22F;
    boolean maintainDepth = true;

    private static Field f_isJumping;

    public boolean getAquaAffinityModifier(EntityLivingBase player, boolean value)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags((EntityPlayerMP) player);
            if ((flags & 0x1) != 0)
            {
                return Enchantment.aquaAffinity.getMaxLevel() > 0;
            }
        }
        return value;
    }

    public int getDepthStriderModifier(Entity player, int value)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags((EntityPlayerMP) player);
            if ((flags & 0x1) != 0)
            {
                return Enchantment.depthStrider.getMaxLevel();
            }
        }
        return value;
    }

    public int getFlags(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
            return check(player) ? 1 : 0;
        return 0;
    }

    public int getFlagsClient()
    {
        return 0;
    }

    public int getRespiration(Entity player, int value)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags((EntityPlayerMP) player);
            if ((flags & 0x1) != 0)
            {
                return Enchantment.respiration.getMaxLevel();
            }
        }
        return value;
    }

    protected boolean isJumping(EntityPlayer player)
    {
        if (f_isJumping == null)
        {
            try
            {
                f_isJumping = EntityLivingBase.class.getDeclaredField(Plugin.isDevelopmentEnvironment ? "isJumping" : "field_70703_bu");
                f_isJumping.setAccessible(true);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        try
        {
            return f_isJumping.getBoolean(player);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void onLivingUpdate(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags(player);
            if ((flags & 0x1) != 0)
            {
                if (player.isEntityAlive())
                {
                    FoodStats food = player.getFoodStats();
                    {
                        food.addStats(-food.getFoodLevel(), 0.0F);
                        food.addStats(10, 20.0F);
                        food.addStats(8, 0.0F);
                    }
                    if (player.isInWater())
                    {
                        player.setAir(300);
                        if (swimSpeed != 1.0F && !player.capabilities.isFlying)
                        {
                            if (player.motionX > -swimSpeed && player.motionX < swimSpeed)
                                player.motionX *= swimSpeed * 0.995F;
                            if (player.motionZ > -swimSpeed && player.motionZ < swimSpeed)
                                player.motionZ *= swimSpeed * 0.995F;
                        }
                        if (maintainDepth)
                        {
                            boolean isJumping = isJumping(player);
                            boolean isSneaking = player.isSneaking();
                            if (!isSneaking && !isJumping && player.isInsideOfMaterial(Material.water))
                                player.motionY = 0.0D;
                            else if (isJumping)
                                player.motionY *= swimSpeed;
                        }
                    }
                }
            }
        }
    }

    public void setFlags(EntityPlayer player, int flags)
    {
    }

    public void setFlagsClient(int flags)
    {
    }

}
