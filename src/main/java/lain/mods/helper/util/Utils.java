package lain.mods.helper.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

public class Utils
{

    public static World getTileWorld(TileEntity tile)
    {
        return tile.func_145831_w();
    }

    public static int getTileCoordX(TileEntity tile)
    {
        return tile.field_145851_c;
    }

    public static int getTileCoordY(TileEntity tile)
    {
        return tile.field_145848_d;
    }

    public static int getTileCoordZ(TileEntity tile)
    {
        return tile.field_145849_e;
    }

    public static Block getTileBlockType(TileEntity tile)
    {
        return tile.func_145838_q();
    }

    public static IInventory getTileInventory(TileEntity tile)
    {
        if (tile instanceof TileEntityChest)
        {
            Block block = getTileBlockType(tile);
            if (block instanceof BlockChest)
                return ((BlockChest) block).func_149951_m(getTileWorld(tile), getTileCoordX(tile), getTileCoordY(tile), getTileCoordZ(tile));
        }
        if (tile instanceof IInventory)
            return (IInventory) tile;
        return null;
    }

    public static Block getWorldBlockType(World world, int x, int y, int z)
    {
        return world.func_147439_a(x, y, z);
    }

    public static TileEntity getWorldTile(World world, int x, int y, int z)
    {
        return world.func_147438_o(x, y, z);
    }
    
    public static boolean isBlockOpaque(Block block)
    {
        return block.func_149662_c();
    }

}
