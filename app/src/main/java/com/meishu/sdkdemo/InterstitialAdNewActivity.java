package com.meishu.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.meishu.sdk.core.ad.interstitial.InterstitialAd;

public class InterstitialAdNewActivity extends AppCompatActivity {

    private static InterstitialAd interstitialAd;

    public static void setInterstitialAd(InterstitialAd interstitialAd) {
        InterstitialAdNewActivity.interstitialAd = interstitialAd;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ad_new);

        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd != null) {
                    interstitialAd.showAd(InterstitialAdNewActivity.this);
                }
            }
        });
//        findViewById(R.id.show_error).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (interstitialAd != null) {
//                    interstitialAd.showAd();
//                }
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        interstitialAd = null;
    }
}
