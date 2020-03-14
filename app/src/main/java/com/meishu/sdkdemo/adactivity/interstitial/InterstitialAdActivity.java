package com.meishu.sdkdemo.adactivity.interstitial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.meishu.sdk.core.ad.interstitial.InterstitialAd;
import com.meishu.sdk.core.ad.interstitial.InterstitialAdListener;
import com.meishu.sdk.core.ad.interstitial.InterstitialAdLoader;
import com.meishu.sdk.core.loader.InteractionListener;
import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adid.IdProviderFactory;

public class InterstitialAdActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "InterstitialADActivity";

    private InterstitialAd interstitialAd1;
    private InterstitialAd interstitialAd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ad);
        findViewById(R.id.loadInterstitailAD).setOnClickListener(this);
        findViewById(R.id.showInterstitailAD1).setOnClickListener(this);
        findViewById(R.id.showInterstitailAD2).setOnClickListener(this);
        findViewById(R.id.openNewActivity).setOnClickListener(this);
    }

    private InterstitialAdLoader interstitialAdLoader1;
    private InterstitialAdLoader interstitialAdLoader2;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadInterstitailAD:
                findViewById(R.id.showInterstitailAD1).setEnabled(false);
                findViewById(R.id.showInterstitailAD2).setEnabled(false);
                findViewById(R.id.openNewActivity).setEnabled(false);
                if (interstitialAdLoader1 != null) {
                    interstitialAdLoader1.destroy();
                }
                if (interstitialAdLoader2 != null) {
                    interstitialAdLoader2.destroy();
                }
                interstitialAdLoader1 = new InterstitialAdLoader(this, IdProviderFactory.getDefaultProvider().insertScreen(), interstitialAdListener1);
                interstitialAdLoader1.loadAd();
                break;
            case R.id.showInterstitailAD1:
                interstitialAd1.showAd();
                break;
            case R.id.showInterstitailAD2:
                interstitialAd2.showAd();
                break;
            case R.id.openNewActivity:
                Intent intent = new Intent(this, InterstitialAdNewActivity.class);
                InterstitialAdNewActivity.setInterstitialAd1(interstitialAd1);
                InterstitialAdNewActivity.setInterstitialAd2(interstitialAd2);
                startActivity(intent);
                break;
        }
    }

    private InterstitialAdListener interstitialAdListener1 = new InterstitialAdListener() {
        @Override
        public void onAdLoaded(InterstitialAd interstitialAd) {
            interstitialAdLoader2 = new InterstitialAdLoader(InterstitialAdActivity.this, IdProviderFactory.getDefaultProvider().insertScreen(), interstitialAdListener2);
            interstitialAdLoader2.loadAd();
            Log.d(TAG, "onAdLoaded: ");
            interstitialAd1 = interstitialAd;
            findViewById(R.id.showInterstitailAD1).setEnabled(true);
            findViewById(R.id.openNewActivity).setEnabled(true);
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
    };

    private InterstitialAdListener interstitialAdListener2 = new InterstitialAdListener() {
        @Override
        public void onAdLoaded(InterstitialAd interstitialAd) {
            Log.d(TAG, "onAdLoaded: ");
            interstitialAd2 = interstitialAd;
            findViewById(R.id.showInterstitailAD2).setEnabled(true);
            interstitialAd.setInteractionListener(new InteractionListener() {
                @Override
                public void onAdClicked() {
                    Log.d(TAG, "onAdClicked: 广告被点击2");
                }
            });
        }
        @Override
        public void onAdExposure() {
            Log.d(TAG, "onAdExposure: 广告曝光2");
        }
        @Override
        public void onAdClosed() {
            Log.d(TAG, "onAdClosed: 广告关闭2");
        }
        @Override
        public void onAdError() {
            Log.d(TAG, "onAdError: 没有加载到广告2");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (interstitialAdLoader1 != null) {
            interstitialAdLoader1.destroy();
        }
        if (interstitialAdLoader2 != null) {
            interstitialAdLoader2.destroy();
        }
    }
}
