package com.didilink.sdkdemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.didilink.sdk.core.AdSdk;
import com.didilink.sdk.core.MSAdConfig;

public class SdkDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MSAdConfig sdkConfig = new MSAdConfig.Builder()
                .appId("101629")
                .isTest(true)       //测试环境
                .enableDebug(true)  //开启DEBUG模式，打印内部LOG
                .downloadConfirm(MSAdConfig.DOWNLOAD_CONFIRM_AUTO)  //下载提示模式
//                .userId("123456")                   //设置用户ID
//                .userType(1)                        //设置用户类型
//                .userGender(MSAdConfig.GENDER_MALE) //设置用户性别
//                .userAge(18)                        //设置用户年龄
//                .userKeywords("汽车,漫画")          //设置用户关键词
                .build();

        AdSdk.init(this, sdkConfig);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
