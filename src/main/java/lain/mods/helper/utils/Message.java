package lain.mods.helper.utils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public enum Message
{

    msgNotPlayer("LH_NotPlayer"),
    msgTeleportationFailed("LH_TeleportationFailed"),
    msgLastPosNotFound("LH_LastPosNotFound"),
    msgBackDone("LH_BackDone"),
    msgHomeNotFound("LH_HomeNotFound"),
    msgHomeDone("LH_HomeDone"),
    msgSetHomeDone("LH_SetHomeDone"),
    msgOverworldHomeOnly("LH_OverworldHomeOnly"),
    msgSpawnDone("LH_SpawnDone"),
    msgBackUsage("LH_Back_Usage"),
    msgHomeUsage("LH_Home_Usage"),
    msgSetHomeUsage("LH_SetHome_Usage"),
    msgSpawnUsage("LH_Spawn_Usage"),
    msgSharedStorageUsage("LH_SharedStorage_Usage"),
    msgSharedStorageTitle("LH_SharedStorage_Title");

    public final String key;

    private Message(String key)
    {
        this.key = key;
    }

    public ITextComponent convert()
    {
        ITextComponent tmp = new TextComponentTranslation(key);
        return tmp;
    }

    public ITextComponent convert(Object... objects)
    {
        ITextComponent tmp = new TextComponentTranslation(key, objects);
        return tmp;
    }

    public ITextComponent convert(TextFormatting color)
    {
        ITextComponent tmp = new TextComponentTranslation(key);
        tmp.getStyle().setColor(color);
        return tmp;
    }

    public ITextComponent convert(TextFormatting color, Object... objects)
    {
        ITextComponent tmp = new TextComponentTranslation(key, objects);
        tmp.getStyle().setColor(color);
        return tmp;
    }

}
