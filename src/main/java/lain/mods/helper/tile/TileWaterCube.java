package lain.mods.helper.tile;

import lain.mods.helper.tile.base.IActivatableCubeTile;
import lain.mods.helper.tile.base.ISpecialCubeTile;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.TileFluidHandler;

public class TileWaterCube extends TileFluidHandler implements ISpecialCubeTile, IActivatableCubeTile
{

    public static class harvestHandler
    {
        @ForgeSubscribe
        public void onBlockHarvestDrops(BlockEvent.HarvestDropsEvent event)
        {
            if (!event.world.isRemote && !event.isSilkTouching && event.block instanceof IPlantable)
            {
                boolean flag = false;
                for (int cX = (event.x >> 4) - 1; cX <= (event.x >> 4) + 1; cX++)
                {
                    for (int cZ = (event.z >> 4) - 1; cZ <= (event.z >> 4) + 1; cZ++)
                    {
                        if (event.world.getChunkProvider().chunkExists(cX, cZ))
                        {
                            Chunk c = event.world.getChunkFromChunkCoords(cX, cZ);
                            if (c.isChunkLoaded)
                            {
                                for (Object tile : c.chunkTileEntityMap.values())
                                {
                                    if (tile instanceof TileWaterCube)
                                    {
                                        TileWaterCube cube = (TileWaterCube) tile;
                                        if (Math.abs(cube.xCoord - event.x) <= 4 && Math.abs(cube.zCoord - event.z) <= 4 && Math.abs(cube.yCoord - event.y) <= 4)
                                        {
                                            flag = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (flag)
                {
                    for (ItemStack item : event.drops)
                    {
                        if (event.world.rand.nextBoolean())
                            event.drops.add(item.copy());
                    }
                    event.dropChance = 1.0F;
                }
            }
        }
    }

    private static final ItemStack ITEMTODISPLAY = new ItemStack(Item.bucketWater);

    private static final int capacity = (int) (FluidContainerRegistry.BUCKET_VOLUME * 8.0);
    private static final int tickGain = (int) (FluidContainerRegistry.BUCKET_VOLUME * 0.4);
    private static final int tickFlow = (int) (FluidContainerRegistry.BUCKET_VOLUME * 0.2);

    static
    {
        MinecraftForge.EVENT_BUS.register(new harvestHandler());
    }

    public TileWaterCube()
    {
        tank.setCapacity(capacity);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return true;
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
                        player.openContainer.detectAndSendChanges();
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

        for (ForgeDirection to : ForgeDirection.VALID_DIRECTIONS)
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

        for (int x = xCoord - 4; x <= xCoord + 4; x++)
        {
            for (int y = yCoord - 0; y <= yCoord + 0; y++)
            {
                for (int z = zCoord - 4; z <= zCoord + 4; z++)
                {
                    int id = worldObj.getBlockId(x, y, z);
                    if (id != 0 && Block.blocksList[id] != null)
                    {
                        if (id == Block.tilledField.blockID)
                        {
                            if (worldObj.getBlockMetadata(x, y, z) < 7)
                                worldObj.setBlockMetadataWithNotify(x, y, z, 7, 2);
                        }
                    }
                }
            }
        }
    }

}
