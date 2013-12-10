package lain.mods.helper.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;

public class InventoryUtils
{

    private static boolean areItemStacksEqualItem(ItemStack a, ItemStack b)
    {
        return a.itemID != b.itemID ? false : (a.getItemDamage() != b.getItemDamage() ? false : (a.stackSize > a.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(a, b)));
    }

    public static boolean canExtractItemFromInventory(IInventory inventory, ItemStack itemstack, int slot, int side)
    {
        return !(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canExtractItem(slot, itemstack, side);
    }

    public static boolean canInsertItemToInventory(IInventory inventory, ItemStack itemstack, int slot, int side)
    {
        return !inventory.isItemValidForSlot(slot, itemstack) ? false : !(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canInsertItem(slot, itemstack, side);
    }

    public static ItemStack insertStack(IInventory inventory, ItemStack itemstack, int side)
    {
        if (inventory instanceof TileEntityChest)
        {
            TileEntityChest chest = (TileEntityChest) inventory;
            Block block = chest.getBlockType();
            if (block instanceof BlockChest)
                inventory = ((BlockChest) block).getInventory(chest.worldObj, chest.xCoord, chest.yCoord, chest.zCoord);
        }

        if (inventory == null)
            return itemstack;

        if (inventory instanceof ISidedInventory && side > -1)
        {
            ISidedInventory sided = (ISidedInventory) inventory;
            int[] a = sided.getAccessibleSlotsFromSide(side);
            for (int i = 0; i < a.length && itemstack != null && itemstack.stackSize > 0; i++)
                itemstack = insertStack0(inventory, itemstack, a[i], side);
        }
        else
        {
            int n = inventory.getSizeInventory();
            for (int s = 0; s < n && itemstack != null && itemstack.stackSize > 0; s++)
                itemstack = insertStack0(inventory, itemstack, s, side);
        }

        if (itemstack != null && itemstack.stackSize == 0)
            itemstack = null;

        return itemstack;
    }

    private static ItemStack insertStack0(IInventory inventory, ItemStack itemstack, int slot, int side)
    {
        ItemStack stack = inventory.getStackInSlot(slot);
        if (canInsertItemToInventory(inventory, itemstack, slot, side))
        {
            boolean flag = false;
            if (stack == null)
            {
                int max = Math.min(itemstack.getMaxStackSize(), inventory.getInventoryStackLimit());
                if (max >= itemstack.stackSize)
                {
                    inventory.setInventorySlotContents(slot, itemstack);
                    itemstack = null;
                }
                else
                {
                    inventory.setInventorySlotContents(slot, itemstack.splitStack(max));
                }
                flag = true;
            }
            else if (areItemStacksEqualItem(stack, itemstack))
            {
                int max = Math.min(itemstack.getMaxStackSize(), inventory.getInventoryStackLimit());
                if (max > stack.stackSize)
                {
                    int n = Math.min(itemstack.stackSize, max - stack.stackSize);
                    itemstack.stackSize -= n;
                    stack.stackSize += n;
                    flag = n > 0;
                }
            }

            if (flag)
                inventory.onInventoryChanged();
        }
        return itemstack;
    }

}
