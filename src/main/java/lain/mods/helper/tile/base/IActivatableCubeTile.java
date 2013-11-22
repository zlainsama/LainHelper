package lain.mods.helper.tile.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IActivatableCubeTile
{

    public boolean onActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ);

}
