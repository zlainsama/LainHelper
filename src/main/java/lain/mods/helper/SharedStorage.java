package lain.mods.helper;

import java.util.concurrent.TimeUnit;
import lain.mods.helper.utils.DataStorage;
import lain.mods.helper.utils.DataStorageAttachment;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class SharedStorage
{

    private static class InventoryStorage extends InventoryBasic implements DataStorageAttachment
    {

        public InventoryStorage(String title, boolean titleTranslated, int size)
        {
            super(title, titleTranslated, size);
        }

        private void clearInventory()
        {
            for (int i = 0; i < getSizeInventory(); i++)
                setInventorySlotContents(i, null);
        }

        @Override
        public void loadData(NBTTagCompound data)
        {
            clearInventory();
            NBTTagList items = data.getTagList("Items", 10);
            for (int i = 0; i < items.tagCount(); i++)
            {
                NBTTagCompound itemdata = items.getCompoundTagAt(i);
                int slot = itemdata.getByte("Slot") & 0xFF;
                if ((slot >= 0) && (slot < getSizeInventory()))
                    setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(itemdata));
            }
        }

        @Override
        public void saveData(NBTTagCompound data)
        {
            NBTTagList items = new NBTTagList();
            for (int i = 0; i < getSizeInventory(); i++)
            {
                ItemStack item = getStackInSlot(i);
                if (item != null)
                {
                    NBTTagCompound itemdata = new NBTTagCompound();
                    itemdata.setByte("Slot", (byte) i);
                    item.writeToNBT(itemdata);
                    items.appendTag(itemdata);
                }
            }
            data.setTag("Items", items);
        }

    }

    public static IInventory getInventory()
    {
        return caches.getUnchecked(LainHelper.getWorldDataStorage());
    }

    private static final LoadingCache<DataStorage, IInventory> caches = CacheBuilder.newBuilder().expireAfterAccess(10L, TimeUnit.MINUTES).build(new CacheLoader<DataStorage, IInventory>()
    {

        @Override
        public IInventory load(DataStorage store) throws Exception
        {
            InventoryStorage inv = new InventoryStorage("LH_SharedStorage_Title", false, 54);
            store.registerAttachmentObject("SharedStorage", inv);
            return inv;
        }

    });

}
