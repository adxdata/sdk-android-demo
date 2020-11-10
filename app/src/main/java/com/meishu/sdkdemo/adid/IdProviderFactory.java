package com.meishu.sdkdemo.adid;

import java.util.HashMap;
import java.util.Map;

public class IdProviderFactory {

    public static final String PLATFORM_MS = "meishu";  //msad
    public static final String PLATFORM_CSJ = "csj";    //穿山甲
    public static final String PLATFORM_GDT = "gdt";    //广点通
    public static final String PLATFORM_BD = "bd";      //百度
    public static final String PLATFORM_KS = "ks";      //快手
    public static final String PLATFORM_OPPO = "oppo";  //oppo
    public static final String PLATFORM_MIMO = "mimo";  //小米
    public static String PLATFORM_DEFAULT = "";

    private static final Map<String, IAdIdProvider> providers;

    static {
        providers = new HashMap<>();
        providers.put(PLATFORM_MS, new MSIdProvider());
        providers.put(PLATFORM_CSJ, new CSJIdProvider());
        providers.put(PLATFORM_GDT, new GDTIdProvider());
        providers.put(PLATFORM_BD, new BDIdProvider());
        providers.put(PLATFORM_KS, new KSIdProvider());
        providers.put(PLATFORM_OPPO, new OPPOIdProvider());
        providers.put(PLATFORM_MIMO, new MimoIdProvider());
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
