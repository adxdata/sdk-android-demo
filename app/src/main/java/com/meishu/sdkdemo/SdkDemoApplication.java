package com.meishu.sdkdemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.meishu.sdk.core.AdSdk;
import com.meishu.sdk.core.MSAdConfig;

public class SdkDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MSAdConfig sdkConfig = new MSAdConfig.Builder()
                .appId("101629")
                .isTest(true)       //测试环境
                .enableDebug(true)  //开启DEBUG模式，打印内部LOG
                .downloadConfirm(MSAdConfig.DOWNLOAD_CONFIRM_AUTO)
                .build();

        AdSdk.init(this, sdkConfig);


        // 设置是否 debug 模式，debug 模式会打印内部 log，默认不打印
        //AdSdk.setDebug(true);

        //一定要在Application中初始化sdk，否则无法正常使用sdk
        //appId是在美数注册的appId
        //testModeEnabled表示是否开启测试模式，测试模式无法用于生产环境
        //AdSdk.init(this, "101629", true, true);

        // 设置下载提示类型，默认不提示 AdSdk.DOWNLOAD_MODE_DIRECTLY
        //AdSdk.setDownloadMode(AdSdk.DOWNLOAD_MODE_WIFI_DIRECTLY);

        // 设置请求广告的 secure 参数，一般不需要设置，默认 SECURE_ALL
//        AdSdk.setSecure(AdSdk.SECURE_HTTP);

        // 设置用户标签，什么时候获取到，什么时候设置就可以了
//        AdSdk.setAge(18);
//        AdSdk.setGender(AdSdk.GENDER_MALE);
//        AdSdk.setKeywords("food,game");

        // 设置广点通自渲染版本，默认 2.0
//        AdSdk.setGdtRecyclerVersion(AdSdk.GDT_1);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
