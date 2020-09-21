package com.meishu.sdkdemo.adactivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.meishu.sdk.core.AdSdk;
import com.meishu.sdk.meishu_ad.interstitial.Popup;
import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adactivity.banner.BannerAdActivity;
import com.meishu.sdkdemo.adactivity.draw.PrepareVideoFeedActivity;
import com.meishu.sdkdemo.adactivity.feed.NativeRecyclerListSelectActivity;
import com.meishu.sdkdemo.adactivity.feed.NativeVideoActivity;
import com.meishu.sdkdemo.adactivity.fullscreenvideo.FullScreenVideoActivity;
import com.meishu.sdkdemo.adactivity.interstitial.InterstitialAdActivity;
import com.meishu.sdkdemo.adactivity.paster.PasterActivity;
import com.meishu.sdkdemo.adactivity.rewardvideo.RewardVideoActivity;
import com.meishu.sdkdemo.adactivity.splash.PrepareSplashActivity;
import com.meishu.sdkdemo.adid.IdProviderFactory;

import java.util.ArrayList;
import java.util.List;

import static android.provider.Settings.EXTRA_APP_PACKAGE;
import static android.provider.Settings.EXTRA_CHANNEL_ID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bannerAD = findViewById(R.id.bannerAD);
        bannerAD.setOnClickListener(this);
        findViewById(R.id.slashAD).setOnClickListener(this);
        findViewById(R.id.interstitialAD).setOnClickListener(this);
        findViewById(R.id.pasterAD).setOnClickListener(this);
        findViewById(R.id.nativeMediaAD).setOnClickListener(this);
        findViewById(R.id.nativeRecyclerAD).setOnClickListener(this);
        findViewById(R.id.rewardVideoAd).setOnClickListener(this);
        findViewById(R.id.videoFeedAd).setOnClickListener(this);
        findViewById(R.id.full_screen_video).setOnClickListener(this);
        // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
            checkNotifationPermission();
        }


        findViewById(R.id.open_popupwindow).setOnClickListener(this);



        initAdProvider();

        initAdDownloadMode();

        initVersionName();
    }

    private void checkNotifationPermission() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        boolean isEnabled = manager.areNotificationsEnabled();
        if (!isEnabled){
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("通知功能未开启，无法在通知栏显示下载状态，去开启吧～")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                                    //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                                    intent.putExtra(EXTRA_APP_PACKAGE, getPackageName());
                                    intent.putExtra(EXTRA_CHANNEL_ID, getApplicationInfo().uid);
                                }else {
                                    //5.0-7.1 21-25
                                    intent.putExtra("app_package", getPackageName());
                                    intent.putExtra("app_uid", getApplicationInfo().uid);
                                }

                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                                //直接跳转到当前应用的设置界面
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            dialog.show();

            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

            lp.width    = (int) (((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() * 0.7);
            dialog.getWindow().setAttributes(lp);

        }
    }

    private void initVersionName() {
        try {
            PackageManager pm = getPackageManager();
            String packageName = getPackageName();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            TextView txtVersion = findViewById(R.id.txt_version);
            txtVersion.setText(String.format("Demo 版本：%s\n" +
                            "美数 SDK 版本：%s\n" +
                            "穿山甲 SDK 版本：%s\n" +
                            "百度 SDK 版本：%s\n" +
                            "广点通 SDK 版本：%s\n" +
                            "快手 SDK 版本：%s\n" +
                            "OPPO SDK 版本：%s\n" +
                            "小米 SDK 版本：%s\n" +
                            "oaid: %s\n" +
                            "包名: %s",
                    packageInfo.versionName,
                    AdSdk.getVersionName(),
                    AdSdk.getCSJVersionName(),
                    AdSdk.getBDVersionName(),
                    AdSdk.getGDTVersionName(),
                    AdSdk.getKSVersionName(),
                    AdSdk.getOPPOVersionName(),
                    AdSdk.getMimoVersionName(),
                    AdSdk.getOaid(),
                    getPackageName()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initAdProvider() {
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar == null) {
//            return;
//        }
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setCustomView(R.layout.layout_ad_provider);
        // 修改默认的广告提供者
        RadioGroup rgd = findViewById(R.id.rdg_ad_provider);
        if (null != AdSdk.getOPPOVersionName()) {
            rgd.findViewById(R.id.rb_ad_provider_oppo).setVisibility(View.VISIBLE);
        } else if ("Xiaomi".equals(Build.MANUFACTURER)) {
            rgd.findViewById(R.id.rb_ad_provider_mimo).setVisibility(View.VISIBLE);
        }
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
                    case R.id.rb_ad_provider_ks:
                        IdProviderFactory.setDefaultPlatform(IdProviderFactory.PLATFORM_KS);
                        break;
                    case R.id.rb_ad_provider_oppo:
                        IdProviderFactory.setDefaultPlatform(IdProviderFactory.PLATFORM_OPPO);
                        break;
                    case R.id.rb_ad_provider_mimo:
                        IdProviderFactory.setDefaultPlatform(IdProviderFactory.PLATFORM_MIMO);
                        break;
                }
            }
        });
        rgd.check(R.id.rb_ad_provider_ms);
    }

    private void initAdDownloadMode() {
        RadioGroup rgd = findViewById(R.id.rdg_ad_download_mode);

        rgd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_ad_download_mode_directly:
                        //AdSdk.setDownloadMode(AdSdk.DOWNLOAD_MODE_DIRECTLY);
                        break;
                    case R.id.rb_ad_download_mode_notify:
                        //AdSdk.setDownloadMode(AdSdk.DOWNLOAD_MODE_NOTIFY);
                        break;
                    case R.id.rb_ad_download_mode_wifi_directly:
                        //AdSdk.setDownloadMode(AdSdk.DOWNLOAD_MODE_WIFI_DIRECTLY);
                        break;
                }
            }
        });
        rgd.check(R.id.rb_ad_download_mode_wifi_directly);
    }

//    public void showPopupWindow(Activity context, View parent){
//        LayoutInflater inflater = (LayoutInflater)
//                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View vPopupWindow=inflater.inflate(R.layout.meishu_interstitial_ad_layout, null, false);
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
//        final View vPopupWindow = inflater.inflate(R.layout.meishu_interstitial_ad_layout, null);
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
                intent.setClass(this, PrepareSplashActivity.class);
                startActivity(intent);
                break;
            case R.id.interstitialAD:
                intent.setClass(this, InterstitialAdActivity.class);
                startActivity(intent);
                break;
            case R.id.pasterAD:
                intent.setClass(this, PasterActivity.class);
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
                intent.setClass(this, RewardVideoActivity.class);
                startActivity(intent);
                break;
            case R.id.videoFeedAd:
                if (!IdProviderFactory.PLATFORM_CSJ.equals(IdProviderFactory.getDefaultProvider().platformName())) {
                    Toast.makeText(getApplicationContext(), "此类广告目前不支持，请修改广告提供商", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.setClass(this, PrepareVideoFeedActivity.class);
                startActivity(intent);
                break;
            case R.id.full_screen_video:
                intent.setClass(this, FullScreenVideoActivity.class);
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
