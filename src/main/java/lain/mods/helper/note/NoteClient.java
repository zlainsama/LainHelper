package lain.mods.helper.note;

public class NoteClient extends Note
{

    private static NoteClient INSTANCE;

    public static NoteClient instance()
    {
        if (INSTANCE == null)
            INSTANCE = new NoteClient();
        return INSTANCE;
    }

}
