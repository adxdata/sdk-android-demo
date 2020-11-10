## 目录
* [**依赖配置**](#依赖配置)
* [**manifest配置**](#manifest配置)
* [**SDK初始化配置**](#SDK初始化配置)
* [**广告形式**](#广告形式)
    * [开屏](#开屏)
    * [Banner](#Banner)
    * [插屏](#插屏)
    * [信息流](#信息流)
    * [视频贴片](#视频贴片)
    * [激励视频](#激励视频)
* [**错误排查**](#错误排查)

## 依赖配置

将 sdk-android-demo/app/libs 中的 meishu-sdk_xxx_release.aar、open_ad_sdk_xxx.aar、Baidu_MobAds_SDK-release-xxx.aar、GDTSDK.unionNormal.xxx.aar、msa_mdid_1.0.13 拷贝到项目的 libs 下，对应的 build.gradle 文件里面添加如下配置

sdk 依赖一些第三方库，具体如下代码所示

xxx代表版本号，建议使用 demo 中的版本

穿山甲、百度、广点通三个包可选，不接入可以不添加

```groovy
repositories{
    flatDir{
        dirs 'libs'
    }
}

dependencies {
    implementation(name: 'meishu-sdk_xxx_release', ext: 'aar') // msad
    implementation(name: 'open_ad_sdk_xxx', ext: 'aar') // 穿山甲
    implementation(name: 'Baidu_MobAds_SDK-release-xxx', ext: 'aar') // 百度
    implementation(name: 'GDTSDK.unionNormal.xxx', ext: 'aar') // 广点通
    implementation(name: 'msa_mdid_xxx', ext: 'aar') // oaid

    implementation 'com.squareup.okhttp3:okhttp:3.12.1'
    implementation 'com.google.code.gson:gson:2.8.5'
    implementation 'com.googlecode.android-query:android-query:0.25.9'
    implementation 'com.github.razerdp:BasePopup:2.2.1'
    implementation 'pl.droidsonroids.gif:android-gif-drawable:1.2.6'
    implementation 'com.danikula:videocache:2.7.1'
}
```

## manifest配置

在application中添加如下代码

```xml
android:requestLegacyExternalStorage="true"
```

添加provider，要注意 provider 中的 android:authorities 不能有重复，xxx_file_path 文件可以从 demo 程序中获取

穿山甲、百度、广点通三个包可选，不接入可以不添加

百度 5.85 之前 provider 使用 com.baidu.mobads.openad.FileProvider，5.85(含) 之后使用 com.baidu.mobads.openad.BDFileProvider，请注意替换，否则无法下载apk

权限也需要添加，代码如下

```xml
<!-- msad begin -->
<provider
    android:name="com.meishu.sdk.core.service.MeishuFileProvider"
    android:authorities="${applicationId}.MeishuFileProvider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/meishu_file_path" />
</provider>
<!-- msad end -->
<!-- 穿山甲 begin -->
<provider
    android:name="com.bytedance.sdk.openadsdk.TTFileProvider"
    android:authorities="${applicationId}.TTFileProvider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/csj_file_path" />
</provider>
<provider
    android:name="com.bytedance.sdk.openadsdk.multipro.TTMultiProvider"
    android:authorities="${applicationId}.TTMultiProvider"
    android:exported="false" />
<!-- 穿山甲 end -->
<!-- 百度 begin -->
<activity
    android:name="com.baidu.mobads.AppActivity"
    android:configChanges="keyboard|keyboardHidden|orientation"
    android:theme="@android:style/Theme.Translucent.NoTitleBar"/>
<provider
    android:name="com.baidu.mobads.openad.FileProvider"
    android:authorities="${applicationId}.bd.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/bd_file_path" />
</provider>
<!-- 百度 end -->
<!-- 广点通 begin -->
<service
    android:name="com.qq.e.comm.DownloadService"
    android:exported="false" />
<activity
    android:name="com.qq.e.ads.ADActivity"
    android:configChanges="keyboard|keyboardHidden|orientation|screenSize" />
<activity
    android:name="com.qq.e.ads.PortraitADActivity"
    android:configChanges="keyboard|keyboardHidden|orientation|screenSize"
    android:screenOrientation="portrait" />
<activity
    android:name="com.qq.e.ads.LandscapeADActivity"
    android:configChanges="keyboard|keyboardHidden|orientation|screenSize"
    android:screenOrientation="landscape" />
<provider
    android:name="android.support.v4.content.FileProvider"
    android:authorities="${applicationId}.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/gdt_file_path" />
</provider>
<!-- 广点通 end -->
```
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
```

## SDK初始化配置

```java
// 一定要在 Application 中初始化 sdk，第一个参数需要填入 Application，否则无法正常使用 sdk
// appId 是在msad注册的 appId
// isTest 表示是否开启测试模式，测试模式无法用于生产环境
// getOaid 表示是否获取 oaid，建议填 true
AdSdk.init(this, "101629", true, true);

// 设置下载提示类型，默认不提示 AdSdk.DOWNLOAD_MODE_DIRECTLY
AdSdk.setDownloadMode(AdSdk.DOWNLOAD_MODE_NOTIFY);

// 设置是否 debug 模式，debug 模式会打印内部 log，默认不打印，需要排查错误一定要设置 true
AdSdk.setDebug(true);

// 设置用户标签，什么时候获取到，什么时候设置就可以了
// AdSdk.setAge(18);
// AdSdk.setGender(AdSdk.GENDER_MALE);
// AdSdk.setKeywords("food,game");

// 设置广点通自渲染版本，默认 2.0。这是为老版本做的兼容，一般不需要设置
// AdSdk.setGdtRecyclerVersion(AdSdk.GDT_1);
```

## 广告形式
### 开屏
支持半屏开屏，用户可以添加自己的 logo，注意需要使用 RelativeLayout 来作为 adContainer，目前 loadAd 后会自动展示

接入代码示例如下
``` java
SplashAdListener listener = new SplashAdListener() {

    @Override
    public void onAdLoaded(ISplashAd splashAd) {
        // 广告加载成功
        splashAd.setInteractionListener(new InteractionListener() {
            @Override
            public void onAdClicked() {
                // 设置点击监听
            }
        });
    }

    @Override
    public void onAdError() {
        // 广告加载错误
    }

    @Override
    public void onAdExposure() {
        // 广告曝光
    }

    @Override
    public void onAdClosed() {
        //  广告关闭
    }

    @Override
    public void onAdPresent(ISplashAd splashAd) {
        // 广告展示，广告被展示时回调（非曝光回调），主要为了解决广点通曝光太晚的问题
    }

    @Override
    public void onAdSkip(ISplashAd splashAd) {
        // 点击跳过按钮回调
    }

    @Override
    public void onAdTimeOver(ISplashAd splashAd) {
        // 倒计时结束回调
    }
};
SplashAdLoader splashAdLoader = new SplashAdLoader(this, adContainer, pid, listener, 3000);
splashAdLoader.loadAd();
```

### Banner

建议使用 FrameLayout 作为 bannerContainer

需要 destroy

```java
BannerAdListener listener = new BannerAdListener() {

    @Override
    public void onAdLoaded(IBannerAd iBannerAd) {
        // 不显示关闭按钮，仅限msad
        bannerAd.setCloseButtonVisible(showCloseButton);
        // 适应 container 的大小需要设置宽高，仅限msad
        bannerAd.setWidthAndHeight(bannerContainer.getMeasuredWidth(), bannerContainer.getMeasuredHeight());
        // 将渲染好的广告 view 放入布局的 bannerContainer 中
        bannerContainer.addView(bannerAd.getAdView());
        bannerAd.setInteractionListener(new InteractionListener() {
            @Override
            public void onAdClicked() {
                // 点击监听
            }
        });
    }

    @Override
    public void onAdError() {

    }

    @Override
    public void onAdExposure() {

    }

    @Override
    public void onAdClosed() {

    }
};

BannerAdLoader bannerLoader = new BannerAdLoader(this, pid, listener);
bannerLoader.loadAd();

if (bannerLoader != null) {
    bannerLoader.destroy();
}
```

### 插屏

插屏广告支持加载与展示分离，可通过 interstitialAd.showAd(activity); 在其他 activity 中进行展示

需要 destroy

```java
InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {

    @Override
    public void onAdLoaded(InterstitialAd interstitialAd) {
        interstitialAd.setInteractionListener(new InteractionListener() {
            @Override
            public void onAdClicked() {
            }
        });
        // 展示广告
        interstitialAd.showAd();
    }

    @Override
    public void onAdExposure() {

    }

    @Override
    public void onAdClosed() {

    }

    @Override
    public void onAdError() {

    }
};

InterstitialAdLoader interstitialAdLoader = new InterstitialAdLoader(this, pid, interstitialAdListener);
interstitialAdLoader.loadAd();

if (interstitialAdLoader != null) {
    interstitialAdLoader.destroy();
}
```

### 信息流



```java
```

### 视频贴片

需要 destroy

```java
PasterAdListener listener = new PasterAdListener() {

    @Override
    public void onAdLoaded(PasterAd pasterAd) {
        pasterAd.setInteractionListener(new InteractionListener() {
            @Override
            public void onAdClicked() {
            }
        });
    }

    @Override
    public void onAdError() {

    }

    @Override
    public void onAdExposure() {

    }

    @Override
    public void onAdClosed() {

    }

    @Override
    public void onVideoLoaded() {
        // 视频 onPrepared 时回调
    }

    @Override
    public void onVideoComplete() {
        // 视频播放完成时回调
    }
};

PasterAdLoader pasterAdLoader = new PasterAdLoader(this, videoContainer, pid, listener);
pasterAdLoader.loadAd();

if (pasterAdLoader != null) {
    pasterAdLoader.destroy();
}
```

### 激励视频

支持加载与展示分离

需要 destroy

```java
RewardVideoAdListener listener = new RewardVideoAdListener() {

    @Override
    public void onAdLoaded(RewardVideoAd rewardVideoAd) {
        rewardVideoAd.setInteractionListener(new InteractionListener() {
            @Override
            public void onAdClicked() {
            }
        });
        rewardVideoAd.showAd();
    }

    @Override
    public void onAdError() {

    }

    @Override
    public void onAdExposure() {

    }

    @Override
    public void onAdClosed() {

    }

    @Override
    public void onVideoCached() {
        // 目前无用
    }

    @Override
    public void onReward() {
        // 获得奖励后回调
    }
};

RewardVideoLoader rewardVideoLoader = new RewardVideoLoader(this, pid, listener);
rewardVideoLoader.loadAd();

if (rewardVideoLoader != null) {
    rewardVideoLoader.destroy();
}
```

## 错误排查

初始化时设置 AdSdk.setDebug(true); 日志中搜索 MeishuSdk_ 即可看到 sdk 内部打印的 log，目前包含上报信息，错误信息等