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

将 sdk-android-demo/app/libs 中的 didilink-sdk_xxx_release.aar、oaid_sdk_1.0.23 拷贝到项目的 libs 下，对应的 build.gradle 文件里面添加如下配置

sdk 依赖一些第三方库，具体如下代码所示

xxx代表版本号，建议使用 demo 中的版本


```groovy
repositories{
    flatDir{
        dirs 'libs'
    }
}

dependencies {
    implementation(name: 'didilink-sdk_xxx_release', ext: 'aar') // didilink
    implementation(name: 'oaid_sdk_1.0.23', ext: 'aar') // oaid

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


权限也需要添加，代码如下

```xml
<!-- didilink begin -->
<provider
    android:name="com.didilink.sdk.core.service.DiDiLinkFileProvider"
    android:authorities="${applicationId}.DiDiLinkFileProvider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/DiDiLink_file_path" />
</provider>
<!-- didilink end -->
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
// appId 是在didilink注册的 appId
// isTest 表示是否开启测试模式，测试模式无法用于生产环境
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
        // 不显示关闭按钮，仅限didilink
        bannerAd.setCloseButtonVisible(showCloseButton);
        // 适应 container 的大小需要设置宽高，仅限didilink
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

初始化时设置 AdSdk.setDebug(true); 日志中搜索 DiDiLinkSdk_ 即可看到 sdk 内部打印的 log，目前包含上报信息，错误信息等