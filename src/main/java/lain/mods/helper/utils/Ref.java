package lain.mods.helper.utils;

public class Ref<T>
{

    public static <T> Ref<T> newRef()
    {
        return new Ref<T>();
    }

    public static <T> Ref<T> newRef(T T)
    {
        return new Ref<T>(T);
    }

    private T T;

    private Ref()
    {
    }

    private Ref(T T)
    {
        this.T = T;
    }

    public T get()
    {
        return T;
    }

    public T set(T newT)
    {
        T oldT = T;
        T = newT;
        return oldT;
    }

}
