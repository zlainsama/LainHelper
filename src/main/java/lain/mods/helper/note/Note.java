package lain.mods.helper.note;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lain.mods.helper.handlers.PlayerExtraSavedDataHandler;
import lain.mods.helper.utils.DataStorageAttachment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import com.google.common.collect.Maps;

public class Note implements DataStorageAttachment
{

    private static final Map<UUID, WeakReference<Note>> caches = Maps.newHashMap();

    public static Note getNote(EntityPlayerMP p)
    {
        UUID id = p.getUniqueID();
        Note note = caches.get(id) != null ? caches.get(id).get() : null;
        if (note == null)
            caches.put(id, new WeakReference<Note>(note = new Note()));
        PlayerExtraSavedDataHandler.get(p).registerAttachmentObject("Note", note);
        return note;
    }

    private Map<String, NoteOption> options = Maps.newHashMap();
    private boolean dirty = true;

    public void clear()
    {
        options.clear();
        setDirty();
    }

    public NoteOption get(String name)
    {
        return options.get(name);
    }

    public boolean isDirty()
    {
        return dirty;
    }

    @Override
    public void loadData(NBTTagCompound data)
    {
        clear();
        NBTTagList items = data.getTagList("NoteOptions", 10);
        for (int i = 0; i < items.tagCount(); i++)
        {
            NBTTagCompound item = items.getCompoundTagAt(i);
            put(new NoteOption(item.getString("Name"), item.getBoolean("Locked"), item.getString("Value")));
        }
        setDirty();
    }

    public Set<String> names()
    {
        return options.keySet();
    }

    public void put(NoteOption option)
    {
        options.put(option.name, option);
        setDirty();
    }

    public void remove(String name)
    {
        options.remove(name);
        setDirty();
    }

    @Override
    public void saveData(NBTTagCompound data)
    {
        NBTTagList items = new NBTTagList();
        for (NoteOption option : options.values())
        {
            NBTTagCompound item = new NBTTagCompound();
            item.setString("Name", option.name);
            item.setBoolean("Locked", option.locked);
            item.setString("Value", option.value);
            items.appendTag(item);
        }
        data.setTag("NoteOptions", items);
    }

    public boolean setDirty()
    {
        return setDirty(true);
    }

    public boolean setDirty(boolean isDirty)
    {
        boolean prev = dirty;
        dirty = isDirty;
        return prev;
    }

}
