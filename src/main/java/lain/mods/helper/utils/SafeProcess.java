package lain.mods.helper.utils;

public abstract class SafeProcess implements Runnable
{

    public static boolean isClassAccessible(Class<?> c)
    {
        return c != null;
    }

    public static boolean isClassAccessible(String c)
    {
        try
        {
            return isClassAccessible(Class.forName(c));
        }
        catch (ClassNotFoundException notfound)
        {
            return false;
        }
    }

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
