package lain.mods.helper.note;

public class NoteClient extends Note
{

    private static final long serialVersionUID = 6798909125920402070L;

    private static NoteClient INSTANCE;

    public static NoteClient instance()
    {
        if (INSTANCE == null)
            INSTANCE = new NoteClient();
        return INSTANCE;
    }

}
