package com.meishu.sdkdemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.meishu.sdk.core.AdSdk;
import com.meishu.sdk.meishu_ad.interstitial.Popup;
import com.meishu.sdkdemo.adid.IdProviderFactory;
import com.meishu.sdkdemo.nativead.NativeAdSelectActivity;
import com.meishu.sdkdemo.nativead.NativeRecyclerListSelectActivity;
import com.meishu.sdkdemo.nativead.NativeVideoActivity;
import com.meishu.sdkdemo.splash.SplashActivity;

import java.util.ArrayList;
import java.util.List;

//import razerdp.util.SimpleAnimationUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        Button bannerAD = findViewById(R.id.bannerAD);
        bannerAD.setOnClickListener(this);
        findViewById(R.id.slashAD).setOnClickListener(this);
        findViewById(R.id.interstitialAD).setOnClickListener(this);
        findViewById(R.id.nativeAD).setOnClickListener(this);
        findViewById(R.id.nativeMediaAD).setOnClickListener(this);
        findViewById(R.id.nativeRecyclerAD).setOnClickListener(this);
        findViewById(R.id.rewardVideoAd).setOnClickListener(this);
        findViewById(R.id.videoFeedAd).setOnClickListener(this);
        // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        }

        findViewById(R.id.open_popupwindow).setOnClickListener(this);

        initAdProvider();

        initVersionName();
    }

    private void initVersionName() {
        try {
            PackageManager pm = getPackageManager();
            String packageName = getPackageName();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            TextView txtVersion = findViewById(R.id.txt_version);
            txtVersion.setText(String.format("Demo 版本：%s\n美数 SDK 版本：%s\n穿山甲 SDK 版本：%s\n百度 SDK 版本：%s\n广点通 SDK 版本：%s",
                    packageInfo.versionName, AdSdk.getVersionName(), AdSdk.getCSJVersionName(), AdSdk.getBDVersionName(), AdSdk.getGDTVersionName()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initAdProvider() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        // 修改默认的广告提供者
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_ad_provider);
        RadioGroup rgd = findViewById(R.id.rdg_ad_provider);
        rgd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_ad_provider_ms:
                        IdProviderFactory.setDefaultPlatform(IdProviderFactory.PLATFORM_MS);
                        break;
                    case R.id.rb_ad_provider_bd:
                        IdProviderFactory.setDefaultPlatform(IdProviderFactory.PLATFORM_BD);
                        break;
                    case R.id.rb_ad_provider_csj:
                        IdProviderFactory.setDefaultPlatform(IdProviderFactory.PLATFORM_CSJ);
                        break;
                    case R.id.rb_ad_provider_gdt:
                        IdProviderFactory.setDefaultPlatform(IdProviderFactory.PLATFORM_GDT);
                        break;
                }
            }
        });
        rgd.check(R.id.rb_ad_provider_ms);
    }

//    public void showPopupWindow(Activity context, View parent){
//        LayoutInflater inflater = (LayoutInflater)
//                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View vPopupWindow=inflater.inflate(R.layout.interstitial_ad_layout, null, false);
//        final PopupWindow pw= new PopupWindow(vPopupWindow, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,false);
//
//        //Cancel按钮及其处理事件
//        View btnCancel=vPopupWindow.findViewById(R.id.popupwindow_cancel);
//        btnCancel.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                pw.dismiss();//关闭
//            }
//        });
//        pw.setOutsideTouchable(false);
//        //显示popupWindow对话框
//        pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
//    }

//    private CustomPopWindow pw;

//    public void showPopupWindow(Activity context, View parent) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        final View vPopupWindow = inflater.inflate(R.layout.interstitial_ad_layout, null);
//        //Cancel按钮及其处理事件
//        View btnCancel = vPopupWindow.findViewById(R.id.popupwindow_cancel);
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: ");
//                pw.dissmiss();
//            }
//        });
//        pw = new CustomPopWindow.PopupWindowBuilder(this)
//                .setView(vPopupWindow)
////                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
////                .setBgDarkAlpha(0.7f) // 控制亮度
//                .enableOutsideTouchableDissmiss(false)
//                .create();
//                pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
//
//    }

    private Popup pw;

//    private void showPopupWindow(Activity activity, View parent) {
//
//        pw = new Popup(activity, 400, 400);
//        pw.setPopupGravity(Gravity.CENTER)
//                .setShowAnimation(SimpleAnimationUtils.getTranslateVerticalAnimation(1f, 0, 200))
//                .setDismissAnimation(SimpleAnimationUtils.getTranslateVerticalAnimation(0, 1f, 200));
//        pw.setOutSideDismiss(false);
//        pw.showPopupWindow();
//        pw.setViewClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: ");
//                pw.dismiss();
//            }
//        }, pw.findViewById(R.id.popupwindow_cancel));
//    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.bannerAD:
                intent.setClass(this, BannerAdActivity.class);
                startActivity(intent);
                break;
            case R.id.slashAD:
                intent.setClass(this, SplashActivity.class);
                startActivity(intent);
                break;
            case R.id.interstitialAD:
                intent.setClass(this, InterstitialAdActivity.class);
                startActivity(intent);
                break;
            case R.id.nativeAD:
                intent.setClass(this, NativeAdSelectActivity.class);
                startActivity(intent);
                break;
            case R.id.nativeMediaAD:
                intent.setClass(this, NativeVideoActivity.class);
                startActivity(intent);
                break;
            case R.id.nativeRecyclerAD:
                intent.setClass(this, NativeRecyclerListSelectActivity.class);
                startActivity(intent);
                break;
            case R.id.rewardVideoAd:
                intent.setClass(this,RewardVideoActivity.class);
                startActivity(intent);
                break;
            case R.id.videoFeedAd:
                intent.setClass(this,VideoFeedActivity.class);
                startActivity(intent);
                break;
            case R.id.open_popupwindow:
//                showPopupWindow(this, this.findViewById(android.R.id.content));
//                MediaView mediaView = findViewById(R.id.media_video);
//                final ViewGroup mediaViewContainer = findViewById(R.id.media_video);
//                MediaView mediaView = new MediaView(this);
//                mediaView.setVideoListener(new MediaView.VideoListener() {
//                    @Override
//                    public void onLoaded(MediaView mediaView) {
//
//                    }
//                });
//                mediaViewContainer.addView(mediaView);
//                mediaView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.video_sample_2);
//                mediaView.setInternetResource("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4");
//                mediaView.start();

                break;
        }
    }

    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用SDK，否则SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);//申请经纬度坐标权限
        }

        if (lackedPermission.size() != 0) {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1024 && !hasAllPermissionsGranted(grantResults)) {
//            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
//            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            intent.setData(Uri.parse("package:" + getPackageName()));
//            startActivity(intent);
//        }
//    }
}
