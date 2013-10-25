package lain.mods.helper.tile.renderer;

import lain.mods.helper.tile.base.ISpecialCubeTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpecialCubeTileRenderer extends TileEntitySpecialRenderer
{

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks)
    {
        if (tile != null && tile.worldObj != null && tile instanceof ISpecialCubeTile)
        {
            ItemStack stack = ((ISpecialCubeTile) tile).getItemToDisplayOnTop();
            if (stack != null && !tile.worldObj.isBlockOpaqueCube(tile.xCoord, tile.yCoord + 1, tile.zCoord))
            {
                EntityItem ent = null;
                float ticks = Minecraft.getMinecraft().renderViewEntity.ticksExisted + partialTicks;
                GL11.glPushMatrix();
                GL11.glTranslatef((float) x + 0.5F, (float) y + 1.2F, (float) z + 0.5F);
                GL11.glRotatef(ticks % 360.0F, 0.0F, 1.0F, 0.0F);
                int b = tile.worldObj.getLightBrightnessForSkyBlocks(tile.xCoord, tile.yCoord + 1, tile.zCoord, 0);
                // GL11.glScalef(1.0F, 1.0F, 1.0F);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) (int) (b % 65536) / 1.0F, (float) (int) (b / 65536)); // standard lighting: 240 240
                // GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                ent = new EntityItem(tile.worldObj, 0.0D, 0.0D, 0.0D, stack);
                ent.hoverStart = 0.0F;
                RenderManager.instance.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
                GL11.glPopMatrix();
            }
        }
    }

}
