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
            if (!event.world.isRemote && !event.isSilkTouching && (event.block instanceof IPlantable || event.block.blockID == Block.melon.blockID))
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
                    if (event.block.blockID == Block.crops.blockID)
                    {
                        event.drops.clear();
                        event.drops.add(new ItemStack(Item.seeds.itemID, 1, 0));
                        if (event.blockMetadata >= 7)
                        {
                            event.drops.add(new ItemStack(Item.seeds.itemID, 1, 0));
                            for (int n = 0; n < 3 + event.fortuneLevel; n++)
                                event.drops.add(new ItemStack(Item.wheat.itemID, 1, 0));
                        }
                    }
                    else if (event.block.blockID == Block.carrot.blockID)
                    {
                        event.drops.clear();
                        event.drops.add(new ItemStack(Item.carrot.itemID, 1, 0));
                        if (event.blockMetadata >= 7)
                        {
                            for (int n = 0; n < 3 + event.fortuneLevel; n++)
                                event.drops.add(new ItemStack(Item.carrot.itemID, 1, 0));
                        }
                    }
                    else if (event.block.blockID == Block.potato.blockID)
                    {
                        event.drops.clear();
                        event.drops.add(new ItemStack(Item.potato.itemID, 1, 0));
                        if (event.blockMetadata >= 7)
                        {
                            for (int n = 0; n < 3 + event.fortuneLevel; n++)
                                event.drops.add(new ItemStack(Item.potato.itemID, 1, 0));
                        }
                    }
                    else if (event.block.blockID == Block.melon.blockID)
                    {
                        event.drops.clear();
                        for (int n = 0; n < 7; n++)
                            event.drops.add(new ItemStack(Item.melon.itemID, 1, 0));
                    }
                    else if (event.block.blockID == Block.netherStalk.blockID)
                    {
                        event.drops.clear();
                        event.drops.add(new ItemStack(Item.netherStalkSeeds.itemID, 1, 0));
                        if (event.blockMetadata >= 3)
                        {
                            for (int n = 0; n < 3 + event.fortuneLevel; n++)
                                event.drops.add(new ItemStack(Item.netherStalkSeeds.itemID, 1, 0));
                        }
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
    }

}
