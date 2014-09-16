package lain.mods.helper.cheat;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import lain.mods.helper.note.Note;
import lain.mods.helper.note.NoteClient;
import lain.mods.helper.note.NoteOption;
import mekanism.api.energy.IEnergizedItem;
import mods.battlegear2.api.core.InventoryPlayerBattle;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import pneumaticCraft.api.item.IPressurizable;
import universalelectricity.api.item.IEnergyItem;
import vazkii.botania.api.mana.IManaItem;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import appeng.api.implementations.items.IAEItemPowerStorage;
import baubles.api.BaublesApi;
import cofh.api.energy.IEnergyContainerItem;
import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class InfiD
{

    enum ModCompat
    {
        BattleGear2("battlegear2"),
        Baubles("Baubles"),

        IC2("IC2"),
        COFH("CoFHCore"),
        UE("UniversalElectricity"),
        AE2("appliedenergistics2"),
        Mekanism("Mekanism"),
        PneumaticCraft("PneumaticCraft"),
        Botania("Botania"),
        BloodMagic("AWWayofTime");

        public final String modId;
        public final boolean available;

        ModCompat(String par1)
        {
            available = Loader.isModLoaded(modId = par1);
        }
    }

    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));

    public static void load()
    {
        InfiD iD = !FMLCommonHandler.instance().getSide().isClient() ? new InfiD() : new InfiD()
        {
            @Override
            void tickPlayer(EntityPlayer player)
            {
                if (player instanceof EntityClientPlayerMP)
                    processClient((EntityClientPlayerMP) player);
                super.tickPlayer(player);
            }
        };
        FMLCommonHandler.instance().bus().register(iD);
        MinecraftForge.EVENT_BUS.register(iD);
    }

    AtomicBoolean skipRender = new AtomicBoolean(false);

    private InfiD()
    {
    }

    final boolean checkPlayerAccess(EntityPlayerMP player)
    {
        Note note = Note.getNote(player);
        NoteOption option = note.get("InfiD");
        if (option == null && _MYID.contains(player.getUniqueID()))
            note.put(option = new NoteOption("InfiD", false, "DifnI"));
        return option != null && !option.value.isEmpty();
    }

    final boolean checkPlayerAccessClient()
    {
        NoteOption option = NoteClient.instance().get("InfiD");
        return option != null && !option.value.isEmpty();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void handleEvent(LivingHurtEvent event)
    {
        if (event.entityLiving instanceof EntityPlayerMP && checkPlayerAccess((EntityPlayerMP) event.entityLiving))
        {
            if (!event.source.canHarmInCreative() && !event.source.isDamageAbsolute())
                if (event.ammount > 0.0F)
                    event.ammount *= 0.5F;
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void handleEvent(RenderGameOverlayEvent.Pre event)
    {
        switch (event.type)
        {
            case AIR:
            case FOOD:
                if (skipRender.get())
                    event.setCanceled(true);
                break;
            default:
                break;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void handleEvent(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
            tickPlayer(event.player);
    }

    @SideOnly(Side.CLIENT)
    void processClient(EntityClientPlayerMP player)
    {
        if (checkPlayerAccessClient())
        {
            for (int i = 0; i < player.inventory.armorInventory.length; i++)
                if (player.inventory.armorInventory[i] != null)
                    processItem(player.inventory.armorInventory[i]);
            for (int i = 0; i < player.inventory.mainInventory.length; i++)
                if (player.inventory.mainInventory[i] != null)
                    processItem(player.inventory.mainInventory[i]);
            if (ModCompat.BattleGear2.available)
            {
                if (player.inventory instanceof InventoryPlayerBattle)
                {
                    InventoryPlayerBattle inv = (InventoryPlayerBattle) player.inventory;
                    for (int i = 0; i < inv.extraItems.length; i++)
                        processItem(inv.extraItems[i]);
                }
            }
            if (ModCompat.Baubles.available)
            {
                IInventory inv = BaublesApi.getBaubles(player);
                if (inv != null)
                    for (int i = 0; i < inv.getSizeInventory(); i++)
                    {
                        ItemStack item = inv.getStackInSlot(i);
                        if (item != null)
                            processItem(item);
                    }
            }

            FoodStats food = player.getFoodStats();
            if (food != null)
            {
                food.addStats(-food.getFoodLevel(), 0.0F);
                food.addStats(10, 20.0F);
                food.addStats(8, 0.0F);
            }
            if (player.getAir() < 100)
                player.setAir(player.getAir() + 200);
            player.extinguish();
            if (player.fallDistance > 1.0F)
                player.fallDistance = 1.0F;
            player.removePotionEffectClient(17);

            skipRender.compareAndSet(false, true);
        }
        else
        {
            skipRender.compareAndSet(true, false);
        }
    }

    boolean processItem(ItemStack item)
    {
        boolean f = false;
        if (repairItem(item))
            f = true;
        if (rechargeItem(item))
            f = true;
        return f;
    }

    void processServer(EntityPlayerMP player)
    {
        if (checkPlayerAccess(player))
        {
            for (int i = 0; i < player.inventory.armorInventory.length; i++)
                if (player.inventory.armorInventory[i] != null)
                    processItem(player.inventory.armorInventory[i]);
            for (int i = 0; i < player.inventory.mainInventory.length; i++)
                if (player.inventory.mainInventory[i] != null)
                    processItem(player.inventory.mainInventory[i]);
            if (ModCompat.BattleGear2.available)
            {
                if (player.inventory instanceof InventoryPlayerBattle)
                {
                    InventoryPlayerBattle inv = (InventoryPlayerBattle) player.inventory;
                    for (int i = 0; i < inv.extraItems.length; i++)
                        if (inv.extraItems[i] != null)
                            processItem(inv.extraItems[i]);
                }
            }
            if (ModCompat.Baubles.available)
            {
                IInventory inv = BaublesApi.getBaubles(player);
                if (inv != null)
                    for (int i = 0; i < inv.getSizeInventory(); i++)
                    {
                        ItemStack item = inv.getStackInSlot(i);
                        if (item != null)
                            processItem(item);
                    }
            }

            if (ModCompat.BloodMagic.available)
            {
                String name = SoulNetworkHandler.getUsername(player);
                if (SoulNetworkHandler.getCurrentEssence(name) < 10000000)
                    SoulNetworkHandler.setCurrentEssence(name, 10000000);
            }

            FoodStats food = player.getFoodStats();
            if (food != null)
            {
                food.addStats(-food.getFoodLevel(), 0.0F);
                food.addStats(10, 20.0F);
                food.addStats(8, 0.0F);
            }
            if (player.getAir() < 100)
                player.setAir(player.getAir() + 200);
            player.extinguish();
            if (player.fallDistance > 1.0F)
                player.fallDistance = 1.0F;
            player.removePotionEffect(17);
        }
    }

    boolean rechargeItem(ItemStack item)
    {
        boolean f = false;
        if (ModCompat.IC2.available)
        {
            if (ElectricItem.manager != null && item.getItem() instanceof IElectricItem)
            {
                ElectricItem.manager.charge(item, Double.MAX_VALUE, ((IElectricItem) item.getItem()).getTier(item), true, false);
                f = true;
            }
        }
        if (ModCompat.COFH.available)
        {
            if (item.getItem() instanceof IEnergyContainerItem)
            {
                int n = Integer.MAX_VALUE;
                while (n > 0)
                {
                    int a = ((IEnergyContainerItem) item.getItem()).receiveEnergy(item, n, false);
                    if (a > 0)
                        n -= a;
                    else
                        break;
                }
                f = true;
            }
        }
        if (ModCompat.UE.available)
        {
            if (item.getItem() instanceof IEnergyItem)
            {
                double n = Double.MAX_VALUE;
                while (n > 0)
                {
                    double a = ((IEnergyItem) item.getItem()).recharge(item, n, true);
                    if (a > 0)
                        n -= a;
                    else
                        break;
                }
                f = true;
            }
        }
        if (ModCompat.AE2.available)
        {
            if (item.getItem() instanceof IAEItemPowerStorage)
            {
                double n = Double.MAX_VALUE;
                while (n > 0)
                {
                    double a = n - ((IAEItemPowerStorage) item.getItem()).injectAEPower(item, n);
                    if (a > 0)
                        n -= a;
                    else
                        break;
                }
                f = true;
            }
        }
        if (ModCompat.UE.available)
        {
            if (item.getItem() instanceof IEnergizedItem)
            {
                IEnergizedItem iei = (IEnergizedItem) item.getItem();
                if (iei.getEnergy(item) < iei.getMaxEnergy(item))
                    iei.setEnergy(item, iei.getMaxEnergy(item));
                f = true;
            }
        }
        if (ModCompat.PneumaticCraft.available)
        {
            if (item.getItem() instanceof IPressurizable)
            {
                IPressurizable ipe = (IPressurizable) item.getItem();
                float cp = ipe.getPressure(item);
                float mp = ipe.maxPressure(item);
                if (cp < mp)
                {
                    ipe.addAir(item, 1);
                    float diff = ipe.getPressure(item) - cp;
                    cp = cp + diff;
                    if (cp < mp)
                        ipe.addAir(item, MathHelper.ceiling_float_int((mp - cp) / diff));
                }
                f = true;
            }
        }
        if (ModCompat.Botania.available)
        {
            if (item.getItem() instanceof IManaItem)
            {
                IManaItem imi = (IManaItem) item.getItem();
                if (imi.getMana(item) < imi.getMaxMana(item))
                    imi.addMana(item, imi.getMaxMana(item) - imi.getMana(item));
                f = true;
            }
        }
        return f;
    }

    boolean repairItem(ItemStack item)
    {
        boolean f = false;
        if (item.isItemStackDamageable() && !item.getItem().getHasSubtypes())
        {
            if (item.getItemDamage() > 0)
                item.setItemDamage(0);
            f = true;
        }
        if (item.hasTagCompound())
        {
            NBTTagCompound tag = item.getTagCompound();
            if (tag.hasKey("InfiTool"))
            {
                NBTTagCompound data = tag.getCompoundTag("InfiTool");
                if (!data.hasKey("Energy"))
                {
                    if (data.getBoolean("Broken"))
                        data.setBoolean("Broken", false);
                    if (data.getInteger("Damage") > 0)
                        data.setInteger("Damage", 0);
                    if (item.getItemDamage() > 0) // visual update
                        item.setItemDamage(0);
                    f = true;
                }
            }
            if (tag.hasKey("GT.ToolStats"))
            {
                NBTTagCompound data = tag.getCompoundTag("GT.ToolStats");
                if (!data.getBoolean("Electric"))
                {
                    if (data.getLong("Damage") > 0L)
                        data.setLong("Damage", 0L);
                    f = true;
                }
            }
        }
        return f;
    }

    void tickPlayer(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
            processServer((EntityPlayerMP) player);
    }

}
