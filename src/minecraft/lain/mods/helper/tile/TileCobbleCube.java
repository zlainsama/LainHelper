package lain.mods.helper.tile;

import java.util.Arrays;
import lain.mods.helper.tile.base.BlockCubeBase;
import lain.mods.helper.tile.base.IActivatableCubeTile;
import lain.mods.helper.tile.base.ISpecialCubeTile;
import lain.mods.helper.util.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class TileCobbleCube extends TileEntity implements ISidedInventory, ISpecialCubeTile, IActivatableCubeTile
{

    private static final ItemStack ITEMTODISPLAY = new ItemStack(Block.cobblestone);

    private final ItemStack[] cobbles = new ItemStack[1];
    private final int[] slots = { 0 };
    private int timer = 0;

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j)
    {
        if (j == ForgeDirection.UNKNOWN.ordinal())
            return true;
        for (ForgeDirection dir : BlockCubeBase.VALID_DIRECTIONS)
            if (j == dir.ordinal())
                return true;
        return false;
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j)
    {
        return false;
    }

    @Override
    public void closeChest()
    {
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (i < 0 || i >= cobbles.length)
            return null;
        if (cobbles[i] == null)
            return null;
        ItemStack stack;
        if (cobbles[i].stackSize <= j)
        {
            stack = cobbles[i];
            cobbles[i] = null;
        }
        else
        {
            stack = cobbles[i].splitStack(j);
            if (cobbles[i].stackSize == 0)
                cobbles[i] = null;
        }
        return stack;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        return slots;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public String getInvName()
    {
        return "";
    }

    @Override
    public ItemStack getItemToDisplayOnTop()
    {
        return ITEMTODISPLAY;
    }

    @Override
    public int getSizeInventory()
    {
        return cobbles.length;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        if (i < 0 || i >= cobbles.length)
            return null;
        return cobbles[i];
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (i < 0 || i >= cobbles.length)
            return null;
        ItemStack stack = cobbles[i];
        cobbles[i] = null;
        return stack;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return itemstack != null && itemstack.itemID == Block.cobblestone.blockID;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public boolean onActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        boolean flag = false;
        for (int i = 0; i < cobbles.length; i++)
        {
            if (cobbles[i] != null)
            {
                if (!player.inventory.addItemStackToInventory(cobbles[i]) || cobbles[i].stackSize > 0)
                    player.dropPlayerItem(cobbles[i]);
                cobbles[i] = null;
                flag = true;
            }
        }
        if (flag)
            player.inventoryContainer.detectAndSendChanges();
        return true;
    }

    @Override
    public void openChest()
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
        Arrays.fill(cobbles, null);

        timer = par1NBTTagCompound.getInteger("Timer");

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < cobbles.length)
                cobbles[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        if (i < 0 || i >= cobbles.length)
            return;
        cobbles[i] = itemstack;
    }

    @Override
    public void updateEntity()
    {
        if (worldObj.isRemote)
            return;

        if (++timer >= 30)
        {
            timer = 0;

            ItemStack stack = new ItemStack(Block.cobblestone);
            for (int i = 0; i < cobbles.length && stack != null && stack.stackSize > 0; i++)
            {
                if (cobbles[i] == null || cobbles[i].itemID != Block.cobblestone.blockID)
                {
                    cobbles[i] = stack;
                    stack = null;
                }
                else
                {
                    int max = Math.min(stack.getMaxStackSize(), getInventoryStackLimit());
                    if (max > cobbles[i].stackSize)
                    {
                        int n = Math.min(stack.stackSize, max - cobbles[i].stackSize);
                        stack.stackSize -= n;
                        cobbles[i].stackSize += n;
                    }
                }
            }

            for (int i = 0; i < cobbles.length; i++)
            {
                for (ForgeDirection to : BlockCubeBase.VALID_DIRECTIONS)
                {
                    if (cobbles[i] == null || cobbles[i].stackSize == 0)
                        break;
                    TileEntity tile = worldObj.getBlockTileEntity(xCoord + to.offsetX, yCoord + to.offsetY, zCoord + to.offsetZ);
                    if (tile != null && tile instanceof IInventory)
                        cobbles[i] = InventoryUtils.insertStack((IInventory) tile, cobbles[i], to.getOpposite().ordinal());
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < cobbles.length; ++i)
        {
            if (cobbles[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                cobbles[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);
        par1NBTTagCompound.setInteger("Timer", timer);
    }

}
