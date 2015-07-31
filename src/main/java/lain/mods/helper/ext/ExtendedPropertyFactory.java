package lain.mods.helper.ext;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ExtendedPropertyFactory
{

    public static ExtendedProperty<Boolean> createBoolean()
    {
        return createBoolean(SyncTag.NoSync);
    }

    public static ExtendedProperty<Boolean> createBoolean(SyncTag tag)
    {
        final SyncTag _tmp = tag;
        return new ExtendedProperty<Boolean>()
        {

            @Override
            public SyncTag getSyncTag()
            {
                return _tmp;
            }

            @Override
            public void readFromBuffer(ByteBuf buf)
            {
                set(buf.readBoolean());
            }

            @Override
            public void writeToBuffer(ByteBuf buf)
            {
                buf.writeBoolean(get());
            }

        };
    }

    public static ExtendedProperty<Byte> createByte()
    {
        return createByte(SyncTag.NoSync);
    }

    public static ExtendedProperty<Byte> createByte(SyncTag tag)
    {
        final SyncTag _tmp = tag;
        return new ExtendedProperty<Byte>()
        {

            @Override
            public SyncTag getSyncTag()
            {
                return _tmp;
            }

            @Override
            public void readFromBuffer(ByteBuf buf)
            {
                set(buf.readByte());
            }

            @Override
            public void writeToBuffer(ByteBuf buf)
            {
                buf.writeByte(get());
            }

        };
    }

    public static ExtendedProperty<Double> createDouble()
    {
        return createDouble(SyncTag.NoSync);
    }

    public static ExtendedProperty<Double> createDouble(SyncTag tag)
    {
        final SyncTag _tmp = tag;
        return new ExtendedProperty<Double>()
        {

            @Override
            public SyncTag getSyncTag()
            {
                return _tmp;
            }

            @Override
            public void readFromBuffer(ByteBuf buf)
            {
                set(buf.readDouble());
            }

            @Override
            public void writeToBuffer(ByteBuf buf)
            {
                buf.writeDouble(get());
            }

        };
    }

    public static ExtendedProperty<Float> createFloat()
    {
        return createFloat(SyncTag.NoSync);
    }

    public static ExtendedProperty<Float> createFloat(SyncTag tag)
    {
        final SyncTag _tmp = tag;
        return new ExtendedProperty<Float>()
        {

            @Override
            public SyncTag getSyncTag()
            {
                return _tmp;
            }

            @Override
            public void readFromBuffer(ByteBuf buf)
            {
                set(buf.readFloat());
            }

            @Override
            public void writeToBuffer(ByteBuf buf)
            {
                buf.writeFloat(get());
            }

        };
    }

    public static ExtendedProperty<Integer> createInt()
    {
        return createInt(SyncTag.NoSync);
    }

    public static ExtendedProperty<Integer> createInt(SyncTag tag)
    {
        final SyncTag _tmp = tag;
        return new ExtendedProperty<Integer>()
        {

            @Override
            public SyncTag getSyncTag()
            {
                return _tmp;
            }

            @Override
            public void readFromBuffer(ByteBuf buf)
            {
                set(buf.readInt());
            }

            @Override
            public void writeToBuffer(ByteBuf buf)
            {
                buf.writeInt(get());
            }

        };
    }

    public static ExtendedProperty<Long> createLong()
    {
        return createLong(SyncTag.NoSync);
    }

    public static ExtendedProperty<Long> createLong(SyncTag tag)
    {
        final SyncTag _tmp = tag;
        return new ExtendedProperty<Long>()
        {

            @Override
            public SyncTag getSyncTag()
            {
                return _tmp;
            }

            @Override
            public void readFromBuffer(ByteBuf buf)
            {
                set(buf.readLong());
            }

            @Override
            public void writeToBuffer(ByteBuf buf)
            {
                buf.writeLong(get());
            }

        };
    }

    public static ExtendedProperty<Short> createShort()
    {
        return createShort(SyncTag.NoSync);
    }

    public static ExtendedProperty<Short> createShort(SyncTag tag)
    {
        final SyncTag _tmp = tag;
        return new ExtendedProperty<Short>()
        {

            @Override
            public SyncTag getSyncTag()
            {
                return _tmp;
            }

            @Override
            public void readFromBuffer(ByteBuf buf)
            {
                set(buf.readShort());
            }

            @Override
            public void writeToBuffer(ByteBuf buf)
            {
                buf.writeShort(get());
            }

        };
    }

    public static ExtendedProperty<String> createString()
    {
        return createString(SyncTag.NoSync);
    }

    public static ExtendedProperty<String> createString(SyncTag tag)
    {
        final SyncTag _tmp = tag;
        return new ExtendedProperty<String>()
        {

            @Override
            public SyncTag getSyncTag()
            {
                return _tmp;
            }

            @Override
            public void readFromBuffer(ByteBuf buf)
            {
                set(ByteBufUtils.readUTF8String(buf));
            }

            @Override
            public void writeToBuffer(ByteBuf buf)
            {
                ByteBufUtils.writeUTF8String(buf, get());
            }

        };
    }

}
