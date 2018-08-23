package lain.mods.helper.cheat;

import lain.mods.helper.LainHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CheatCapsProvider implements INBTSerializable<NBTTagCompound>, ICapabilityProvider
{

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayer)
            event.addCapability(new ResourceLocation(LainHelper.MODID, "cheats"), new CheatCapsProvider(event.getObject()));
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event)
    {
        try
        {
            event.getEntityPlayer().getCapability(CheatCaps.CAPABILITY_CHEAT, null).deserializeNBT(event.getOriginal().getCapability(CheatCaps.CAPABILITY_CHEAT, null).serializeNBT());
        }
        catch (Throwable ignored)
        {
        }
    }

    private final CheatCaps cheats;

    private CheatCapsProvider(Entity owner)
    {
        this.cheats = new CheatCaps(owner);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        cheats.deserializeNBT(nbt);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        return capability == CheatCaps.CAPABILITY_CHEAT ? (T) cheats : null;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CheatCaps.CAPABILITY_CHEAT;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        return cheats.serializeNBT();
    }

}
