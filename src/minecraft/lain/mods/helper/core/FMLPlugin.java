package lain.mods.helper.core;

import java.util.Map;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name("LainHelper:CorePlugin")
@IFMLLoadingPlugin.MCVersion("1.6.4")
@IFMLLoadingPlugin.TransformerExclusions({ "lain.mods.helper.core.", "lain.mods.helper.customport." })
public class FMLPlugin implements IFMLLoadingPlugin
{

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[] { "lain.mods.helper.customport.CustomPortClassTransformer" };
    }

    @Override
    public String[] getLibraryRequestClass()
    {
        return null;
    }

    @Override
    public String getModContainerClass()
    {
        return "lain.mods.helper.LainHelper";
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
    }

}
