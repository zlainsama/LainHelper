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

                public double chargeItem(ItemStack stack, double energy, boolean ignoreTransferLimit, boolean simulate)
                {
                    if (ElectricItem.manager == null)
                        return 0D;
                    if (stack != null && stack.getItem() instanceof IElectricItem)
                    {
                        IElectricItem eitem = (IElectricItem) stack.getItem();
                        return ElectricItem.manager.charge(stack, energy, eitem.getTier(stack), ignoreTransferLimit, simulate);
                    }
                    return 0D;
                }

            });
        }
        catch (Throwable ignored)
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

                public double chargeItem(ItemStack stack, double energy, boolean ignoreTransferLimit, boolean simulate)
                {
                    if (stack != null && stack.getItem() instanceof IEnergyContainerItem)
                    {
                        IEnergyContainerItem ecitem = (IEnergyContainerItem) stack.getItem();
                        if (!ignoreTransferLimit)
                            return (double) ecitem.receiveEnergy(stack, (int) energy, simulate);
                        int m = (int) energy;
                        int n = m;
                        int c = 0;
                        do
                        {
                            int d = ecitem.receiveEnergy(stack, n, simulate);
                            n -= d;
                            c++;
                            if (d <= 0)
                                break;
                        }
                        while (n > 0 && c < 0xFFF);
                        return (double) (m - n);
                    }
                    return 0D;
                }

            });
        }
        catch (Throwable ignored)
        {
        }
        return list;
    }

    public static final List<ItemCharger> chargers = ImmutableList.copyOf(loadChargers());

    public boolean canHandle(ItemStack stack)
    {
        return false;
    }

    public double chargeItem(ItemStack stack, double energy, boolean ignoreTransferLimit, boolean simulate)
    {
        return 0D;
    }

}
