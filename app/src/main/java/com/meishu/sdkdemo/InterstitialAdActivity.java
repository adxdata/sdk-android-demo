package com.meishu.sdkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.meishu.sdk.core.ad.interstitial.InterstitialAd;
import com.meishu.sdk.core.loader.InteractionListener;
import com.meishu.sdk.core.ad.interstitial.InterstitialAdLoader;
import com.meishu.sdk.core.ad.interstitial.InterstitialAdListener;
import com.meishu.sdkdemo.adid.IdProviderFactory;

public class InterstitialAdActivity extends AppCompatActivity implements View.OnClickListener, InterstitialAdListener {
    private static final String TAG = "InterstitialADActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ad);
        findViewById(R.id.showInterstitailAD).setOnClickListener(this);
    }

    private InterstitialAdLoader interstitialAdLoader;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showInterstitailAD:
                if (interstitialAdLoader != null) {
                    interstitialAdLoader.destroy();
                }
                interstitialAdLoader = new InterstitialAdLoader(this, IdProviderFactory.getDefaultProvider().insertScreen(), this);
                interstitialAdLoader.loadAd();
                break;
        }
    }

    @Override
    public void onAdLoaded(InterstitialAd interstitialAd) {
        Log.d(TAG, "onAdLoaded: ");
        interstitialAd.setInteractionListener(new InteractionListener() {
            @Override
            public void onAdClicked() {
                Log.d(TAG, "onAdClicked: 广告被点击");
            }
        });
    }

    @Override
    public void onAdExposure() {
        Log.d(TAG, "onAdExposure: 广告曝光");
    }

    @Override
    public void onAdClosed() {
        Log.d(TAG, "onAdClosed: 广告关闭");
    }

    @Override
    public void onAdError() {
        Log.d(TAG, "onAdError: 没有加载到广告");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (interstitialAdLoader != null) {
            interstitialAdLoader.destroy();
        }
    }
}
