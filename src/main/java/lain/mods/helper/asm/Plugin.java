package lain.mods.helper.asm;

import java.util.Map;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name("LainHelper")
@IFMLLoadingPlugin.MCVersion("")
@IFMLLoadingPlugin.TransformerExclusions("lain.mods.helper.asm.")
public class Plugin implements IFMLLoadingPlugin
{

    public static boolean runtimeDeobfuscationEnabled = false;

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[] { "lain.mods.helper.asm.ASMTransformer" };
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return "lain.mods.helper.asm.Setup";
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
        runtimeDeobfuscationEnabled = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

}
