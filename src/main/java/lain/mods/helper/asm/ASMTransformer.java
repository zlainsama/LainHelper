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
            public void visitCode()
            {
                super.visitCode();
                this.visitVarInsn(Opcodes.ALOAD, 0);
                this.visitMethodInsn(Opcodes.INVOKESTATIC, "lain/mods/helper/asm/Hooks", "onLivingUpdate", "(Lnet/minecraft/entity/player/EntityPlayer;)V", false);
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
                if (opcode == Opcodes.IRETURN)
                {
                    this.visitVarInsn(Opcodes.ISTORE, 1);
                    this.visitVarInsn(Opcodes.ALOAD, 0);
                    this.visitVarInsn(Opcodes.ILOAD, 1);
                    this.visitMethodInsn(Opcodes.INVOKESTATIC, "lain/mods/helper/asm/Hooks", "isInvisible", "(Lnet/minecraft/entity/player/EntityPlayer;Z)Z", false);
                }
                super.visitInsn(opcode);
            };

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
                if (opcode == Opcodes.FRETURN)
                {
                    this.visitVarInsn(Opcodes.FSTORE, 1);
                    this.visitVarInsn(Opcodes.ALOAD, 0);
                    this.visitVarInsn(Opcodes.FLOAD, 1);
                    this.visitMethodInsn(Opcodes.INVOKESTATIC, "lain/mods/helper/asm/Hooks", "getArmorVisibility", "(Lnet/minecraft/entity/player/EntityPlayer;F)F", false);
                }
                super.visitInsn(opcode);
            };

        }

        class method004 extends MethodVisitor
        {

            public method004(MethodVisitor mv)
            {
                super(Opcodes.ASM5, mv);
            }

            @Override
            public void visitCode()
            {
                super.visitCode();
                this.visitVarInsn(Opcodes.ALOAD, 0);
                this.visitVarInsn(Opcodes.ALOAD, 1);
                this.visitVarInsn(Opcodes.FLOAD, 2);
                this.visitMethodInsn(Opcodes.INVOKESTATIC, "lain/mods/helper/asm/Hooks", "applyDamageReduction", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/DamageSource;F)F", false);
                this.visitVarInsn(Opcodes.FSTORE, 2);
            }

        }

        class method005 extends MethodVisitor
        {

            public method005(MethodVisitor mv)
            {
                super(Opcodes.ASM5, mv);
            }

            @Override
            public void visitInsn(int opcode)
            {
                if (opcode == Opcodes.IRETURN)
                {
                    this.visitVarInsn(Opcodes.ISTORE, 3);
                    this.visitVarInsn(Opcodes.ALOAD, 0);
                    this.visitVarInsn(Opcodes.ALOAD, 1);
                    this.visitVarInsn(Opcodes.ILOAD, 3);
                    this.visitMethodInsn(Opcodes.INVOKESTATIC, "lain/mods/helper/asm/Hooks", "isPotionApplicable", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/potion/PotionEffect;Z)Z", false);
                }
                super.visitInsn(opcode);
            };

        }

        ObfHelper parent = ObfHelper.newClass("net/minecraft/entity/EntityLivingBase");

        ObfHelper m001 = ObfHelper.newMethod("func_70636_d", "net/minecraft/entity/player/EntityPlayer", "()V").setDevName("onLivingUpdate");
        boolean foundM001 = false;
        ObfHelper m002 = ObfHelper.newMethod("func_82150_aj", "net/minecraft/entity/player/EntityPlayer", "()Z").setDevName("isInvisible");
        boolean foundM002 = false;
        ObfHelper m003 = ObfHelper.newMethod("func_82243_bO", "net/minecraft/entity/player/EntityPlayer", "()F").setDevName("getArmorVisibility");
        boolean foundM003 = false;
        ObfHelper m004 = ObfHelper.newMethod("func_70665_d", "net/minecraft/entity/player/EntityPlayer", "(Lnet/minecraft/util/DamageSource;F)V").setDevName("damageEntity");
        boolean foundM004 = false;
        ObfHelper m005 = ObfHelper.newMethod("func_70687_e", "net/minecraft/entity/player/EntityPlayer", "(Lnet/minecraft/potion/PotionEffect;)Z").setDevName("isPotionApplicable");
        boolean foundM005 = false;

        public transformer001(ClassVisitor cv)
        {
            super(Opcodes.ASM5, cv);
        }

        @Override
        public void visitEnd()
        {
            if (!foundM001)
            {
                MethodVisitor mv = this.visitMethod(Opcodes.ACC_PUBLIC, m001.getData(1), m001.getData(2), null, null);
                mv.visitCode();
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, parent.getData(0), m001.getData(1), m001.getData(2), false);
                mv.visitInsn(Opcodes.RETURN);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
            if (!foundM002)
            {
                MethodVisitor mv = this.visitMethod(Opcodes.ACC_PUBLIC, m002.getData(1), m002.getData(2), null, null);
                mv.visitCode();
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, parent.getData(0), m002.getData(1), m002.getData(2), false);
                mv.visitInsn(Opcodes.IRETURN);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
            if (!foundM003)
            {
                MethodVisitor mv = this.visitMethod(Opcodes.ACC_PUBLIC, m003.getData(1), m003.getData(2), null, null);
                mv.visitCode();
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, parent.getData(0), m003.getData(1), m003.getData(2), false);
                mv.visitInsn(Opcodes.FRETURN);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
            if (!foundM004)
            {
                MethodVisitor mv = this.visitMethod(Opcodes.ACC_PROTECTED, m004.getData(1), m004.getData(2), null, null);
                mv.visitCode();
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitVarInsn(Opcodes.FLOAD, 2);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, parent.getData(0), m004.getData(1), m004.getData(2), false);
                mv.visitInsn(Opcodes.RETURN);
                mv.visitMaxs(3, 3);
                mv.visitEnd();
            }
            if (!foundM005)
            {
                MethodVisitor mv = this.visitMethod(Opcodes.ACC_PUBLIC, m005.getData(1), m005.getData(2), null, null);
                mv.visitCode();
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, parent.getData(0), m005.getData(1), m005.getData(2), false);
                mv.visitInsn(Opcodes.IRETURN);
                mv.visitMaxs(2, 2);
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
            if (m004.match(name, desc))
            {
                foundM004 = true;
                return new method004(super.visitMethod(access, name, desc, signature, exceptions));
            }
            if (m005.match(name, desc))
            {
                foundM005 = true;
                return new method005(super.visitMethod(access, name, desc, signature, exceptions));
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        if ("net.minecraft.entity.player.EntityPlayer".equals(transformedName))
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
