package lain.mods.helper.tile.base;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialCube extends Material
{

    public MaterialCube(MapColor color)
    {
        super(color);
    }

    @Override
    public boolean blocksMovement()
    {
        return true;
    }

    @Override
    public boolean getCanBlockGrass()
    {
        return true;
    }

    @Override
    public boolean getCanBurn()
    {
        return false;
    }

    @Override
    public int getMaterialMobility()
    {
        return 2;
    }

    @Override
    public boolean isAdventureModeExempt()
    {
        return false;
    }

    @Override
    public boolean isLiquid()
    {
        return false;
    }

    @Override
    public boolean isOpaque()
    {
        return true;
    }

    @Override
    public boolean isReplaceable()
    {
        return false;
    }

    @Override
    public boolean isSolid()
    {
        return true;
    }

    @Override
    public boolean isToolNotRequired()
    {
        return true;
    }

}
