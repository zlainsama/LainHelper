package lain.mods.helper.tile;

import lain.mods.helper.tile.base.BlockCubeBase;
import lain.mods.helper.tile.base.ISpecialCubeTile;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.TileFluidHandler;

public class TileWaterCube extends TileFluidHandler implements ISpecialCubeTile
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
        switch (from)
        {
            case DOWN:
            case UP:
                return false;
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                return true;
            default:
                return true;
        }
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
    }

}
