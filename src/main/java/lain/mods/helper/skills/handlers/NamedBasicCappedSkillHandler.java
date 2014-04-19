package lain.mods.helper.skills.handlers;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class NamedBasicCappedSkillHandler extends BasicCappedSkillHandler
{

    String name;

    public NamedBasicCappedSkillHandler(String name, int levelcap, int xpcapbasis, int xpcapincrement, float xpcapincrementmultiplier)
    {
        super(levelcap, xpcapbasis, xpcapincrement, xpcapincrementmultiplier);
        this.name = name;
    }

    @Override
    public IChatComponent getInfo()
    {
        return new ChatComponentTranslation("LH_Skills_" + name + "_Info");
    }

    @Override
    public IChatComponent getName()
    {
        return new ChatComponentTranslation("LH_Skills_" + name + "_Name");
    }

}
