package lain.mods.helper.skills;

import lain.mods.helper.note.NOTE;
import lain.mods.helper.skills.handlers.BasicCappedSkillHandler;
import lain.mods.helper.skills.handlers.GenericSkillHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public enum Skill
{

    MeleeCombat(new BasicCappedSkillHandler(200, 10, 10, 1.05F))
    {

        @Override
        protected void load()
        {
            MinecraftForge.EVENT_BUS.register(this);
        }

        @SubscribeEvent
        public void onLivingHurt(LivingHurtEvent event)
        {
            if (event.ammount > 0 && !(event.source instanceof EntityDamageSourceIndirect) && event.source.getEntity() instanceof EntityPlayerMP)
            {
                NOTE n = NOTE.get((EntityPlayerMP) event.source.getEntity());
                NBTTagCompound data = n.getSkillData(this);
                int level = getLevel(data);
                addXP(data, (int) (event.ammount * 0.3F));
                event.ammount *= 1.0F + (4.0F * (level / 200));
            }
            if (event.ammount > 0 && !(event.source instanceof EntityDamageSourceIndirect) && event.entity instanceof EntityPlayerMP)
            {
                NOTE n = NOTE.get((EntityPlayerMP) event.entity);
                NBTTagCompound data = n.getSkillData(this);
                int level = getLevel(data);
                addXP(data, (int) (event.ammount * 0.1F));
                if (!event.source.isUnblockable())
                    event.ammount *= 1.0F - (0.8F * (level / 200));
                else if (level >= 100)
                    event.ammount *= 1.0F - (0.4F * (level / 200));
            }
        }

        @Override
        protected void unload()
        {
            MinecraftForge.EVENT_BUS.unregister(this);
        }

    },
    RangedCombat(new BasicCappedSkillHandler(200, 10, 10, 1.05F))
    {

        @Override
        protected void load()
        {
            MinecraftForge.EVENT_BUS.register(this);
        }

        @SubscribeEvent
        public void onLivingHurt(LivingHurtEvent event)
        {
            if (event.ammount > 0 && (event.source instanceof EntityDamageSourceIndirect) && event.source.getEntity() instanceof EntityPlayerMP)
            {
                NOTE n = NOTE.get((EntityPlayerMP) event.source.getEntity());
                NBTTagCompound data = n.getSkillData(this);
                int level = getLevel(data);
                addXP(data, (int) (event.ammount * 0.3F));
                event.ammount *= 1.0F + (4.0F * (level / 200));
            }
            if (event.ammount > 0 && (event.source instanceof EntityDamageSourceIndirect) && event.entity instanceof EntityPlayerMP)
            {
                NOTE n = NOTE.get((EntityPlayerMP) event.entity);
                NBTTagCompound data = n.getSkillData(this);
                int level = getLevel(data);
                addXP(data, (int) (event.ammount * 0.1F));
                if (!event.source.isUnblockable())
                    event.ammount *= 1.0F - (0.8F * (level / 200));
                else if (level >= 100)
                    event.ammount *= 1.0F - (0.4F * (level / 200));
            }
        }

        @Override
        protected void unload()
        {
            MinecraftForge.EVENT_BUS.unregister(this);
        }

    };

    public static void setDisabled(FMLServerStoppingEvent event)
    {
        for (Skill skill : values())
            skill.unload();
    }

    public static void setEnabled(FMLServerStartingEvent event)
    {
        for (Skill skill : values())
            skill.load();
    }

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

    protected abstract void load();

    public void setLevel(NBTTagCompound data, int level)
    {
        data.setInteger("level", level);
    }

    public void setXP(NBTTagCompound data, int xp) // You need to updateLevel() or use addXP() instead
    {
        data.setInteger("xp", xp);
    }

    protected abstract void unload();

    public void updateLevel(NBTTagCompound data)
    {
        int level = getLevel(data);
        int xp = getXP(data);
        int levelcap = handler.getLevelCap();
        if (levelcap > 0 && levelcap <= level || (level + 1) <= 0)
            return;
        int xpcap = handler.getXPCap(level);
        if (xpcap > 0 && xpcap <= xp || xp < xpcap)
            return;
        xp -= xpcap;
        level += 1;
        setXP(data, xp);
        setLevel(data, level);
        updateLevel(data);
    }

    public void validateData(NBTTagCompound data)
    {
        updateLevel(data);
        setLevel(data, MathHelper.clamp_int(getLevel(data), 0, handler.getLevelCap()));
        setXP(data, MathHelper.clamp_int(getXP(data), 0, handler.getXPCap(getLevel(data))));
    }

}
