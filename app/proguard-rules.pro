# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepattributes Signature
-keepattributes *Annotation*

# 美数
-keep class com.meishu.sdk.** { *; }

# OAID
-keep class com.bun.miitmdid.** { *; }
-keep class com.bun.lib.** { *; }
-keep class com.asus.msa.** { *; }
-keep class com.huawei.hms.ads.identifier.** { *; }
-keep class com.netease.nis.sdkwrapper.** { *; }
-keep class com.samsung.android.deviceidservice.** { *; }
-keep class a.** { *; }
-keep class XI.** { *; }

# 百度
-keepclassmembers class * extends android.app.Activity { public void *(android.view.View); }
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class com.baidu.mobads.** { *; }
-keep class com.baidu.mobad.** { *; }
-keep class com.bun.miitmdid.core.** { *; }

# 穿山甲
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** { *; }
-keep class com.pgl.sys.ces.* { *; }

# 广点通
-keep class android.support.v4.** { public *; }
-keep class android.support.v7.** { public *; }
-keep class * extends java.lang.annotation.Annotation { *; }
-keep interface * extends java.lang.annotation.Annotation { *; }
-keepclasseswithmembers,includedescriptorclasses class * { native <methods>; }
-keep class com.qq.e.** { public protected *; }
-keep class yaq.gdtadv { *; }
-keep class com.qq.e.** { *; }
-keep class com.tencent.** { *; }
-keep class cn.mmachina.JniClient { *; }
-keep class c.t.m.li.tsa.** { *; }
-keep, allowobfuscation class com.qq.e.comm.plugin.services.SDKServerService { *; }
-keepclassmembers, allowobfuscation class com.qq.e.comm.plugin.net.SecurePackager { public *; }
-keep class * extends com.qq.e.mediation.interfaces.BaseNativeUnifiedAd { *; }
-keep class * extends com.qq.e.mediation.interfaces.BaseSplashAd { *; }
-keep class * extends com.qq.e.mediation.interfaces.BaseRewardAd { *; }

# 快手
-keep class com.kwad.sdk.** { *; }
-keep class com.ksad.download.** { *; }
-keep class com.kwai.filedownloader.** { *; }
-keep class org.chromium.** { *; }
-keep class aegon.chrome.** { *; }
-keep class com.kwai.**{ *; }
-dontwarn com.kwai.**
-dontwarn com.kwad.**
-dontwarn com.ksad.**
-dontwarn aegon.chrome.**

# 小米
-keep class com.miui.zeus.mimo.sdk.** { *; }
-keep class com.miui.analytics.** { *; }
-keep class com.xiaomi.analytics.* { public protected *; }
-keep class * extends android.os.IInterface{ *; }

-keep class com.google.gson.examples.android.model.** { <fields>; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# OPPO
-keep class com.androidquery.callback.** { *; }
-keep class com.opos.** { *; }
-keep class com.heytap.msp.mobad.api.** { *; }
-keep class com.heytap.openid.** { *; }
-keep class android.support.v4.** { public *; }
-keep class android.support.v7.** { public *; }

# inmobi
-keepattributes SourceFile,LineNumberTable
-keep class com.inmobi.** { *; }
-dontwarn com.inmobi.**
-dontwarn com.squareup.picasso.**
#skip the Picasso library classes
-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.**
#skip AVID classes
-keep class com.integralads.avid.library.** {*;}
#skip IAB classes
-keep class com.iab.** {*;}
-dontwarn com.iab.**
