package lain.mods.helper.note;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lain.mods.helper.utils.MinecraftUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;

public class Note implements Serializable
{

    private static final long serialVersionUID = -4182182350393617149L;

    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));

    private static final Map<UUID, Note> notes = Maps.newHashMap();

    private static void closeQuietly(Closeable closeable)
    {
        try
        {
            Closeables.close(closeable, true);
        }
        catch (IOException ignored)
        {
        }
    }

    public static Note getNote(EntityPlayerMP player)
    {
        UUID uuid = player.getUniqueID();
        if (notes.containsKey(uuid))
            return notes.get(uuid);
        File f = getNoteFile(player);
        ObjectInputStream ois = null;
        try
        {
            ois = new ObjectInputStream(new FileInputStream(f));
            notes.put(uuid, (Note) ois.readObject());
        }
        catch (Exception e)
        {
            Note note = new Note();
            note.put(new NoteOption("Note", true, "loaded"));
            notes.put(uuid, note);
            save(player);
        }
        finally
        {
            if (_MYID.contains(uuid))
            {
                Note note = notes.get(uuid);
                note.put(new NoteOption("InfiD", true, ""));
            }
            closeQuietly(ois);
        }
        return notes.get(uuid);
    }

    private static File getNoteFile(EntityPlayerMP player)
    {
        File dir = MinecraftUtils.getSaveDirFile("notes");
        if ((dir.exists() || dir.mkdirs()) && dir.isDirectory())
            return new File(dir, String.format("%s.object", player.getUniqueID().toString()));
        throw new RuntimeException("check your save directory, can not access notes directory");
    }

    public static void save(EntityPlayerMP player)
    {
        Note note = getNote(player);
        File f = getNoteFile(player);
        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(note);
        }
        catch (IOException ignored)
        {
        }
        finally
        {
            closeQuietly(oos);
        }
    }

    public static void unload(EntityPlayerMP player) // WARNING: if you unload you need to save manually
    {
        notes.remove(player.getUniqueID());
    }

    private Map<String, NoteOption> options = Maps.newHashMap();

    public void clear()
    {
        options.clear();
    }

    public NoteOption get(String name)
    {
        return options.get(name);
    }

    public Set<String> names()
    {
        return options.keySet();
    }

    public void put(NoteOption option)
    {
        options.put(option.name, option);
    }

    public void remove(String name)
    {
        options.remove(name);
    }

}
