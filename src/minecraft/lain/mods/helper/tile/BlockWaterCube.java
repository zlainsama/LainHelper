package lain.mods.helper.tile;

import lain.mods.helper.tile.base.BlockCubeBase;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockWaterCube extends BlockCubeBase
{

    public BlockWaterCube(int par1)
    {
        super(par1);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileWaterCube();
    }

    @Override
    public void registerIcons(IconRegister par1)
    {
        icons.put("top", par1.registerIcon("helper:cubeTopWater"));
        icons.put("side", par1.registerIcon("helper:cubeSide"));
        icons.put("bottom", par1.registerIcon("helper:cubeBottom"));
        icons.put("unknown", par1.registerIcon("helper:cubeUnknown"));
    }

}
