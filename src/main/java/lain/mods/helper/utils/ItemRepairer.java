package lain.mods.helper.utils;

import java.util.Collection;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public abstract class ItemRepairer
{

    private static Collection<ItemRepairer> loadRepairers()
    {
        List<ItemRepairer> list = Lists.newArrayList();
        try
        {
            list.add(new ItemRepairer()
            {

                @Override
                public boolean canHandle(ItemStack stack)
                {
                    if (stack != null)
                    {
                        NBTTagCompound data = stack.getTagCompound();
                        if (data != null && data.hasKey("GT.ToolStats") && (data = data.getCompoundTag("GT.ToolStats")) != null)
                        {
                            if (!data.getBoolean("Electric"))
                                return true;
                        }
                    }
                    return super.canHandle(stack);
                }

                @Override
                public double repairItem(ItemStack stack, double amount, boolean simulate)
                {
                    if (stack != null)
                    {
                        NBTTagCompound data = stack.getTagCompound();
                        if (data != null && data.hasKey("GT.ToolStats") && (data = data.getCompoundTag("GT.ToolStats")) != null)
                        {
                            if (!data.getBoolean("Electric"))
                            {
                                long damage0 = data.getLong("Damage");
                                if (damage0 <= 0L)
                                    return 0L;
                                long damage1 = damage0 - (long) amount;
                                if (damage1 < 0L)
                                    damage1 = 0L;
                                if (!simulate)
                                    data.setLong("Damage", damage1);
                                return (double) -(damage1 - damage0);
                            }
                        }
                    }
                    return super.repairItem(stack, amount, simulate);
                }

            });
        }
        catch (Throwable ignored)
        {
        }
        try
        {
            list.add(new ItemRepairer()
            {

                @Override
                public boolean canHandle(ItemStack stack)
                {
                    if (stack != null)
                    {
                        NBTTagCompound data = stack.getTagCompound();
                        if (data != null && data.hasKey("InfiTool") && (data = data.getCompoundTag("InfiTool")) != null)
                        {
                            if (!data.hasKey("Energy"))
                                return true;
                        }
                    }
                    return super.canHandle(stack);
                }

                @Override
                public double repairItem(ItemStack stack, double amount, boolean simulate)
                {
                    if (stack != null)
                    {
                        NBTTagCompound data = stack.getTagCompound();
                        if (data != null && data.hasKey("InfiTool") && (data = data.getCompoundTag("InfiTool")) != null)
                        {
                            if (!data.hasKey("Energy"))
                            {
                                int damage0 = data.getInteger("Damage");
                                if (damage0 <= 0)
                                    return 0D;
                                int damage1 = damage0 - (int) amount;
                                if (damage1 < 0)
                                    damage1 = 0;
                                if (!simulate)
                                {
                                    data.setInteger("Damage", damage1);
                                    if (damage1 < damage0 && data.getBoolean("Broken"))
                                        data.setBoolean("Broken", false);
                                }
                                return (double) -(damage1 - damage0);
                            }
                        }
                    }
                    return super.repairItem(stack, amount, simulate);
                }

            });
        }
        catch (Throwable ignored)
        {
        }
        return list;
    }

    public static final List<ItemRepairer> repairers = ImmutableList.copyOf(loadRepairers());

    public boolean canHandle(ItemStack stack)
    {
        return stack != null && stack.getItem().isRepairable();
    }

    public double repairItem(ItemStack stack, double amount, boolean simulate)
    {
        if (stack != null && stack.getItem().isRepairable())
        {
            if (simulate)
                stack = stack.copy();
            int damage0 = stack.getItemDamage();
            if (damage0 <= 0)
                return 0D;
            int damage1 = damage0 - (int) amount;
            if (damage1 < 0)
                damage1 = 0;
            if (!simulate)
                stack.setItemDamage(damage1);
            return (double) -(damage1 - damage0);
        }
        return 0D;
    }

}
