package lain.mods.helper.tile.renderer;

import lain.mods.helper.tile.base.ISpecialCubeTile;
import lain.mods.helper.util.Utils;
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
    public void func_147500_a(TileEntity tile, double x, double y, double z, float partialTicks)
    {
        if (tile != null && Utils.getTileWorld(tile) != null && tile instanceof ISpecialCubeTile)
        {
            ItemStack stack = ((ISpecialCubeTile) tile).getItemToDisplayOnTop();
            if (stack != null && !Utils.isBlockOpaque(Utils.getWorldBlockType(Utils.getTileWorld(tile), Utils.getTileCoordX(tile), Utils.getTileCoordY(tile) + 1, Utils.getTileCoordZ(tile))))
            {
                EntityItem ent = null;
                float ticks = Minecraft.getMinecraft().renderViewEntity.ticksExisted + partialTicks;
                GL11.glPushMatrix();
                GL11.glTranslatef((float) x + 0.5F, (float) y + 1.2F, (float) z + 0.5F);
                if (Minecraft.isFancyGraphicsEnabled())
                    GL11.glRotatef(ticks % 360.0F, 0.0F, 1.0F, 0.0F);
                else
                    GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
                int b = Utils.getTileWorld(tile).getLightBrightnessForSkyBlocks(Utils.getTileCoordX(tile), Utils.getTileCoordY(tile) + 1, Utils.getTileCoordZ(tile), 0);
                // GL11.glScalef(1.0F, 1.0F, 1.0F);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) (int) (b % 65536) / 1.0F, (float) (int) (b / 65536)); // standard lighting: 240 240
                // GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                ent = new EntityItem(Utils.getTileWorld(tile), 0.0D, 0.0D, 0.0D, stack);
                ent.hoverStart = 0.0F;
                RenderManager.instance.func_147940_a(ent, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }
    }

}
