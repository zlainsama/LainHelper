package lain.mods.helper.skills.handlers;

import lain.mods.helper.skills.SkillHandler;

public class GenericSkillHandler implements SkillHandler
{

    @Override
    public int getLevelCap()
    {
        return 0;
    }

    @Override
    public int getXPCap(int level)
    {
        return 0;
    }

}
