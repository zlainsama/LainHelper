package lain.mods.helper;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.ReloadableResourceManager;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.client.resources.ResourceManagerReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.IWorldAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import com.google.common.collect.Maps;
import cpw.mods.fml.client.FMLClientHandler;

public final class OfflineSkin implements IWorldAccess, ResourceManagerReloadListener
{

    private static Minecraft mc;
    private static ResourceManager resman;

    public static void setup()
    {
        mc = FMLClientHandler.instance().getClient();
        resman = mc.getResourceManager();
        OfflineSkin handler = new OfflineSkin();
        MinecraftForge.EVENT_BUS.register(handler);
        if (resman instanceof ReloadableResourceManager)
            ((ReloadableResourceManager) resman).registerReloadListener(handler);
    }

    private Map<ResourceLocation, BufferedImage> cache = Maps.newHashMap();

    @Override
    public void broadcastSound(int i, int j, int k, int l, int i1)
    {
    }

    @Override
    public void destroyBlockPartially(int i, int j, int k, int l, int i1)
    {
    }

    private BufferedImage getCachedImage(ResourceLocation location)
    {
        BufferedImage image = cache.get(location);
        if (image == null)
        {
            try
            {
                image = ImageIO.read(resman.getResource(location).getInputStream());
            }
            catch (IOException ignored)
            {
            }
            cache.put(location, image);
        }
        return image;
    }

    private void loadSkin(Entity entity)
    {
        if (entity instanceof AbstractClientPlayer)
        {
            AbstractClientPlayer player = (AbstractClientPlayer) entity;
            ThreadDownloadImageData texture = player.getTextureSkin();
            if (texture != null && !texture.isTextureUploaded())
            {
                ResourceLocation location = new ResourceLocation(String.format("skins/%s.png", StringUtils.stripControlCodes(player.username)));
                BufferedImage image = getCachedImage(location);
                if (image != null)
                    TextureUtil.uploadTextureImage(texture.getGlTextureId(), image);
            }
        }
    }

    @ForgeSubscribe
    public void loadWorld(WorldEvent.Load event)
    {
        event.world.addWorldAccess(this);
    }

    @Override
    public void markBlockForRenderUpdate(int i, int j, int k)
    {
    }

    @Override
    public void markBlockForUpdate(int i, int j, int k)
    {
    }

    @Override
    public void markBlockRangeForRenderUpdate(int i, int j, int k, int l, int i1, int j1)
    {
    }

    @Override
    public void onEntityCreate(Entity entity)
    {
        loadSkin(entity);
    }

    @Override
    public void onEntityDestroy(Entity entity)
    {
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourcemanager)
    {
        if (mc.theWorld != null)
            for (Entity entity : (List<Entity>) mc.theWorld.loadedEntityList)
                loadSkin(entity);
    }

    @Override
    public void playAuxSFX(EntityPlayer entityplayer, int i, int j, int k, int l, int i1)
    {
    }

    @Override
    public void playRecord(String s, int i, int j, int k)
    {
    }

    @Override
    public void playSound(String s, double d0, double d1, double d2, float f, float f1)
    {
    }

    @Override
    public void playSoundToNearExcept(EntityPlayer entityplayer, String s, double d0, double d1, double d2, float f, float f1)
    {
    }

    @Override
    public void spawnParticle(String s, double d0, double d1, double d2, double d3, double d4, double d5)
    {
    }

    @ForgeSubscribe
    public void unloadWorld(WorldEvent.Unload event)
    {
        event.world.removeWorldAccess(this);
    }

}
