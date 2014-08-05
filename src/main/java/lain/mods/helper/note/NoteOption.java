package lain.mods.helper.note;

import java.io.Serializable;
import com.google.common.base.Strings;

public class NoteOption implements Serializable
{

    private static final long serialVersionUID = 3782902396170972256L;

    public String name;
    public boolean locked;
    public String value;

    public NoteOption()
    {
        name = "";
        value = "";
    }

    public NoteOption(String name, boolean locked, String value)
    {
        this.name = Strings.nullToEmpty(name);
        this.locked = locked;
        this.value = Strings.nullToEmpty(value);
    }

}
