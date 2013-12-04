package lain.mods.helper;

import lain.mods.helper.tile.TileCobbleCube;
import lain.mods.helper.tile.TileWaterCube;
import lain.mods.helper.tile.renderer.SpecialCubeTileRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy
{

    @Override
    public void load(FMLInitializationEvent event)
    {
        super.load(event);

        TileEntitySpecialRenderer renderer = new SpecialCubeTileRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileWaterCube.class, renderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileCobbleCube.class, renderer);

        TooltipTweaker.setup();
    }

}
