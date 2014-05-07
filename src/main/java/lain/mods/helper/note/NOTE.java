package lain.mods.helper.note;

import java.util.UUID;
import lain.mods.helper.ModAttributes;
import lain.mods.helper.utils.PositionData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public final class NOTE
{

    private static final String _ID = "92c98451-a0c6-4899-bce6-c5cc0f75e447";
    private static final UUID _MYID = UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49");
    private static final UUID _SAID = UUID.fromString("5f2cab81-58e7-43f0-9ae0-ba6a58e0ae20");

    private static NBTTagCompound _GetOrCreateCompound(NBTTagCompound base, String name)
    {
        if (!base.hasKey(name))
            base.setTag(name, new NBTTagCompound());
        return base.getCompoundTag(name);
    }

    private static NBTTagCompound _GetPlayerPersistedData(EntityPlayer p)
    {
        return _GetOrCreateCompound(p.getEntityData(), EntityPlayer.PERSISTED_NBT_TAG);
    }

    private static NBTTagCompound _GetRawData(EntityPlayer p)
    {
        return _GetOrCreateCompound(_GetPlayerPersistedData(p), _ID);
    }

    public static NOTE get(EntityPlayer p)
    {
        return new NOTE(_GetRawData(p));
    }

    private final NBTTagCompound d;

    private NOTE(NBTTagCompound data)
    {
        d = data;
    }

    public boolean allowCheat()
    {
        return d.getBoolean("allowCheat");
    }

    public void applySpecialAttributes(EntityPlayer player)
    {
        if (_MYID.equals(player.getUniqueID()))
        {
            IAttributeInstance ai = player.getEntityAttribute(ModAttributes.damageReduction);
            if (ai instanceof ModifiableAttributeInstance && ai.getModifier(_SAID) == null)
                ai.applyModifier(new AttributeModifier(_SAID, _ID, 0.2D, 0));
            ai = player.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            if (ai instanceof ModifiableAttributeInstance)
            {
                double base = ((ModifiableAttributeInstance) ai).getBaseValue();
                AttributeModifier mod = ai.getModifier(_SAID);
                float maxhealth = player.getMaxHealth();
                if (mod != null && (mod.getAmount() != base || mod.getOperation() != 0))
                {
                    ai.removeModifier(mod);
                    mod = ai.getModifier(_SAID);
                    player.heal(player.getMaxHealth() - maxhealth);
                    maxhealth = player.getMaxHealth();
                }
                if (mod == null)
                {
                    ai.applyModifier(new AttributeModifier(_SAID, _ID, base, 0));
                    player.heal(player.getMaxHealth() - maxhealth);
                }
            }
        }
    }

    public PositionData getHomePosition()
    {
        if (d.hasKey("homePosition"))
        {
            PositionData loc = new PositionData();
            loc.readFromNBT(d.getCompoundTag("homePosition"));
            return loc;
        }
        return null;
    }

    public PositionData getLastPosition()
    {
        if (d.hasKey("lastPosition"))
        {
            PositionData loc = new PositionData();
            loc.readFromNBT(d.getCompoundTag("lastPosition"));
            return loc;
        }
        return null;
    }

    public void setHomePosition(PositionData pos)
    {
        NBTTagCompound data = new NBTTagCompound();
        pos.writeToNBT(data);
        d.setTag("homePosition", data);
    }

    public void setLastPosition(PositionData pos)
    {
        NBTTagCompound data = new NBTTagCompound();
        pos.writeToNBT(data);
        d.setTag("lastPosition", data);
    }

}
