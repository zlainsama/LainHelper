package lain.mods.helper.tile.base;

import java.util.Map;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import com.google.common.collect.Maps;

public abstract class BlockCubeBase extends BlockContainer
{

    public static final ForgeDirection[] VALID_DIRECTIONS = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST };

    protected Map<String, Icon> icons = Maps.newHashMap();

    protected BlockCubeBase(int par1)
    {
        super(par1, Material.iron);
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
                return icons.get("side");
            default:
                return icons.get("unknown");
        }
    }

    @Override
    public void registerIcons(IconRegister par1)
    {
        icons.put("top", par1.registerIcon("helper:cubeTop"));
        icons.put("side", par1.registerIcon("helper:cubeSide"));
        icons.put("bottom", par1.registerIcon("helper:cubeBottom"));
        icons.put("unknown", par1.registerIcon("helper:cubeUnknown"));
    }

}
