package com.meishu.sdkdemo;

import android.app.Application;

import com.meishu.sdk.core.AdSdk;
import com.meishu.sdkdemo.adid.IdProviderFactory;

public class SdkDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //一定要在Application中初始化sdk，否则无法正常使用sdk
        //appId是在美数注册的appId
        //testModeEnabled表示是否开启测试模式，测试模式无法用于生产环境
        // 101343
        AdSdk.init("101629", true);
    }
}
