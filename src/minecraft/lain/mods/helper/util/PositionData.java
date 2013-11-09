package lain.mods.helper.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;

public class PositionData
{

    public int dimension = -999;
    public double x, y, z;
    public float yaw, pitch;

    public PositionData()
    {
    }

    public PositionData(ChunkCoordinates par1)
    {
        this.x = (double) par1.posX + 0.5D;
        this.y = (double) par1.posY + 0.5D;
        this.z = (double) par1.posZ + 0.5D;
    }

    public PositionData(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PositionData(double x, double y, double z, float yaw, float pitch)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public PositionData(Entity par1)
    {
        this.dimension = par1.dimension;
        this.x = par1.posX;
        this.y = par1.posY;
        this.z = par1.posZ;
        this.yaw = par1.rotationYaw;
        this.pitch = par1.rotationPitch;
    }

    public PositionData(int dimension, double x, double y, double z, float yaw, float pitch)
    {
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void readFromNBT(NBTTagCompound par1)
    {
        dimension = par1.getInteger("dimension");
        x = par1.getDouble("x");
        y = par1.getDouble("y");
        z = par1.getDouble("z");
        yaw = par1.getFloat("yaw");
        pitch = par1.getFloat("pitch");
    }

    public void teleportEntity(Entity par1)
    {
        Teleporter.teleport(par1, dimension, x, y, z, yaw, pitch);
    }

    public void writeToNBT(NBTTagCompound par1)
    {
        par1.setInteger("dimension", dimension);
        par1.setDouble("x", x);
        par1.setDouble("y", y);
        par1.setDouble("z", z);
        par1.setFloat("yaw", yaw);
        par1.setFloat("pitch", pitch);
    }

}
