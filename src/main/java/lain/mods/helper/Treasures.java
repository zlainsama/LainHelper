package lain.mods.helper;

import java.util.List;
import lain.mods.helper.util.Translator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.FakePlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import com.google.common.collect.Lists;

public class Treasures
{

    public static void setup()
    {
        ItemStack item = null;

        if (LainHelper.killingSwords)
        {
            item = new ItemStack(Item.swordIron);
            item.getTagCompound().setBoolean("killingSwords:humanKilling", true);
            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING, new WeightedRandomChestContent(item, 1, 1, 1));
            item = new ItemStack(Item.swordIron);
            item.getTagCompound().setBoolean("killingSwords:monsterKilling", true);
            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING, new WeightedRandomChestContent(item, 1, 1, 1));
            item = new ItemStack(Item.swordIron);
            item.getTagCompound().setBoolean("killingSwords:animalKilling", true);
            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING, new WeightedRandomChestContent(item, 1, 1, 1));
            item = new ItemStack(Item.swordIron);
            item.getTagCompound().setBoolean("killingSwords:otherKilling", true);
            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING, new WeightedRandomChestContent(item, 1, 1, 1));
            item = new ItemStack(Item.swordIron);
            item.getTagCompound().setBoolean("killingSwords:livingKilling", true);
            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING, new WeightedRandomChestContent(item, 1, 1, 1));
        }

        MinecraftForge.EVENT_BUS.register(new Treasures());
    }

    Translator tooltip_killingSwords_humanKilling = new Translator("LH_Tooltip_KillingSwords_HumanKilling");
    Translator tooltip_killingSwords_monsterKilling = new Translator("LH_Tooltip_KillingSwords_MonsterKilling");
    Translator tooltip_killingSwords_animalKilling = new Translator("LH_Tooltip_KillingSwords_AnimalKilling");
    Translator tooltip_killingSwords_otherKilling = new Translator("LH_Tooltip_KillingSwords_OtherKilling");
    Translator tooltip_killingSwords_livingKilling = new Translator("LH_Tooltip_KillingSwords_LivingKilling");

    @ForgeSubscribe
    public void onGetTooltip(ItemTooltipEvent event)
    {
        if (LainHelper.killingSwords)
        {
            NBTTagCompound tag = event.itemStack.getTagCompound();
            List<String> list = Lists.newArrayList();
            if (tag.getBoolean("killingSwords:humanKilling"))
                list.add(ChatMessageComponent.createFromText(tooltip_killingSwords_humanKilling.key).toStringWithDefaultFormatting(false, EnumChatFormatting.DARK_RED, false, false, false, false));
            if (tag.getBoolean("killingSwords:monsterKilling"))
                list.add(ChatMessageComponent.createFromText(tooltip_killingSwords_monsterKilling.key).toStringWithDefaultFormatting(false, EnumChatFormatting.DARK_RED, false, false, false, false));
            if (tag.getBoolean("killingSwords:animalKilling"))
                list.add(ChatMessageComponent.createFromText(tooltip_killingSwords_animalKilling.key).toStringWithDefaultFormatting(false, EnumChatFormatting.DARK_RED, false, false, false, false));
            if (tag.getBoolean("killingSwords:otherKilling"))
                list.add(ChatMessageComponent.createFromText(tooltip_killingSwords_otherKilling.key).toStringWithDefaultFormatting(false, EnumChatFormatting.DARK_RED, false, false, false, false));
            if (tag.getBoolean("killingSwords:livingKilling"))
                list.add(ChatMessageComponent.createFromText(tooltip_killingSwords_livingKilling.key).toStringWithDefaultFormatting(false, EnumChatFormatting.DARK_RED, false, false, false, false));
            event.toolTip.addAll(0, list);
        }
    }

    @ForgeSubscribe
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (event.entity.worldObj == null)
            return;
        if (LainHelper.killingSwords)
        {
            if (event.entity.worldObj.getTotalWorldTime() % 10 == 0)
            {
                ItemStack holding = event.entityLiving.getHeldItem();
                if (holding != null)
                {
                    NBTTagCompound tag = holding.getTagCompound();
                    int flags = 0;
                    if (tag.getBoolean("killingSwords:humanKilling"))
                        flags |= 0x1;
                    if (tag.getBoolean("killingSwords:monsterKilling"))
                        flags |= 0x2;
                    if (tag.getBoolean("killingSwords:animalKilling"))
                        flags |= 0x4;
                    if (tag.getBoolean("killingSwords:otherKilling"))
                        flags |= 0x8;
                    if (tag.getBoolean("killingSwords:livingKilling"))
                        flags |= 0x1 | 0x2 | 0x4 | 0x8;
                    List<EntityLivingBase> list = Lists.newArrayList();
                    for (EntityLivingBase ent : (List<EntityLivingBase>) event.entity.worldObj.getEntitiesWithinAABB(EntityLiving.class, event.entity.boundingBox.expand(3, 3, 3)))
                    {
                        if (ent == event.entityLiving || !ent.isEntityAlive() || ent instanceof FakePlayer)
                            continue;
                        if (ent instanceof EntityPlayer)
                        {
                            if ((flags & 0x1) != 0)
                                list.add(ent);
                        }
                        else if (ent instanceof IMob)
                        {
                            if ((flags & 0x2) != 0)
                                list.add(ent);
                        }
                        else if (ent instanceof IAnimals)
                        {
                            if ((flags & 0x4) != 0)
                                list.add(ent);
                        }
                        else
                        {
                            if ((flags & 0x8) != 0)
                                list.add(ent);
                        }
                    }
                    if (event.entity.worldObj.isRemote)
                    {
                        if (!list.isEmpty())
                            event.entityLiving.swingItem();
                    }
                    else
                    {
                        if (!list.isEmpty())
                        {
                            if (event.entity instanceof EntityPlayerMP)
                            {
                                EntityPlayerMP plr = (EntityPlayerMP) event.entity;
                                for (EntityLivingBase ent : list)
                                    plr.attackTargetEntityWithCurrentItem(ent);
                            }
                            else
                            {
                                for (EntityLivingBase ent : list)
                                    event.entityLiving.attackEntityAsMob(ent);
                            }
                        }
                    }
                }
            }
        }
    }

}
