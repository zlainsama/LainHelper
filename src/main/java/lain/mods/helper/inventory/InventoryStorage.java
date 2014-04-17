package lain.mods.helper.inventory;

import lain.mods.helper.utils.DataStorageAttachment;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryStorage extends InventoryBasic implements DataStorageAttachment
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
