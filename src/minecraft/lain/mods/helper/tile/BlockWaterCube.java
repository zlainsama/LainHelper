package lain.mods.helper.tile;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockWaterCube extends BlockContainer
{

    public BlockWaterCube(int par1, Material par2Material)
    {
        super(par1, par2Material);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileWaterCube();
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        ItemStack holding = par5EntityPlayer.getCurrentEquippedItem();
        if (FluidContainerRegistry.getFluidForFilledItem(holding) == null)
        {
            TileWaterCube tile = (TileWaterCube) par1World.getBlockTileEntity(par2, par3, par4);
            FluidStack fluid = tile.getTankInfo(ForgeDirection.UNKNOWN)[0].fluid;
            if (fluid != null)
            {
                ItemStack filled = FluidContainerRegistry.fillFluidContainer(fluid, holding);
                fluid = FluidContainerRegistry.getFluidForFilledItem(filled);
                if (fluid != null)
                {
                    if (!par5EntityPlayer.capabilities.isCreativeMode)
                    {
                        if (holding.stackSize > 1)
                        {
                            if (!par5EntityPlayer.inventory.addItemStackToInventory(filled))
                                return false;
                            holding.splitStack(1);
                            par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, holding);
                        }
                        else
                        {
                            par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, filled);
                        }
                        par5EntityPlayer.inventoryContainer.detectAndSendChanges();
                    }
                    tile.drain(ForgeDirection.UNKNOWN, fluid.amount, true);
                }
            }
        }
        return true;
    }

}
