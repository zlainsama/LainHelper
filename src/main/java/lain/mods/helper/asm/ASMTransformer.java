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
                    this.visitVarInsn(Opcodes.ISTORE, 1);
                    this.visitVarInsn(Opcodes.ALOAD, 0);
                    this.visitVarInsn(Opcodes.ILOAD, 1);
                    this.visitMethodInsn(Opcodes.INVOKESTATIC, "lain/mods/helper/asm/Hooks", "isInvisible", "(Lnet/minecraft/entity/player/EntityPlayerMP;Z)Z", false);
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
            public void visitInsn(int opcode)
            {
                if (opcode == Opcodes.FRETURN)
                {
                    this.visitVarInsn(Opcodes.FSTORE, 1);
                    this.visitVarInsn(Opcodes.ALOAD, 0);
                    this.visitVarInsn(Opcodes.FLOAD, 1);
                    this.visitMethodInsn(Opcodes.INVOKESTATIC, "lain/mods/helper/asm/Hooks", "getArmorVisibility", "(Lnet/minecraft/entity/player/EntityPlayerMP;F)F", false);
                }
                super.visitInsn(opcode);
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
                    mv.visitVarInsn(Opcodes.ISTORE, 2);
                    mv.visitVarInsn(Opcodes.ALOAD, 0);
                    mv.visitVarInsn(Opcodes.ALOAD, 1);
                    mv.visitVarInsn(Opcodes.ILOAD, 2);
                    this.visitMethodInsn(Opcodes.INVOKESTATIC, "lain/mods/helper/asm/Hooks", "isPotionApplicable", "(Lnet/minecraft/entity/player/EntityPlayerMP;Lnet/minecraft/potion/PotionEffect;Z)Z", false);
                }
                super.visitInsn(opcode);
            }

        }

        String mN001 = "ay"; // isInvisible
        String mD001 = "()Z"; // ()Z
        boolean foundM001 = false;
        String mTO001 = "ahd"; // net.minecraft.entity.player.EntityPlayer
        String mTN001 = "ay"; // isInvisible
        String mTD001 = "()Z"; // ()Z
        String mN002 = "bX"; // getArmorVisibility
        String mD002 = "()F"; // ()F
        boolean foundM002 = false;
        String mTO002 = "ahd"; // net.minecraft.entity.player.EntityPlayer
        String mTN002 = "bX"; // getArmorVisibility
        String mTD002 = "()F"; // ()F
        String mN003 = "d"; // isPotionApplicable
        String mD003 = "(Lwq;)Z"; // (Lnet/minecraft/potion/PotionEffect;)Z
        boolean foundM003 = false;
        String mTO003 = "ahd"; // net.minecraft.entity.player.EntityPlayer
        String mTN003 = "d"; // isPotionApplicable
        String mTD003 = "(Lwq;)Z"; // (Lnet/minecraft/potion/PotionEffect;)Z

        public transformer001(ClassVisitor cv)
        {
            super(Opcodes.ASM5, cv);
        }

        @Override
        public void visitEnd()
        {
            if (!foundM001)
            {
                MethodVisitor mv = super.visitMethod(Opcodes.ACC_PUBLIC, mN001, mD001, null, null);
                mv.visitCode();
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, mTO001, mTN001, mTD001, false);
                mv.visitVarInsn(Opcodes.ISTORE, 1);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(Opcodes.ILOAD, 1);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "lain/mods/helper/asm/Hooks", "isInvisible", "(Lnet/minecraft/entity/player/EntityPlayerMP;Z)Z", false);
                mv.visitInsn(Opcodes.IRETURN);
                mv.visitMaxs(2, 2);
                mv.visitEnd();
            }
            if (!foundM002)
            {
                MethodVisitor mv = super.visitMethod(Opcodes.ACC_PUBLIC, mN002, mD002, null, null);
                mv.visitCode();
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, mTO002, mTN002, mTD002, false);
                mv.visitVarInsn(Opcodes.FSTORE, 1);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(Opcodes.FLOAD, 1);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "lain/mods/helper/asm/Hooks", "getArmorVisibility", "(Lnet/minecraft/entity/player/EntityPlayerMP;F)F", false);
                mv.visitInsn(Opcodes.FRETURN);
                mv.visitMaxs(2, 2);
                mv.visitEnd();
            }
            if (!foundM003)
            {
                MethodVisitor mv = super.visitMethod(Opcodes.ACC_PUBLIC, mN003, mD003, null, null);
                mv.visitCode();
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, mTO003, mTN003, mTD003, false);
                mv.visitVarInsn(Opcodes.ISTORE, 2);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitVarInsn(Opcodes.ILOAD, 2);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "lain/mods/helper/asm/Hooks", "isPotionApplicable", "(Lnet/minecraft/entity/player/EntityPlayerMP;Lnet/minecraft/potion/PotionEffect;Z)Z", false);
                mv.visitInsn(Opcodes.IRETURN);
                mv.visitMaxs(2, 3);
                mv.visitEnd();
            }
            super.visitEnd();
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
        {
            if (mN001.equals(name) && mD001.equals(desc))
            {
                foundM001 = true;
                return new method001(super.visitMethod(access, name, desc, signature, exceptions));
            }
            if (mN002.equals(name) && mD002.equals(desc))
            {
                foundM002 = true;
                return new method002(super.visitMethod(access, name, desc, signature, exceptions));
            }
            if (mN003.equals(name) && mD003.equals(desc))
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
