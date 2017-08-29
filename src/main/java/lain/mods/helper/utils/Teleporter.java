package lain.mods.helper.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class Teleporter extends net.minecraft.world.Teleporter
{

    private static void move(Entity ent, double px, double py, double pz, float yaw, float pitch, double mx, double my, double mz)
    {
        if (ent instanceof EntityPlayerMP)
        {
            EntityPlayerMP mp = (EntityPlayerMP) ent;
            mp.connection.setPlayerLocation(px, py, pz, yaw, pitch);
            if (mx != 0D || my != 0D || mz != 0D)
            {
                mp.motionX = mx;
                mp.motionY = my;
                mp.motionZ = mz;
                mp.connection.sendPacket(new SPacketEntityVelocity(mp));
            }
            mp.fallDistance = 0F;
        }
        else
        {
            ent.setPositionAndRotation(px, py, pz, yaw, pitch);
            ent.motionX = mx;
            ent.motionY = my;
            ent.motionZ = mz;
            ent.fallDistance = 0F;
        }
    }

    public static Entity teleport(Entity ent, int dimension, double px, double py, double pz, float yaw, float pitch, double mx, double my, double mz)
    {
        if (ent.isDead || ent.world.isRemote)
            return null;

        MinecraftServer s = ent.getServer();

        int oldD = ent.dimension;
        WorldServer oldW = s.getWorld(oldD);
        WorldServer newW = s.getWorld(dimension);

        if (newW == null)
            return null;
        else if (newW == oldW)
        {
            move(ent, px, py, pz, yaw, pitch, mx, my, mz);
            return ent;
        }

        if (ent instanceof EntityPlayerMP)
        {
            EntityPlayerMP p = (EntityPlayerMP) ent;
            s.getPlayerList().transferPlayerToDimension(p, dimension, new Teleporter(newW, px, py, pz));
            p.addExperienceLevel(0);
            if (oldD == 1)
            {
                newW.spawnEntity(p);
                newW.updateEntityWithOptionalForce(p, false);
            }
        }
        else
        {
            NBTTagCompound data = new NBTTagCompound();
            ent.writeToNBTAtomically(data);
            oldW.removeEntity(ent);
            ent = EntityList.createEntityFromNBT(data, newW);
            if (ent != null)
            {
                ent.setLocationAndAngles(px, py, pz, yaw, pitch);
                ent.forceSpawn = true;
                newW.spawnEntity(ent);
                ent.forceSpawn = false;
            }
        }

        if (ent != null)
            move(ent, px, py, pz, yaw, pitch, mx, my, mz);

        return ent;
    }

    final WorldServer world;
    final double x;
    final double y;
    final double z;

    private Teleporter(WorldServer world, double x, double y, double z)
    {
        super(world);

        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean makePortal(Entity ent)
    {
        return true;
    }

    @Override
    public boolean placeInExistingPortal(Entity ent, float yaw)
    {
        return true;
    }

    @Override
    public void placeInPortal(Entity ent, float yaw)
    {
        world.getBlockState(new BlockPos(x, y, z));
        ent.setPosition(x, y, z);
        ent.motionX = ent.motionY = ent.motionZ = 0D;
        ent.fallDistance = 0F;
    }

    @Override
    public void removeStalePortalLocations(long worldTime)
    {
    }

}
