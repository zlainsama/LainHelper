package lain.mods.helper.note;

import com.google.common.base.Strings;

public class NoteOption
{

    public final String name;
    public final boolean locked;
    public final String value;

    public NoteOption(String name, boolean locked, String value)
    {
        this.name = Strings.nullToEmpty(name);
        this.locked = locked;
        this.value = Strings.nullToEmpty(value);
    }

}
