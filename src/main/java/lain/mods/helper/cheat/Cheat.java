package lain.mods.helper.cheat;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lain.mods.helper.LainHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.items.IRechargable;
import thaumcraft.api.items.RechargeHelper;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;

public class Cheat
{

    private static boolean check(Object obj)
    {
        if (obj instanceof EntityPlayerMP)
            return _MYID.contains(((EntityPlayerMP) obj).getUniqueID());
        if (obj instanceof UUID)
            return _MYID.contains(obj);
        return false;
    }

    public static Stream<ItemStack> sIn(EntityPlayer player)
    {
        Stream<ItemStack> res = IntStream.range(0, player.inventory.getSizeInventory()).mapToObj(player.inventory::getStackInSlot);

        if (fBaubles)
        {
            try
            {
                IBaublesItemHandler bih = BaublesApi.getBaublesHandler(player);
                res = Streams.concat(res, IntStream.range(0, bih.getSlots()).mapToObj(bih::getStackInSlot));
            }
            catch (Throwable ignored)
            {
            }
        }

        return res;
    }

    public static final Cheat INSTANCE = FMLCommonHandler.instance().getSide().isClient() ? new CheatClient() : new Cheat();
    protected static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));
    protected static final UUID _MODIFIER = UUID.fromString("c0d922c2-2d2f-423d-9a32-57f8ea57a86a");

    protected static final boolean fBaubles = Loader.isModLoaded("baubles");
    protected static final boolean fThaumcraft = Loader.isModLoaded("thaumcraft");
    protected static final boolean fBotania = Loader.isModLoaded("botania");

    static
    {
        LainHelper.network.registerPacket(240, PacketCheatInfo.class);

        MinecraftForge.EVENT_BUS.register(new CheatEventHandler());
    }

    public float applyDamageReduction(EntityPlayer player, DamageSource source, float amount)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags(player);
            if ((flags & 0x1) != 0)
            {
                float n = amount;

                if (!source.isUnblockable())
                    amount = CombatRules.getDamageAfterAbsorb(amount, 10F, 4F);
                // amount = CombatRules.getDamageAfterAbsorb(amount, 20F, 8F);
                if (!source.isDamageAbsolute())
                    amount = CombatRules.getDamageAfterMagicAbsorb(amount, 8F);
                // amount = CombatRules.getDamageAfterMagicAbsorb(amount, source == DamageSource.FALL ? 20F : 16F);

                n -= amount;

                player.addExperience(MathHelper.clamp(MathHelper.floor(n), 0, 5));
            }
        }
        return amount;
    }

    public float getArmorVisibility(EntityPlayer player, float result)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags(player);
            if ((flags & 0x1) != 0)
            {
                return 0F;
            }
        }
        return result;
    }

    public int getFlags(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
            return check(player) ? 1 : 0;
        return 0;
    }

    public int getFlags(UUID uuid)
    {
        return check(uuid) ? 1 : 0;
    }

    public int getFlagsClient()
    {
        return 0;
    }

    public boolean isInvisible(EntityPlayer player, boolean result)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags(player);
            if ((flags & 0x1) != 0)
            {
                return true;
            }
        }
        return result;
    }

    public boolean isPotionApplicable(EntityPlayer player, PotionEffect effect, boolean result)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags(player);
            if ((flags & 0x1) != 0)
            {
                if (effect.getPotion().isBadEffect())
                    return false;
            }
        }
        return result;
    }

    public void onLivingUpdate(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
        {
            int flags = getFlags(player);
            if ((flags & 0x1) != 0)
            {
                if (player.isEntityAlive())
                {
                    if (player.ticksExisted % 40 == 0)
                    {
                        float r = MathHelper.clamp(player.experienceLevel / 120F, 0F, 1F);

                        float maxShield = Math.max(6F, player.getMaxHealth() * (0.3F + (0.7F * r)));
                        float shield = player.getAbsorptionAmount();
                        if (shield < maxShield)
                        {
                            if (shield < 0F)
                                shield = 0F;
                            shield += Math.max(1F, maxShield * 0.2F);
                            if (shield > maxShield)
                                shield = maxShield;
                            player.setAbsorptionAmount(shield);
                        }

                        Set<ItemStack> heldItems = Streams.stream(player.getHeldEquipment()).filter(item -> !item.isEmpty()).collect(Collectors.toSet());
                        sIn(player).filter(item -> !item.isEmpty() && !heldItems.contains(item)).forEach(item -> {
                            if (Enchantments.MENDING.canApply(item))
                            {
                                int damage = item.getItemDamage();
                                if (damage > 0)
                                {
                                    damage -= Math.max(MathHelper.floor(damage * 0.2F), 4);
                                    if (damage < 0)
                                        damage = 0;
                                    item.setItemDamage(damage);
                                }
                            }
                            if (item.hasCapability(CapabilityEnergy.ENERGY, null))
                            {
                                IEnergyStorage cap = item.getCapability(CapabilityEnergy.ENERGY, null);
                                if (cap != null && cap.canReceive())
                                {
                                    int energy = cap.getEnergyStored();
                                    int maxEnergy = cap.getMaxEnergyStored();
                                    if (energy < maxEnergy)
                                        cap.receiveEnergy(Math.max(MathHelper.floor((maxEnergy - energy) * 0.2F), 4000), false);
                                }
                            }
                            if (fThaumcraft)
                            {
                                try
                                {
                                    if (item.getItem() instanceof IRechargable)
                                    {
                                        int charge = RechargeHelper.getCharge(item);
                                        int maxCharge = ((IRechargable) item.getItem()).getMaxCharge(item, player);
                                        if (charge < maxCharge)
                                            RechargeHelper.rechargeItemBlindly(item, player, Math.max(MathHelper.floor((maxCharge - charge) * 0.2F), 4));
                                    }
                                }
                                catch (Throwable ignored)
                                {
                                }
                            }
                        });

                        if (player.experienceLevel < 120)
                            player.addExperience(1);
                    }

                    if (player.ticksExisted % 200 == 0)
                    {
                        if (fThaumcraft)
                        {
                            try
                            {
                                World w = player.world;
                                BlockPos p = new BlockPos(player);

                                for (int xx = -1; xx <= 1; xx++)
                                {
                                    for (int zz = -1; zz <= 1; zz++)
                                    {
                                        BlockPos pp = p.add(xx * 16, 0, zz * 16);

                                        float f = AuraHelper.drainFlux(w, pp, MathHelper.clamp(AuraHelper.getFlux(w, pp), 0F, 10F), false);
                                        if (f > 0F)
                                        {
                                            float v = AuraHelper.getVis(w, pp);
                                            float b = AuraHelper.getAuraBase(w, pp);
                                            float c = MathHelper.clamp(f * 0.5F, 0F, v < b ? b - v : 0F);

                                            AuraHelper.addVis(w, pp, c);
                                            player.addExperience(MathHelper.floor(f - c));
                                        }
                                    }
                                }

                                IPlayerWarp warp = ThaumcraftCapabilities.getWarp(player);
                                int wN = warp.get(IPlayerWarp.EnumWarpType.NORMAL);
                                int wT = warp.get(IPlayerWarp.EnumWarpType.TEMPORARY);
                                if (wN > 0 || wT > 0)
                                {
                                    warp.reduce(IPlayerWarp.EnumWarpType.NORMAL, MathHelper.clamp(wN, 0, 5));
                                    warp.reduce(IPlayerWarp.EnumWarpType.TEMPORARY, wT);
                                    warp.sync((EntityPlayerMP) player);
                                }
                            }
                            catch (Throwable ignored)
                            {
                            }
                        }
                    }
                }
            }
        }
    }

    public void setFlags(EntityPlayer player, int flags)
    {
    }

    public void setFlagsClient(int flags)
    {
    }

}
