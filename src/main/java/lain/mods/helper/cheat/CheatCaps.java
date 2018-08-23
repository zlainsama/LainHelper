package lain.mods.helper.cheat;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;
import com.google.common.collect.Lists;

public class CheatCaps implements INBTSerializable<NBTTagCompound>
{

    private final Entity owner;
    private final List<Cheat> cheats;

    private boolean initialized = false;

    @CapabilityInject(CheatCaps.class)
    public static final Capability<CheatCaps> CAPABILITY_CHEAT = null;

    public CheatCaps(Entity owner)
    {
        this.owner = owner;
        this.cheats = Lists.newArrayList();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        cheats.clear();

        for (Cheat cheat : Cheat.values())
            if (nbt.getBoolean(cheat.name()) || cheat.shouldObtain(owner))
                cheats.add(cheat);

        if (!initialized)
            initialized = true;
    }

    public void forEach(Predicate<Cheat> filter, Consumer<Cheat> action)
    {
        if (!initialized)
            deserializeNBT(new NBTTagCompound());

        for (Cheat cheat : cheats)
            if (filter.test(cheat))
                action.accept(cheat);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        for (Cheat cheat : cheats)
            nbt.setBoolean(cheat.name(), true);
        return nbt;
    }

}
