package lain.mods.helper.skills.handlers;

import lain.mods.helper.skills.SkillHandler;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class GenericSkillHandler implements SkillHandler
{

    static final IChatComponent MISSINGTEXT = new ChatComponentText("MISSINGTEXT");

    @Override
    public IChatComponent getInfo()
    {
        return MISSINGTEXT;
    }

    @Override
    public int getLevelCap()
    {
        return 0;
    }

    @Override
    public IChatComponent getName()
    {
        return MISSINGTEXT;
    }

    @Override
    public int getXPCap(int level)
    {
        return 0;
    }

}
