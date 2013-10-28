package lain.mods.helper.tile;

import lain.mods.helper.tile.base.BlockCubeBase;
import lain.mods.helper.tile.base.IActivatableCubeTile;
import lain.mods.helper.tile.base.ISpecialCubeTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

public class TileWaterCube extends TileFluidHandler implements ISpecialCubeTile, IActivatableCubeTile
{

    private static final ItemStack ITEMTODISPLAY = new ItemStack(Item.bucketWater);

    private static final int capacity = (int) (FluidContainerRegistry.BUCKET_VOLUME * 4.0);
    private static final int tickGain = (int) (FluidContainerRegistry.BUCKET_VOLUME * 0.2);
    private static final int tickFlow = (int) (FluidContainerRegistry.BUCKET_VOLUME * 0.1);

    public TileWaterCube()
    {
        tank.setCapacity(capacity);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        if (from.ordinal() == ForgeDirection.UNKNOWN.ordinal())
            return true;
        for (ForgeDirection dir : BlockCubeBase.VALID_DIRECTIONS)
            if (from.ordinal() == dir.ordinal())
                return true;
        return false;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        return 0;
    }

    @Override
    public ItemStack getItemToDisplayOnTop()
    {
        return ITEMTODISPLAY;
    }

    @Override
    public boolean onActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        ItemStack holding = player.getCurrentEquippedItem();
        if (FluidContainerRegistry.getFluidForFilledItem(holding) == null)
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
                    int filled = handler.fill(to.getOpposite(), stack, true);
                    tank.drain(filled, true);
                }
            }
        }

        for (int x = xCoord - 4; x <= xCoord + 4; x++)
        {
            for (int y = yCoord - 1; y <= yCoord + 1; y++)
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
                        else if (id == Block.crops.blockID || Block.blocksList[id] instanceof IPlantable)
                        {
                            if (Block.blocksList[id].getTickRandomly())
                                worldObj.scheduleBlockUpdate(x, y, z, id, worldObj.rand.nextInt(40));
                        }
                        else if (Block.blocksList[id] instanceof BlockSapling)
                        {
                            if (Block.blocksList[id].getTickRandomly())
                                worldObj.scheduleBlockUpdate(x, y, z, id, worldObj.rand.nextInt(50));
                        }
                    }
                }
            }
        }
    }

}
