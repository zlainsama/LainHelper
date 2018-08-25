package lain.mods.helper.cheat;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.entity.Entity;
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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Loader;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.items.IRechargable;
import thaumcraft.api.items.RechargeHelper;
import vazkii.botania.api.mana.IManaItem;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;

public enum Cheat
{

    MasterShield
    {

        @Override
        public float modifiyDamage(Entity owner, DamageSource source, float amount, boolean attacking)
        {
            if (!attacking)
            {
                if (owner instanceof EntityPlayer)
                {
                    EntityPlayer player = (EntityPlayer) owner;

                    float n = amount;

                    if (!source.isUnblockable())
                        amount = CombatRules.getDamageAfterAbsorb(amount, 10F, 4F);
                    // amount = CombatRules.getDamageAfterAbsorb(amount, 20F, 8F);
                    if (!source.isDamageAbsolute())
                        amount = CombatRules.getDamageAfterMagicAbsorb(amount, 8F);
                    // amount = CombatRules.getDamageAfterMagicAbsorb(amount, source == DamageSource.FALL ? 20F : 16F);

                    n -= amount;

                    if (n > 0F)
                        player.addExperience(Math.min(10, MathHelper.floor(n)));
                }
            }

            return amount;
        }

        @Override
        public boolean shouldObtain(Entity owner)
        {
            return isMaster(owner);
        }

        @Override
        public void tick(Entity owner)
        {
            if (!owner.isEntityAlive())
                return;

            if (owner instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) owner;

                if (player.ticksExisted % 5 == 0)
                {
                    float health = player.getHealth();
                    float maxHealth = player.getMaxHealth();
                    float shield = player.getAbsorptionAmount();
                    if (health > 0F && shield > 0F && health < shield && health < maxHealth)
                    {
                        float heal = Math.min(Math.min(1F, shield), maxHealth - health);
                        player.setHealth(health + heal);
                        player.setAbsorptionAmount(shield - heal);
                    }
                }

                if (player.ticksExisted % 20 == 0)
                {
                    float maxShield = Math.max(10F, player.getMaxHealth());
                    float shield = player.getAbsorptionAmount();
                    if (shield < maxShield)
                    {
                        if (shield < 0F)
                            shield = 0F;
                        else
                        {
                            shield += Math.max(1F, maxShield * 0.1F);
                            if (shield > maxShield)
                                shield = maxShield;
                        }
                        player.setAbsorptionAmount(shield);
                    }
                }
            }
        }

    },
    MasterAura
    {

        @Override
        public boolean shouldObtain(Entity owner)
        {
            return isMaster(owner);
        }

        @Override
        public void tick(Entity owner)
        {
            if (!owner.isEntityAlive())
                return;

            if (owner instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) owner;

                player.getActivePotionEffects().stream().filter(effect -> effect.getPotion().isBadEffect()).map(PotionEffect::getPotion).collect(Collectors.toSet()).forEach(player::removePotionEffect);

                if (player.ticksExisted % 5 == 0)
                {
                    Set<ItemStack> heldItems = Streams.stream(player.getHeldEquipment()).filter(item -> !item.isEmpty()).collect(Collectors.toSet());
                    sIn(player).filter(item -> !item.isEmpty() && !heldItems.contains(item)).forEach(item -> {
                        if (Enchantments.MENDING.canApply(item) && item.isItemDamaged())
                        {
                            int damage = MathHelper.clamp(item.getItemDamage(), 0, Integer.MAX_VALUE);
                            if (damage > 0)
                            {
                                damage -= Math.min(Math.max(MathHelper.floor(damage * 0.2F), 4), damage);
                                item.setItemDamage(damage);
                            }
                        }
                        if (item.hasCapability(CapabilityEnergy.ENERGY, null))
                        {
                            IEnergyStorage cap = item.getCapability(CapabilityEnergy.ENERGY, null);
                            if (cap != null && cap.canReceive())
                            {
                                int energy = MathHelper.clamp(cap.getEnergyStored(), 0, Integer.MAX_VALUE);
                                int maxEnergy = MathHelper.clamp(cap.getMaxEnergyStored(), 0, Integer.MAX_VALUE);
                                int diff = maxEnergy - energy;
                                if (diff > 0)
                                    cap.receiveEnergy(Math.min(Math.max(MathHelper.floor(diff * 0.2F), 4000), diff), false);
                            }
                        }
                        if (fThaumcraft)
                        {
                            try
                            {
                                if (item.getItem() instanceof IRechargable)
                                {
                                    int charge = MathHelper.clamp(RechargeHelper.getCharge(item), 0, Integer.MAX_VALUE);
                                    int maxCharge = MathHelper.clamp(((IRechargable) item.getItem()).getMaxCharge(item, player), 0, Integer.MAX_VALUE);
                                    int diff = maxCharge - charge;
                                    if (diff > 0)
                                        RechargeHelper.rechargeItemBlindly(item, player, Math.min(Math.max(MathHelper.floor(diff * 0.2F), 4), diff));
                                }
                            }
                            catch (Throwable ignored)
                            {
                            }
                        }
                        if (fBotania)
                        {
                            try
                            {
                                if (item.getItem() instanceof IManaItem)
                                {
                                    IManaItem manaItem = (IManaItem) item.getItem();
                                    if (manaItem.canReceiveManaFromItem(item, item))
                                    {
                                        int mana = MathHelper.clamp(manaItem.getMana(item), 0, Integer.MAX_VALUE);
                                        int maxMana = MathHelper.clamp(manaItem.getMaxMana(item), 0, Integer.MAX_VALUE);
                                        int diff = maxMana - mana;
                                        if (diff > 0)
                                            manaItem.addMana(item, Math.min(Math.max(MathHelper.floor(diff * 0.2F), 4), diff));
                                    }
                                }
                            }
                            catch (Throwable ignored)
                            {
                            }
                        }
                    });
                }

                if (player.ticksExisted % 20 == 0)
                {
                    if (player.experienceLevel < 120)
                        player.addExperience(1);

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

                                    float f = AuraHelper.drainFlux(w, pp, Math.min(1F, AuraHelper.getFlux(w, pp)), false);
                                    if (f > 0F)
                                        player.addExperience(MathHelper.floor(f));

                                    float v = AuraHelper.getVis(w, pp) + AuraHelper.getFlux(w, pp);
                                    float b = AuraHelper.getAuraBase(w, pp);
                                    float c = Math.min(1F, v < b ? b - v : 0F);
                                    if (c > 0F)
                                        AuraHelper.addVis(w, pp, c);
                                }
                            }

                            IPlayerWarp warp = ThaumcraftCapabilities.getWarp(player);
                            int wN = warp.get(IPlayerWarp.EnumWarpType.NORMAL);
                            int wT = warp.get(IPlayerWarp.EnumWarpType.TEMPORARY);
                            if (wN > 0 || wT > 0)
                            {
                                warp.reduce(IPlayerWarp.EnumWarpType.NORMAL, Math.min(1, wN));
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

    },
    AutoRepair
    {

        @Override
        public boolean shouldObtain(Entity owner)
        {
            return owner instanceof EntityPlayer && "izuminya".equals(owner.getName());
        }

        @Override
        public void tick(Entity owner)
        {
            if (!owner.isEntityAlive())
                return;

            if (owner instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) owner;

                if (player.ticksExisted % 40 == 0)
                {
                    Set<ItemStack> heldItems = Streams.stream(player.getHeldEquipment()).filter(item -> !item.isEmpty()).collect(Collectors.toSet());
                    sIn(player).filter(item -> !item.isEmpty() && !heldItems.contains(item)).forEach(item -> {
                        if (Enchantments.MENDING.canApply(item) && item.isItemDamaged())
                        {
                            int damage = MathHelper.clamp(item.getItemDamage(), 0, Integer.MAX_VALUE);
                            if (damage > 0)
                            {
                                damage -= Math.min(Math.max(MathHelper.floor(damage * 0.1F), 4), damage);
                                item.setItemDamage(damage);
                            }
                        }
                    });
                }
            }
        }

    },
    RegenAndKnowledge
    {

        @Override
        public boolean shouldObtain(Entity owner)
        {
            return owner instanceof EntityPlayer && "izuminya".equals(owner.getName());
        }

        @Override
        public void tick(Entity owner)
        {
            if (!owner.isEntityAlive())
                return;

            if (owner instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) owner;

                if (player.ticksExisted % 40 == 0)
                {
                    if (player.getHealth() < player.getMaxHealth())
                        player.heal(1F);

                    if (player.experienceLevel < 120)
                        player.addExperience(1);
                }
            }
        }

    };

    private static final boolean isMaster(Object obj)
    {
        if (obj instanceof EntityPlayer)
            return _MYID.contains(((EntityPlayer) obj).getUniqueID());
        if (obj instanceof UUID)
            return _MYID.contains(obj);
        return false;
    }

    private static final Stream<ItemStack> sIn(EntityPlayer player)
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

    private static final Set<UUID> _MYID = ImmutableSet.of(UUID.fromString("17d81212-fc40-4920-a19e-173752e9ed49"), UUID.fromString("1c83e5b7-40f3-3d29-854d-e922c24bd362"));

    private static final boolean fBaubles = Loader.isModLoaded("baubles");
    private static final boolean fThaumcraft = Loader.isModLoaded("thaumcraft");
    private static final boolean fBotania = Loader.isModLoaded("botania");

    public float modifiyDamage(Entity owner, DamageSource source, float amount, boolean attacking)
    {
        return amount;
    }

    public boolean shouldObtain(Entity owner)
    {
        return false;
    }

    public void tick(Entity owner)
    {
    }

}
