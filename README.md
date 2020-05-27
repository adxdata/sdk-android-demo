# 美数广告 SDK
### 接入流程
#### 1.嵌入广告SDK
将 sdk-android-demo/app/libs 中的 meishu-sdk_xxx_release.aar、open_ad_sdk_xxx.aar、Baidu_MobAds_SDK-release-xxx.aar、GDTSDK.unionNormal.xxx.aar、msa_mdid_1.0.13 拷贝到项目的 libs 下，对应的 build.gradle 文件里面添加如下配置

xxx代表版本号，建议使用 demo 中的版本

穿山甲、百度、广点通三个包可选，不接入可以不添加

```groovy
repositories{
    flatDir{
        dirs 'libs'
    }
}

dependencies {
    implementation(name: 'meishu-sdk_xxx_release', ext: 'aar') // 美数
    implementation(name: 'open_ad_sdk_xxx', ext: 'aar') // 穿山甲
    implementation(name: 'Baidu_MobAds_SDK-release-xxx', ext: 'aar') // 百度
    implementation(name: 'GDTSDK.unionNormal.xxx', ext: 'aar') // 广点通
    implementation(name: 'msa_mdid_xxx', ext: 'aar') // oaid
}
```

#### 2.添加SDK依赖库
请添加如下依赖库

```groovy
dependencies {
    implementation 'com.squareup.okhttp3:okhttp:3.12.1'
    implementation 'com.google.code.gson:gson:2.8.5'
    implementation 'com.googlecode.android-query:android-query:0.25.9'
    implementation 'com.github.razerdp:BasePopup:2.2.1'
    implementation 'pl.droidsonroids.gif:android-gif-drawable:1.2.6'
}
```

#### 3.添加 manifest 声明
要注意 provider 中的 android:authorities 不能有重复，xxx_file_path 文件可以从 demo 程序中获取

穿山甲、百度、广点通三个包可选，不接入可以不添加

百度 5.85 之前 provider 使用 com.baidu.mobads.openad.FileProvider，5.85(含) 之后使用 com.baidu.mobads.openad.BDFileProvider

```xml
<!-- 美数 begin -->
<provider
    android:name="com.meishu.sdk.core.service.MeishuFileProvider"
    android:authorities="${applicationId}.MeishuFileProvider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/meishu_file_path" />
</provider>
<!-- 美数 end -->
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

#### 4.添加权限
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
```

#### 5.oaid
sdk 内置了 oaid 获取的功能，必须添加 oaid 包，版本最好大于等于 1.0.13，不然可能会出现崩溃情况

需要在 assets 文件夹中添加文件 supplierconfig.json，这样才能正确获取到 oaid，开发者可以调用 AdSdk.getOaid() 来直接获取 oaid，如果您获取不到正确的 oaid，请检查接入方式

文件内容如下：
```json
{
  "supplier":{
    "vivo":{
      "appid":""
    },
    "xiaomi":{
    },
    "huawei":{
    },
    "oppo":{
    }
  }
}
```

更多 oaid 相关请访问移动安全联盟 MSA 官网 https://msa-alliance.cn/col.jsp?id=120

#### 6.代码混淆
如果您需要使用 proguard 混淆代码，需确保不要混淆 SDK 的代码。请把 demo 下的 sdk-android-demo/app/proguard-rules.pro 文件的内容追加到您项目的混淆配置文件中，文件中包含了美数、穿山甲、百度、广点通的混淆

#### 7.代码接入
请在您的 Application 初始化 sdk，代码如下，更多选项及设置详见 demo 代码
```java
// 一定要在 Application 中初始化 sdk，否则无法正常使用sdk
// 参数1: 使用 Application Context
// 参数2: appId 是在美数注册的 appId
// 参数3: isTest 表示是否开启测试模式，测试模式无法用于生产环境
// 参数4: getOaid 表示是否获取 oaid，填写 true 获取，0.6.38 之前的版本不填不自动获取
AdSdk.init(context, "101629", true, true);
// 设置下载提示类型，默认不提示 AdSdk.DOWNLOAD_MODE_DIRECTLY
AdSdk.setDownloadMode(AdSdk.DOWNLOAD_MODE_NOTIFY);
// 设置是否 debug 模式，debug 模式会打印内部 log，默认不打印
AdSdk.setDebug(true);
// 设置用户标签，什么时候获取到，什么时候设置就可以了
AdSdk.setAge(18);
AdSdk.setGender(AdSdk.GENDER_MALE);
AdSdk.setKeywords("food,game");
```
#### 8.广告位代码接入
请参考 demo 代码

* ~~目前贴片视频广告需要在 Activity 中的 onResume 和 onPause 调用代码才能实现暂停和恢复，详见 PasterActivity~~
* 穿山甲 Draw视频流广告接入方法见 VideoFeedActivity
