package com.didilink.sdkdemo.adid;

import java.util.HashMap;
import java.util.Map;

public class IdProviderFactory {

    public static final String PLATFORM_MS = "didilink";  //didilink
    public static String PLATFORM_DEFAULT = "";

    private static final Map<String, IAdIdProvider> providers;

    static {
        providers = new HashMap<>();
        providers.put(PLATFORM_MS, new MSIdProvider());
    }

    public static IAdIdProvider getProvider(String platform) {
        return providers.get(platform);
    }

    public static void setDefaultPlatform(String platform) {
        PLATFORM_DEFAULT = platform;
    }

    /**
     * 获取默认的广告提供者
     * @return
     */
    public static IAdIdProvider getDefaultProvider() {
        return providers.get(PLATFORM_DEFAULT);
    }
}
