package lain.mods.helper.note.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class NoteSyncPacket
{

    public static String readShortString(ByteBuf buf)
    {
        int length = buf.readByte();
        if (length > 0)
            return buf.readBytes(length).toString(Charsets.UTF_8);
        return "";
    }

    public static void writeShortString(ByteBuf buf, String str)
    {
        str = Strings.nullToEmpty(str);
        byte[] bytes = str.getBytes(Charsets.UTF_8);
        int length = bytes.length > 0xFF ? 0xFF : bytes.length;
        buf.writeByte(length);
        if (length > 0)
            buf.writeBytes(bytes, 0, length);
    }

    public String name;
    public byte opcode;
    public boolean locked;
    public String value;

    public NoteSyncPacket()
    {
        name = "";
        value = "";
    }

    public NoteSyncPacket(ByteBuf buf)
    {
        name = readShortString(buf);
        opcode = buf.readByte();
        switch (opcode)
        {
            case 0: // if name is empty then delete all, otherwise nothing
                break;
            case 1: // if name is not empty then delete named option
                break;
            case 2: // if name is not empty then set or create named option with given data
                locked = buf.readBoolean();
                value = readShortString(buf);
                break;
        }
    }

    public NoteSyncPacket(String name, int opcode, boolean locked, String value)
    {
        this.name = Strings.nullToEmpty(name);
        this.opcode = (byte) opcode;
        this.locked = locked;
        this.value = Strings.nullToEmpty(value);
    }

    public FMLProxyPacket createPacket()
    {
        ByteBuf buf = Unpooled.buffer();
        writeToBuf(buf);
        return new FMLProxyPacket(buf, NoteSync.CHANNELNAME);
    }

    public void writeToBuf(ByteBuf buf)
    {
        writeShortString(buf, name);
        buf.writeByte(opcode);
        switch (opcode)
        {
            case 0: // if name is empty then delete all, otherwise nothing
                break;
            case 1: // if name is not empty then delete named option
                break;
            case 2: // if name is not empty then set or create named option with given data
                buf.writeBoolean(locked);
                writeShortString(buf, value);
                break;
        }
    }

}
