package lain.mods.helper.asm;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import com.google.common.collect.ImmutableList;

public class ObfHelper
{

    public static ObfHelper newClass(String className)
    {
        Validate.notNull(className);
        ObfHelper result = new ObfHelper();
        result.type = 2;
        result.data[0] = className;
        result.transform(!Plugin.runtimeDeobfuscationEnabled);
        return result;
    }

    public static ObfHelper newField(String fieldName, String ownerName)
    {
        Validate.notNull(fieldName);
        Validate.notNull(ownerName);
        ObfHelper result = new ObfHelper();
        result.type = 3;
        result.data[0] = ownerName;
        result.data[1] = fieldName;
        result.transform(!Plugin.runtimeDeobfuscationEnabled);
        return result;
    }

    public static ObfHelper newMethod(String methodName, String ownerName, String descriptor)
    {
        Validate.notNull(methodName);
        Validate.notNull(ownerName);
        Validate.notNull(descriptor);
        ObfHelper result = new ObfHelper();
        result.type = 4;
        result.data[0] = ownerName;
        result.data[1] = methodName;
        result.data[2] = descriptor;
        result.transform(!Plugin.runtimeDeobfuscationEnabled);
        return result;
    }

    public static ObfHelper newPackage(String packageName)
    {
        Validate.notNull(packageName);
        ObfHelper result = new ObfHelper();
        result.type = 1;
        result.data[0] = packageName;
        result.transform(!Plugin.runtimeDeobfuscationEnabled);
        return result;
    }

    private int type;
    private String[] data = new String[3];

    private ObfHelper()
    {
    }

    public String getData(int index)
    {
        if (index >= 0 && index <= 2)
            return data[index];
        return null;
    }

    public boolean match(Object... obj)
    {
        switch (type)
        {
            case 1:
                if (obj.length == 1)
                    return data[0].equals(obj[0]);
                break;
            case 2:
                if (obj.length == 1)
                    return data[0].equals(obj[0]);
                break;
            case 3:
                if (obj.length == 1)
                    return data[1].equals(obj[0]);
                else if (obj.length == 2)
                    return data[0].equals(obj[0]) && data[1].equals(obj[1]);
                break;
            case 4:
                if (obj.length == 2)
                    return data[1].equals(obj[0]) && data[2].equals(obj[1]);
                else if (obj.length == 3)
                    return data[0].equals(obj[0]) && data[1].equals(obj[1]) && data[2].equals(obj[2]);
                break;
        }
        return false;
    }

    private void transform(boolean dev)
    {
        switch (type)
        {
            case 1:
                Map<String, String> map1 = dev ? Setup.PackageMap : Setup.PackageMap.inverse();
                if (map1.containsKey(data[0]))
                    data[0] = map1.get(data[0]);
                break;
            case 2:
                Map<String, String> map2 = dev ? Setup.ClassMap : Setup.ClassMap.inverse();
                if (map2.containsKey(data[0]))
                    data[0] = map2.get(data[0]);
                break;
            case 3:
                Map<List<String>, List<String>> map3 = dev ? Setup.FieldMap : Setup.FieldMap.inverse();
                List<String> list3 = ImmutableList.of(data[0], data[1]);
                if (map3.containsKey(list3))
                {
                    list3 = map3.get(list3);
                    data[0] = list3.get(0);
                    data[1] = list3.get(1);
                }
                break;
            case 4:
                Map<List<String>, List<String>> map4 = dev ? Setup.MethodMap : Setup.MethodMap.inverse();
                List<String> list4 = ImmutableList.of(data[0], data[1], data[2]);
                if (map4.containsKey(list4))
                {
                    list4 = map4.get(list4);
                    data[0] = list4.get(0);
                    data[1] = list4.get(1);
                    data[2] = list4.get(2);
                }
                break;
        }
    }

}
