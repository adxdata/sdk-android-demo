package com.meishu.sdkdemo.adactivity.splash;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.meishu.sdk.core.ad.splash.ISplashAd;
import com.meishu.sdk.core.ad.splash.SplashAdListener;
import com.meishu.sdk.core.ad.splash.SplashAdLoader;
import com.meishu.sdk.core.loader.InteractionListener;
import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adid.IdProviderFactory;

public class SplashActivity extends AppCompatActivity implements SplashAdListener {
    private static final String TAG = "SplashActivity";

    private ISplashAd splashAd;
    private volatile boolean canJump = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ((EditText) findViewById(R.id.alternativeSplashAdPlaceID)).setText(IdProviderFactory.getDefaultProvider().splash());
        findViewById(R.id.loadSplashAd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                }

                String pid  = ((EditText) findViewById(R.id.alternativeSplashAdPlaceID)).getText().toString().trim();
                if (TextUtils.isEmpty(pid)) {
                    pid = IdProviderFactory.getDefaultProvider().splash();
                }

                ViewGroup adContainer = findViewById(R.id.splash_container);
                SplashAdLoader splashAdLoader = new SplashAdLoader(SplashActivity.this, adContainer, pid, SplashActivity.this, 3000);
                splashAdLoader.loadAd();
            }
        });

        //ViewGroup adContainer = findViewById(R.id.splash_container);
        //SplashAdLoader splashAdLoader = new SplashAdLoader(this, adContainer, IdProviderFactory.getDefaultProvider().splash(), SplashActivity.this, 3000);
        //splashAdLoader.loadAd();
    }

    @Override
    public void onAdLoaded(ISplashAd splashAd) {
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {}
        findViewById(R.id.splash_container).setVisibility(View.VISIBLE);

        Log.d(TAG, "onAdLoaded: 开屏广告填充");
        this.splashAd = splashAd;
        splashAd.setInteractionListener(new InteractionListener() {

            @Override
            public void onAdClicked() {
                Log.d(TAG, "onAdClicked: 开屏广告被点击");
            }
        });
    }

    @Override
    public void onAdExposure() {
        Log.d(TAG, "onAdExposure: 开屏广告曝光");
    }

    @Override
    public void onAdClosed() {
        try {
            getSupportActionBar().show();
        } catch (Exception e) {}
        findViewById(R.id.splash_container).setVisibility(View.GONE);

        Log.d(TAG, "onAdClosed: 开屏广告被关闭");

        findViewById(R.id.splash_container).setVisibility(View.GONE);

        //if (canJump) {
        //    next();
        //}
        //canJump = true;
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: 暂停");
        super.onPause();
        canJump = false;
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
        Log.d(TAG, "onError: 没有加载到广告");
    }
}
