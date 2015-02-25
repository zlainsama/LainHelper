package lain.mods.helper.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMTransformer implements IClassTransformer
{

    class transformer001 extends ClassVisitor
    {

        class method001 extends MethodVisitor
        {

            public method001(MethodVisitor mv)
            {
                super(Opcodes.ASM5, mv);
            }

            @Override
            public void visitInsn(int opcode)
            {
                if (opcode == Opcodes.IRETURN)
                {
                    this.visitVarInsn(Opcodes.ISTORE, 2);
                    this.visitVarInsn(Opcodes.ALOAD, 0);
                    this.visitVarInsn(Opcodes.ALOAD, 1);
                    this.visitVarInsn(Opcodes.ILOAD, 2);
                    this.visitMethodInsn(Opcodes.INVOKESTATIC, "lain/mods/helper/asm/Hooks", "isPotionApplicable", "(Lnet/minecraft/entity/player/EntityPlayerMP;Lnet/minecraft/potion/PotionEffect;Z)Z", false);
                }
                super.visitInsn(opcode);
            }

        }

        class method002 extends MethodVisitor
        {

            public method002(MethodVisitor mv)
            {
                super(Opcodes.ASM5, mv);
            }

            @Override
            public void visitCode()
            {
                super.visitCode();
                this.visitVarInsn(Opcodes.ALOAD, 0);
                this.visitMethodInsn(Opcodes.INVOKESTATIC, "lain/mods/helper/asm/Hooks", "onLivingUpdate", "(Lnet/minecraft/entity/player/EntityPlayerMP;)V", false);
            }

        }

        class method003 extends MethodVisitor
        {

            public method003(MethodVisitor mv)
            {
                super(Opcodes.ASM5, mv);
            }

            @Override
            public void visitInsn(int opcode)
            {
                if (opcode == Opcodes.IRETURN)
                {
                    this.visitVarInsn(Opcodes.ISTORE, 1);
                    this.visitVarInsn(Opcodes.ALOAD, 0);
                    this.visitVarInsn(Opcodes.ILOAD, 1);
                    this.visitMethodInsn(Opcodes.INVOKESTATIC, "lain/mods/helper/asm/Hooks", "shouldHeal", "(Lnet/minecraft/entity/player/EntityPlayerMP;Z)Z", false);
                }
                super.visitInsn(opcode);
            }

        }

        ObfHelper parent = ObfHelper.newClass("net/minecraft/entity/player/EntityPlayer");

        ObfHelper m001 = ObfHelper.newMethod("func_70687_e", "net/minecraft/entity/EntityLivingBase", "(Lnet/minecraft/potion/PotionEffect;)Z"); // isPotionApplicable
        boolean foundM001 = false;
        ObfHelper m002 = ObfHelper.newMethod("func_70636_d", "net/minecraft/entity/EntityLivingBase", "()V"); // onLivingUpdate
        boolean foundM002 = false;
        ObfHelper m003 = ObfHelper.newMethod("func_70996_bM", "net/minecraft/entity/player/EntityPlayer", "()Z"); // shouldHeal
        boolean foundM003 = false;

        public transformer001(ClassVisitor cv)
        {
            super(Opcodes.ASM5, cv);
        }

        @Override
        public void visitEnd()
        {

            if (!foundM001)
            {
                foundM001 = true;
                MethodVisitor mv = this.visitMethod(Opcodes.ACC_PUBLIC, m001.getData(1), m001.getData(2), null, null);
                mv.visitCode();
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, parent.getData(0), m001.getData(1), m001.getData(2), false);
                mv.visitInsn(Opcodes.IRETURN);
                mv.visitMaxs(2, 2);
                mv.visitEnd();
            }
            if (!foundM002)
            {
                foundM002 = true;
                MethodVisitor mv = this.visitMethod(Opcodes.ACC_PUBLIC, m002.getData(1), m002.getData(2), null, null);
                mv.visitCode();
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, parent.getData(0), m002.getData(1), m002.getData(2), false);
                mv.visitInsn(Opcodes.RETURN);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
            if (!foundM003)
            {
                foundM003 = true;
                MethodVisitor mv = this.visitMethod(Opcodes.ACC_PUBLIC, m003.getData(1), m003.getData(2), null, null);
                mv.visitCode();
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, parent.getData(0), m003.getData(1), m003.getData(2), false);
                mv.visitInsn(Opcodes.IRETURN);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
            super.visitEnd();
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
        {
            if (m001.match(name, desc))
            {
                foundM001 = true;
                return new method001(super.visitMethod(access, name, desc, signature, exceptions));
            }
            if (m002.match(name, desc))
            {
                foundM002 = true;
                return new method002(super.visitMethod(access, name, desc, signature, exceptions));
            }
            if (m003.match(name, desc))
            {
                foundM003 = true;
                return new method003(super.visitMethod(access, name, desc, signature, exceptions));
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        if ("net.minecraft.entity.player.EntityPlayerMP".equals(transformedName))
            return transform001(bytes);
        return bytes;
    }

    private byte[] transform001(byte[] bytes)
    {
        ClassReader classReader = new ClassReader(bytes);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classReader.accept(new transformer001(classWriter), ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }

}
