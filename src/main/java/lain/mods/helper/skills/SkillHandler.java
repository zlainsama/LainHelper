package lain.mods.helper.skills;

import net.minecraft.util.IChatComponent;

public interface SkillHandler
{

    public IChatComponent getInfo();

    int getLevelCap();

    public IChatComponent getName();

    int getXPCap(int level);

}
