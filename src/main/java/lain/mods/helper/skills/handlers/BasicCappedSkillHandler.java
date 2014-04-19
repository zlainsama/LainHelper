package lain.mods.helper.skills.handlers;

public class BasicCappedSkillHandler extends GenericSkillHandler
{

    int levelcap;
    int xpcapbasis;
    int xpcapincrement;
    float xpcapincrementmultiplier;

    public BasicCappedSkillHandler(int levelcap, int xpcapbasis, int xpcapincrement, float xpcapincrementmultiplier)
    {
        this.levelcap = levelcap;
        this.xpcapbasis = xpcapbasis;
        this.xpcapincrement = xpcapincrement;
        this.xpcapincrementmultiplier = xpcapincrementmultiplier;
    }

    @Override
    public int getLevelCap()
    {
        return levelcap;
    }

    @Override
    public int getXPCap(int level)
    {
        if (level < 0 || level >= levelcap)
            return 0;
        int xpcap = xpcapbasis;
        while (--level > 0)
            xpcap += xpcapincrement * Math.pow(xpcapincrementmultiplier, level - 1);
        return xpcap;
    }

}
