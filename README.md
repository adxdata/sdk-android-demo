# 美数广告 SDK
### 接入流程
#### 1.嵌入广告SDK
将 Meishu_Android_SDK_Demo/libs 中的 Baidu_MobAds_SDK-release.aar、meishu-sdk.aar、open_ad_sdk.aar 拷贝到项目的 libs 下，对应的 build.gradle 文件里面添加如下配置

        repositories{
            flatDir{
                dirs 'libs'
            }
        }

        dependencies {
            implementation(name:'Baidu_MobAds_SDK-release',ext:'aar')
            implementation(name:'meishu-sdk',ext:'aar')
            implementation(name:'open_ad_sdk',ext:'aar')
        }
        
#### 2.添加SDK依赖库
请添加如下依赖库

        dependencies {
            implementation 'com.squareup.okhttp3:okhttp:3.12.0'
            implementation 'com.google.code.gson:gson:2.8.5'
            implementation 'com.googlecode.android-query:android-query:0.25.9'
            implementation 'com.github.razerdp:BasePopup:2.2.1'
            implementation 'pl.droidsonroids.gif:android-gif-drawable:1.2.6'
        }

#### 3.代码混淆
如果您需要使用 proguard 混淆代码，需确保不要混淆 SDK 的代码。请把 demo 下的 app/proguard-rules.pro 文件的内容追加到您项目的混淆配置文件中

#### 4.代码接入
请在您的 Application 初始化 sdk，代码如下

    //一定要在Application中初始化sdk，否则无法正常使用sdk
    //appId是在美数注册的appId
    //isTest 表示是否开启测试模式，测试模式无法用于生产环境
    AdSdk.init("101343", true);

#### 5.广告位代码接入
请参考 demo 代码

