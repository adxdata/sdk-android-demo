package com.meishu.sdkdemo;

import android.app.Application;

import com.meishu.sdk.core.AdSdk;

public class SdkDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 设置请求广告是否使用 https，默认不使用，需要在 init 之前调用
//        AdSdk.setHttps(true);

        //一定要在Application中初始化sdk，否则无法正常使用sdk
        //appId是在美数注册的appId
        //testModeEnabled表示是否开启测试模式，测试模式无法用于生产环境
        AdSdk.init(this, "101629", true);

        // 设置下载提示类型，默认不提示 AdSdk.DOWNLOAD_MODE_DIRECTLY
        AdSdk.setDownloadMode(AdSdk.DOWNLOAD_MODE_NOTIFY);

        // 设置是否 debug 模式，debug 模式会打印内部 log，默认不打印
        AdSdk.setDebug(true);

        // 设置请求广告的 secure 参数，一般不需要设置，默认 SECURE_ALL
//        AdSdk.setSecure(AdSdk.SECURE_HTTP);
    }
}
