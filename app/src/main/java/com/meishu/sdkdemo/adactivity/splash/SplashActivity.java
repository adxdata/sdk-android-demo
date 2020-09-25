package com.meishu.sdkdemo.adactivity.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.meishu.sdk.core.ad.splash.ISplashAd;
import com.meishu.sdk.core.ad.splash.SplashAdListener;
import com.meishu.sdk.core.ad.splash.SplashAdLoader;
import com.meishu.sdk.core.loader.AdPlatformError;
import com.meishu.sdk.core.loader.InteractionListener;
import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adid.IdProviderFactory;

public class SplashActivity extends AppCompatActivity implements SplashAdListener {
    private static final String TAG = "SplashActivity";

    private ISplashAd splashAd;
    private boolean canJump = true;
    private Button btnShow;
    private Button btnSkip;
    private boolean autoShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_splash);
        final ViewGroup adContainer = findViewById(R.id.splash_container);

        String pid  = getIntent().getStringExtra("alternativePlaceId");
        if (TextUtils.isEmpty(pid)) {
            pid = IdProviderFactory.getDefaultProvider().splash();
        }

        btnShow = findViewById(R.id.btn_show);
        btnSkip = findViewById(R.id.btn_skip);

        SplashAdLoader splashAdLoader;
        Integer id = getIntent().getIntExtra("id", -1);
        switch (id) {
            case R.id.loadAndShowSplashAd:
                splashAdLoader = new SplashAdLoader(this, adContainer, pid, SplashActivity.this, 3000);
                splashAdLoader.loadAd();
                autoShow = true;
                break;
            case R.id.loadSplashAd:
                splashAdLoader = new SplashAdLoader(this, pid, SplashActivity.this, 3000);
                btnShow.setVisibility(View.VISIBLE);
                splashAdLoader.loadAdOnly();
                autoShow = false;
                break;
            case R.id.customSkipSplashAd:
                splashAdLoader = new SplashAdLoader(this, adContainer, pid, SplashActivity.this, 3000);
                btnSkip.setVisibility(View.VISIBLE);
                splashAdLoader.loadAd(btnSkip);
                autoShow = true;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }

        findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (splashAd != null) {
                    splashAd.showAd(adContainer);
                }
            }
        });
    }

    @Override
    public void onAdLoaded(ISplashAd splashAd) {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
        this.splashAd = splashAd;
        if (!autoShow) {
            btnShow.setEnabled(true);
        }
        splashAd.setInteractionListener(new InteractionListener() {

            @Override
            public void onAdClicked() {
                Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            }
        });
    }

    @Override
    public void onAdExposure() {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
    }

    @Override
    public void onAdClosed() {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));

        if (canJump) {
            next();
        }
        canJump = true;
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: 暂停");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: 开屏界面停止运行");
        super.onStop();
        canJump=true;
    }

    private void next() {
        // 此处打开需要的 activity 即可
        this.finish();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        if (this.splashAd != null && canJump) {
//            next();
        }
        canJump = true;
    }

    @Override
    public void onAdError() {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
        Toast.makeText(this.getApplicationContext(), "没有加载到广告", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public void onAdPlatformError(AdPlatformError e) {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()) + " " + e);
    }

    @Override
    public void onAdPresent(ISplashAd splashAd) {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
    }

    @Override
    public void onAdSkip(ISplashAd splashAd) {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
    }

    @Override
    public void onAdTimeOver(ISplashAd splashAd) {
        // 仅支持美数和穿山甲，倒计时结束时回调
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
    }

    @Override
    public void onAdTick(long leftMilliseconds) {
        // 仅支持美数和广点通，回调剩余时间
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()) + " " + leftMilliseconds);
        btnSkip.setText(leftMilliseconds + "");
    }
}
