package lain.mods.helper.utils;

public abstract class SafeProcess implements Runnable
{

    public void onFailed()
    {
    }

    public void runSafe()
    {
        try
        {
            run();
        }
        catch (Error ignored)
        {
            onFailed();
        }
    }

}
