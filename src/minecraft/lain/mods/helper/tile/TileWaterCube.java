package lain.mods.helper.tile;

import lain.mods.helper.tile.base.BlockCubeBase;
import lain.mods.helper.tile.base.IActivatableCubeTile;
import lain.mods.helper.tile.base.ISpecialCubeTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.TileFluidHandler;

public class TileWaterCube extends TileFluidHandler implements ISpecialCubeTile, IActivatableCubeTile, ISidedInventory
{

    private static final ItemStack ITEMTODISPLAY = new ItemStack(Item.bucketWater);

    private static final int capacity = (int) (FluidContainerRegistry.BUCKET_VOLUME * 4.0);
    private static final int tickGain = (int) (FluidContainerRegistry.BUCKET_VOLUME * 0.2);
    private static final int tickFlow = (int) (FluidContainerRegistry.BUCKET_VOLUME * 0.1);

    private ItemStack item;
    private final int[] slots = { 0 };
    private final int[] noSlot = {};

    public TileWaterCube()
    {
        tank.setCapacity(capacity);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return getAccessibleSlotsFromSide(from.ordinal()) != noSlot;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j)
    {
        if (i != 0)
            return false;
        if (getAccessibleSlotsFromSide(j) == noSlot)
            return false;
        return FluidContainerRegistry.isFilledContainer(itemstack);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return false;
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j)
    {
        if (i != 0)
            return false;
        if (getAccessibleSlotsFromSide(j) == noSlot)
            return false;
        return FluidContainerRegistry.isEmptyContainer(itemstack);
    }

    @Override
    public void closeChest()
    {
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (i != 0)
            return null;
        ItemStack stack;
        if (item.stackSize <= j)
        {
            stack = item;
            item = null;
        }
        else
        {
            stack = item.splitStack(j);
            if (item.stackSize == 0)
                item = null;
        }
        return stack;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        return 0;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        if (var1 == ForgeDirection.UNKNOWN.ordinal())
            return slots;
        for (ForgeDirection dir : BlockCubeBase.VALID_DIRECTIONS)
            if (var1 == dir.ordinal())
                return slots;
        return noSlot;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
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
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        if (i != 0)
            return null;
        return item;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (i != 0)
            return null;
        ItemStack stack = item;
        item = null;
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
        if (i != 0)
            return false;
        return FluidContainerRegistry.isEmptyContainer(itemstack);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public boolean onActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        ItemStack holding = player.getCurrentEquippedItem();
        if (FluidContainerRegistry.isEmptyContainer(holding))
        {
            FluidStack fluid = getTankInfo(ForgeDirection.UNKNOWN)[0].fluid;
            if (fluid != null)
            {
                ItemStack filled = FluidContainerRegistry.fillFluidContainer(fluid, holding);
                fluid = FluidContainerRegistry.getFluidForFilledItem(filled);
                if (fluid != null)
                {
                    if (!player.capabilities.isCreativeMode)
                    {
                        if (holding.stackSize > 1)
                        {
                            if (!player.inventory.addItemStackToInventory(filled))
                                player.dropPlayerItem(filled);
                            holding.splitStack(1);
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, holding);
                        }
                        else
                        {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, filled);
                        }
                        player.inventoryContainer.detectAndSendChanges();
                    }
                    drain(ForgeDirection.UNKNOWN, fluid.amount, true);
                }
            }
        }
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

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 == 0)
                item = ItemStack.loadItemStackFromNBT(nbttagcompound1);
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        if (i == 0)
            item = itemstack;
    }

    @Override
    public void updateEntity()
    {
        if (worldObj.isRemote)
            return;

        tank.fill(new FluidStack(FluidRegistry.WATER, tickGain), true);

        for (ForgeDirection to : BlockCubeBase.VALID_DIRECTIONS)
        {
            TileEntity tile = worldObj.getBlockTileEntity(xCoord + to.offsetX, yCoord + to.offsetY, zCoord + to.offsetZ);
            if (tile != null && tile instanceof IFluidHandler)
            {
                IFluidHandler handler = (IFluidHandler) tile;
                if (handler.canFill(to.getOpposite(), FluidRegistry.WATER))
                {
                    FluidStack stack = tank.drain(tickFlow, false);
                    if (stack != null && stack.amount >= tickFlow)
                    {
                        int filled = handler.fill(to.getOpposite(), stack, true);
                        tank.drain(filled, true);
                    }
                }
            }
        }

        if (FluidContainerRegistry.isEmptyContainer(item))
        {
            FluidStack fluid = getTankInfo(ForgeDirection.UNKNOWN)[0].fluid;
            if (fluid != null)
            {
                ItemStack filled = FluidContainerRegistry.fillFluidContainer(fluid, item);
                fluid = FluidContainerRegistry.getFluidForFilledItem(filled);
                if (fluid != null)
                {
                    item = filled;
                    drain(ForgeDirection.UNKNOWN, fluid.amount, true);
                }
            }
        }

        for (int x = xCoord - 4; x <= xCoord + 4; x++)
        {
            for (int y = yCoord; y <= yCoord + 1; y++)
            {
                for (int z = zCoord - 4; z <= zCoord + 4; z++)
                {
                    int id = worldObj.getBlockId(x, y, z);
                    if (id != 0 && Block.blocksList[id] != null)
                    {
                        if (id == Block.fire.blockID)
                        {
                            worldObj.setBlockToAir(x, y, z);
                        }
                        else if (id == Block.tilledField.blockID)
                        {
                            if (worldObj.getBlockMetadata(x, y, z) < 7)
                                worldObj.setBlockMetadataWithNotify(x, y, z, 7, 2);
                        }
                        else if (id == Block.crops.blockID || Block.blocksList[id] instanceof BlockSapling || Block.blocksList[id] instanceof IPlantable)
                        {
                            if (Block.blocksList[id].getTickRandomly() && worldObj.rand.nextInt(1000) < 5)
                                worldObj.scheduleBlockUpdate(x, y, z, id, worldObj.rand.nextInt(20));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        if (item != null)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte) 0);
            item.writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);
    }

}
