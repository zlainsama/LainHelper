package lain.mods.helper.tile.base;

import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import com.google.common.collect.Maps;

public abstract class BlockCubeBase extends BlockContainer
{

    public static final Material matCube = new MaterialCube(MapColor.airColor);

    protected Map<String, Icon> icons = Maps.newHashMap();

    protected BlockCubeBase(int par1)
    {
        super(par1, matCube);
        setStepSound(Block.soundStoneFootstep);
        setHardness(1.0F);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
        if (tile instanceof IInventory)
        {
            IInventory inv = (IInventory) tile;
            for (int i = 0; i < inv.getSizeInventory(); i++)
            {
                ItemStack itemstack = inv.getStackInSlot(i);
                if (itemstack != null)
                {
                    float f = par1World.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = par1World.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem = null;
                    for (float f2 = par1World.rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld(entityitem))
                    {
                        int k1 = par1World.rand.nextInt(21) + 10;
                        if (k1 > itemstack.stackSize)
                            k1 = itemstack.stackSize;
                        itemstack.stackSize -= k1;
                        entityitem = new EntityItem(par1World, (double) ((float) par2 + f), (double) ((float) par3 + f1), (double) ((float) par4 + f2), new ItemStack(itemstack.itemID, k1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double) ((float) par1World.rand.nextGaussian() * f3);
                        entityitem.motionY = (double) ((float) par1World.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double) ((float) par1World.rand.nextGaussian() * f3);
                        if (itemstack.hasTagCompound())
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                    }
                }
            }
        }
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    @Override
    public Icon getIcon(int par1, int par2)
    {
        switch (par1)
        {
            case 0:
                return icons.get("bottom");
            case 1:
                return icons.get("top");
            case 2:
            case 3:
            case 4:
            case 5:
                return icons.get("sideValid");
                // return icons.get("side");
            default:
                return icons.get("unknown");
        }
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
        if (tile instanceof IActivatableCubeTile)
            return ((IActivatableCubeTile) tile).onActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
        return false;
    }

    @Override
    public void registerIcons(IconRegister par1)
    {
        icons.put("top", par1.registerIcon("cubeTop"));
        icons.put("side", par1.registerIcon("cubeSide"));
        icons.put("sideValid", par1.registerIcon("cubeSideValid"));
        icons.put("bottom", par1.registerIcon("cubeBottom"));
        icons.put("unknown", par1.registerIcon("cubeUnknown"));
    }

}
