package lain.mods.helper.customport;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class CustomPortClassTransformer implements IClassTransformer
{

    class a extends ClassVisitor
    {

        public a(ClassVisitor cv)
        {
            super(262144, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
        {
            return new b(super.visitMethod(access, name, desc, signature, exceptions));
        }

    }

    class b extends MethodVisitor
    {

        Set<String> names;
        String cl;

        public b(MethodVisitor mv)
        {
            super(262144, mv);
            cl = "net/minecraft/util/HttpUtil";
            names = new HashSet<String>();
            names.add("a");
            names.add("func_76181_a");
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc)
        {
            if (cl.equals(FMLDeobfuscatingRemapper.INSTANCE.map(owner)) && names.contains(FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(owner, name, desc)) && "()I".equals(desc))
            {
                owner = "lain/mods/helper/customport/CustomPort";
                name = "getCustomPort";
            }
            super.visitMethodInsn(opcode, owner, name, desc);
        }

    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        if ("net.minecraft.server.integrated.IntegratedServerListenThread".equals(transformedName))
            return transform001(bytes);
        return bytes;
    }

    private byte[] transform001(byte[] bytes)
    {
        ClassReader classReader = new ClassReader(bytes);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classReader.accept(new a(classWriter), ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }

}
