package com.wdiscute.echoes.compat;

import net.irisshaders.iris.api.v0.IrisApi;

public class IrisCompat
{
    public static boolean isShaderPackInUse()
    {
        return IrisApi.getInstance().isShaderPackInUse();
    }
}
