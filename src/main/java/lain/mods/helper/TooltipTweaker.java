package lain.mods.helper;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class TooltipTweaker
{

    public static void setup()
    {
        MinecraftForge.EVENT_BUS.register(new TooltipTweaker());
    }

    @ForgeSubscribe
    public void getToolTip(ItemTooltipEvent event)
    {
        if (!event.showAdvancedItemTooltips)
        {
            List<String> toRemove = Lists.newArrayList();
            Multimap multimap = event.itemStack.getAttributeModifiers();
            if (!multimap.isEmpty())
            {
                toRemove.add("");
                Iterator iterator = multimap.entries().iterator();
                while (iterator.hasNext())
                {
                    Entry entry = (Entry) iterator.next();
                    AttributeModifier attributemodifier = (AttributeModifier) entry.getValue();
                    double d0 = attributemodifier.getAmount();
                    double d1;
                    if (attributemodifier.getOperation() != 1 && attributemodifier.getOperation() != 2)
                        d1 = attributemodifier.getAmount();
                    else
                        d1 = attributemodifier.getAmount() * 100.0D;
                    if (d0 > 0.0D)
                    {
                        toRemove.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier.getOperation(), event.itemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + (String) entry.getKey())));
                    }
                    else if (d0 < 0.0D)
                    {
                        d1 *= -1.0D;
                        toRemove.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + attributemodifier.getOperation(), event.itemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + (String) entry.getKey())));
                    }
                }
            }
            event.toolTip.removeAll(toRemove);
        }
    }

}
