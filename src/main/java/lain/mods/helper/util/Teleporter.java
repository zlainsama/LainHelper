package lain.mods.helper.util;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class Teleporter
{

    public static Entity teleport(Entity ent, int dimension, double posX, double posY, double posZ, float yaw, float pitch)
    {
        if (ent.worldObj == null || ent.worldObj.isRemote || !(ent.worldObj instanceof WorldServer))
            return null;

        int d0 = ent.dimension;
        int d1 = dimension == -999 ? d0 : dimension;
        boolean worldChanges = d0 != d1;
        MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
        ServerConfigurationManager cm = s.getConfigurationManager();
        WorldServer w0 = (WorldServer) ent.worldObj;
        WorldServer w1 = worldChanges ? s.worldServerForDimension(d1) : w0;
        EntityPlayerMP c = (ent instanceof EntityPlayerMP) ? (EntityPlayerMP) ent : null;

        if (ent.ridingEntity != null)
        {
            ent.ridingEntity.riddenByEntity = null;
            ent.ridingEntity = null;
        }
        if (ent.riddenByEntity != null)
        {
            ent.riddenByEntity.ridingEntity = null;
            ent.riddenByEntity = null;
        }
        if (c != null)
            c.closeScreen();

        w0.updateEntityWithOptionalForce(ent, false);

        if (worldChanges && c != null)
        {
            c.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(d1, (byte) w1.difficultySetting, w1.getWorldInfo().getTerrainType(), w1.getHeight(), c.theItemInWorldManager.getGameType()));
            w0.getPlayerManager().removePlayer(c);
            w0.removePlayerEntityDangerously(c);
            c.isDead = false;
        }

        ent.motionX = ent.motionY = ent.motionZ = 0D;
        ent.fallDistance = 0F;
        ent.setPositionAndRotation(posX, posY, posZ, yaw, pitch);
        if (c != null)
            c.playerNetServerHandler.setPlayerLocation(posX, posY, posZ, yaw, pitch);
        w1.theChunkProviderServer.loadChunk(((int) posX) >> 4, ((int) posZ) >> 4);

        w1.updateEntityWithOptionalForce(ent, false);

        if (worldChanges)
        {
            if (c == null)
            {
                NBTTagCompound data = new NBTTagCompound();
                ent.writeToNBTOptional(data);
                ent.isDead = true;
                ent = EntityList.createEntityFromNBT(data, w1);
                if (ent == null)
                    return null;
            }
            ent.dimension = d1;
            w1.spawnEntityInWorld(ent);
            ent.setWorld(w1);
        }

        w1.updateEntityWithOptionalForce(ent, false);

        ent.motionX = ent.motionY = ent.motionZ = 0D;
        ent.fallDistance = 0F;
        ent.setPositionAndRotation(posX, posY, posZ, yaw, pitch);
        if (c != null)
            c.playerNetServerHandler.setPlayerLocation(posX, posY, posZ, yaw, pitch);
        w1.theChunkProviderServer.loadChunk(((int) posX) >> 4, ((int) posZ) >> 4);

        w1.updateEntityWithOptionalForce(ent, false);

        if (worldChanges && c != null)
        {
            w1.getPlayerManager().addPlayer(c);
            c.theItemInWorldManager.setWorld(w1);
            cm.updateTimeAndWeatherForPlayer(c, w1);
            cm.syncPlayerInventory(c);
            Iterator iterator = c.getActivePotionEffects().iterator();
            while (iterator.hasNext())
            {
                PotionEffect effect = (PotionEffect) iterator.next();
                c.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(c.entityId, effect));
            }
            GameRegistry.onPlayerChangedDimension(c);

            c.addExperienceLevel(0);
            c.setPlayerHealthUpdated();
        }

        return ent;
    }

}
