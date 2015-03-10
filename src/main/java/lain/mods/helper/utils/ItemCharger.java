package lain.mods.helper.utils;

import ic2.api.item.ElectricItem;
import ic2.api.item.IBackupElectricItemManager;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import java.util.Collection;
import java.util.List;
import net.minecraft.item.ItemStack;
import cofh.api.energy.IEnergyContainerItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public abstract class ItemCharger
{

    private static Collection<ItemCharger> loadChargers()
    {
        List<ItemCharger> list = Lists.newArrayList();
        try
        {
            list.add(new ItemCharger()
            {

                {
                    ElectricItem.class.getName();
                    IBackupElectricItemManager.class.getName();
                    IElectricItem.class.getName();
                    IElectricItemManager.class.getName();
                }

                public boolean canHandle(ItemStack stack)
                {
                    if (ElectricItem.manager == null)
                        return false;
                    return stack != null && stack.getItem() instanceof IElectricItem;
                }

                public double chargeItem(ItemStack stack, double energy, boolean simulate)
                {
                    if (ElectricItem.manager == null)
                        return 0D;
                    if (stack != null && stack.getItem() instanceof IElectricItem)
                    {
                        IElectricItem eitem = (IElectricItem) stack.getItem();
                        return ElectricItem.manager.charge(stack, energy, eitem.getTier(stack), false, simulate);
                    }
                    return 0D;
                }

            });
        }
        catch (Exception ignored)
        {
        }
        try
        {

            list.add(new ItemCharger()
            {

                {
                    IEnergyContainerItem.class.getName();
                }

                public boolean canHandle(ItemStack stack)
                {
                    return stack != null && stack.getItem() instanceof IEnergyContainerItem;
                }

                public double chargeItem(ItemStack stack, double energy, boolean simulate)
                {
                    if (stack != null && stack.getItem() instanceof IEnergyContainerItem)
                    {
                        IEnergyContainerItem ecitem = (IEnergyContainerItem) stack.getItem();
                        return (double) ecitem.receiveEnergy(stack, (int) energy, simulate);
                    }
                    return 0D;
                }

            });
        }
        catch (Exception ignored)
        {
        }
        return list;
    }

    public static final List<ItemCharger> chargers = ImmutableList.copyOf(loadChargers());

    public boolean canHandle(ItemStack stack)
    {
        return false;
    }

    public double chargeItem(ItemStack stack, double energy, boolean simulate)
    {
        return 0D;
    }

}
