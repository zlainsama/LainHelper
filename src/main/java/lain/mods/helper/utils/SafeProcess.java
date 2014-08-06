package lain.mods.helper.utils;

public abstract class SafeProcess implements Runnable
{

    public void runSafe()
    {
        try
        {
            run();
        }
        catch (Error ignored)
        {
        }
    }

}
