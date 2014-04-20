package lain.mods.helper.skills;

import lain.mods.helper.note.NOTE;
import lain.mods.helper.skills.handlers.BasicCappedSkillHandler;
import lain.mods.helper.skills.handlers.GenericSkillHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public enum Skill
{

    MeleeCombat(new BasicCappedSkillHandler(200, 10, 10, 1.05F))
    {

        @Override
        public void onLivingHurt(LivingHurtEvent event)
        {
            if (event.ammount > 0 && (event.source instanceof EntityDamageSource) && !(event.source instanceof EntityDamageSourceIndirect) && event.source.getEntity() instanceof EntityPlayerMP)
            {
                NOTE n = NOTE.get((EntityPlayerMP) event.source.getEntity());
                NBTTagCompound data = n.getSkillData(this);
                int level = getLevel(data);
                addXP(data, (int) (event.ammount * 0.3F));
                event.ammount *= 1.0F + (4.0F * (level / 200));
            }
            if (event.ammount > 0 && (event.source instanceof EntityDamageSource) && !(event.source instanceof EntityDamageSourceIndirect) && event.entity instanceof EntityPlayerMP)
            {
                NOTE n = NOTE.get((EntityPlayerMP) event.entity);
                NBTTagCompound data = n.getSkillData(this);
                int level = getLevel(data);
                if (!event.source.isUnblockable())
                    event.ammount *= 1.0F - (0.8F * (level / 200));
                else if (level >= 100)
                    event.ammount *= 1.0F - (0.4F * (level / 200));
                addXP(data, (int) (event.ammount * 0.5F));
            }
        }

    },
    RangedCombat(new BasicCappedSkillHandler(200, 10, 10, 1.05F))
    {

        @Override
        public void onLivingHurt(LivingHurtEvent event)
        {
            if (event.ammount > 0 && (event.source instanceof EntityDamageSource) && (event.source instanceof EntityDamageSourceIndirect) && event.source.getEntity() instanceof EntityPlayerMP)
            {
                NOTE n = NOTE.get((EntityPlayerMP) event.source.getEntity());
                NBTTagCompound data = n.getSkillData(this);
                int level = getLevel(data);
                addXP(data, (int) (event.ammount * 0.3F));
                event.ammount *= 1.0F + (4.0F * (level / 200));
            }
            if (event.ammount > 0 && (event.source instanceof EntityDamageSource) && (event.source instanceof EntityDamageSourceIndirect) && event.entity instanceof EntityPlayerMP)
            {
                NOTE n = NOTE.get((EntityPlayerMP) event.entity);
                NBTTagCompound data = n.getSkillData(this);
                int level = getLevel(data);
                if (!event.source.isUnblockable())
                    event.ammount *= 1.0F - (0.8F * (level / 200));
                else if (level >= 100)
                    event.ammount *= 1.0F - (0.4F * (level / 200));
                addXP(data, (int) (event.ammount * 0.5F));
            }
        }

    },
    Flexibility(new BasicCappedSkillHandler(200, 10, 10, 1.05F))
    {

        @Override
        public void onLivingHurt(LivingHurtEvent event)
        {
            String type = event.source.getDamageType();
            if (event.ammount > 0 && ("fall".equals(type) || "cactus".equals(type) || "inWall".equals(type) || "anvil".equals(type) || "fallingBlock".equals(type) || event.source.isExplosion()) && event.entity instanceof EntityPlayerMP)
            {
                NOTE n = NOTE.get((EntityPlayerMP) event.entity);
                NBTTagCompound data = n.getSkillData(this);
                int level = getLevel(data);
                event.ammount *= 1.0F - (0.8F * (level / 200));
                addXP(data, (int) (event.ammount * 0.8F));
            }
        }

    };

    public final SkillHandler handler;

    private Skill()
    {
        this(new GenericSkillHandler());
    }

    private Skill(SkillHandler handler)
    {
        this.handler = handler;
    }

    public int addXP(NBTTagCompound data, int xp)
    {
        int prev = getLevel(data);
        setXP(data, getXP(data) + xp);
        updateLevel(data);
        return getLevel(data) - prev;
    }

    public int getLevel(NBTTagCompound data)
    {
        return data.getInteger("level");
    }

    public int getXP(NBTTagCompound data)
    {
        return data.getInteger("xp");
    }

    public void onLivingHurt(LivingHurtEvent event)
    {
    }

    public void setLevel(NBTTagCompound data, int level)
    {
        data.setInteger("level", level);
    }

    public void setXP(NBTTagCompound data, int xp) // You need to updateLevel() or use addXP() instead
    {
        data.setInteger("xp", xp);
    }

    public void updateLevel(NBTTagCompound data)
    {
        int level = getLevel(data);
        int xp = getXP(data);
        int levelcap = handler.getLevelCap();
        if (level + 1 > 0 && level < levelcap)
        {
            int xpcap = handler.getXPCap(level);
            if (xpcap > 0 && xpcap <= xp)
            {
                xp -= xpcap;
                level += 1;
                setXP(data, xp);
                setLevel(data, level);
                updateLevel(data);
            }
            else if (xp < 0)
                setXP(data, 0);
        }
        else
            setXP(data, 0);
    }

}
