package lain.mods.helper.ext;

import io.netty.buffer.ByteBuf;

public abstract class ExtendedProperty<T>
{

    private T _value;
    private T _value_last;
    private boolean _dirty;

    public final T get()
    {
        return _value;
    }

    public abstract SyncTag getSyncTag();

    public final boolean isDirty()
    {
        return _dirty;
    }

    public abstract void readFromBuffer(ByteBuf buf);

    public final T set(T value)
    {
        if (_value_last != value)
        {
            _value_last = _value;
            _value = value;
            setDirty();
        }
        return _value_last;
    }

    public final void setClean()
    {
        _dirty = false;
    }

    public final void setDirty()
    {
        _dirty = true;
    }

    public abstract void writeToBuffer(ByteBuf buf);

}
