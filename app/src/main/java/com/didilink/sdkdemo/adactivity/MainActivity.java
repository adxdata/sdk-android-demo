package com.didilink.sdkdemo.adactivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
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
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.didilink.sdk.activity.GameCenterWebViewActivity;
import com.didilink.sdk.activity.GamePlayWebViewActivity;
import com.didilink.sdk.core.AdSdk;
import com.didilink.sdk.core.MSAdConfig;
import com.didilink.sdkdemo.R;
import com.didilink.sdkdemo.adactivity.banner.BannerAdActivity;
import com.didilink.sdkdemo.adactivity.draw.PrepareVideoFeedActivity;
import com.didilink.sdkdemo.adactivity.feed.NativeRecyclerListSelectActivity;
import com.didilink.sdkdemo.adactivity.fullscreenvideo.FullScreenVideoActivity;
import com.didilink.sdkdemo.adactivity.interstitial.InterstitialAdActivity;
import com.didilink.sdkdemo.adactivity.paster.PasterActivity;
import com.didilink.sdkdemo.adactivity.rewardvideo.RewardVideoActivity;
import com.didilink.sdkdemo.adactivity.splash.PrepareSplashActivity;
import com.didilink.sdkdemo.adid.IdProviderFactory;

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
        findViewById(R.id.nativeRecyclerAD).setOnClickListener(this);
        findViewById(R.id.rewardVideoAd).setOnClickListener(this);
        findViewById(R.id.videoFeedAd).setOnClickListener(this);
        findViewById(R.id.full_screen_video).setOnClickListener(this);
        findViewById(R.id.enter_game).setOnClickListener(this);
        // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
            checkNotifationPermission();
        }

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

            ((TextView) findViewById(R.id.main_info_demo)).setText(packageInfo.versionName);
            ((TextView) findViewById(R.id.main_info_didilink)).setText(AdSdk.getVersionName());
            ((TextView) findViewById(R.id.main_info_package)).setText(getPackageName());


            new Thread(() -> {
                try {
                    while (true) {
                        if (!TextUtils.isEmpty(AdSdk.getOaid())) {
                            ((TextView) findViewById(R.id.main_info_oaid)).setText(AdSdk.getOaid());
                            break;
                        }
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initAdProvider() {
        IdProviderFactory.setDefaultPlatform(IdProviderFactory.PLATFORM_MS);
    }

    private void initAdDownloadMode() {
        RadioGroup rgd = findViewById(R.id.rdg_ad_download_mode);

        rgd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_ad_download_mode_directly:
                        AdSdk.adConfig().downloadConfirm(MSAdConfig.DOWNLOAD_CONFIRM_NEVER);
                        break;
                    case R.id.rb_ad_download_mode_notify:
                        AdSdk.adConfig().downloadConfirm(MSAdConfig.DOWNLOAD_CONFIRM_ALWAYS);
                        break;
                    case R.id.rb_ad_download_mode_wifi_directly:
                        AdSdk.adConfig().downloadConfirm(MSAdConfig.DOWNLOAD_CONFIRM_AUTO);
                        break;
                }
            }
        });
        rgd.check(R.id.rb_ad_download_mode_wifi_directly);
    }

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
            case R.id.nativeRecyclerAD:
                intent.setClass(this, NativeRecyclerListSelectActivity.class);
                startActivity(intent);
                break;
            case R.id.rewardVideoAd:
                intent.setClass(this, RewardVideoActivity.class);
                startActivity(intent);
                break;
            case R.id.videoFeedAd:
                intent.setClass(this, PrepareVideoFeedActivity.class);
                startActivity(intent);
                break;
            case R.id.full_screen_video:
                intent.setClass(this, FullScreenVideoActivity.class);
                startActivity(intent);
                break;
            case R.id.enter_game:
                GameCenterWebViewActivity.startActivity(this,"http://static.adtianshi.cn/game/gamecenter/index.html");
//                GamePlayWebViewActivity.startActivity(this,"",1);
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
