package com.meishu.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.meishu.sdk.core.ad.interstitial.InterstitialAd;

public class InterstitialAdNewActivity extends AppCompatActivity {

    private static InterstitialAd interstitialAd1;
    private static InterstitialAd interstitialAd2;

    public static void setInterstitialAd1(InterstitialAd interstitialAd) {
        InterstitialAdNewActivity.interstitialAd1 = interstitialAd;
    }

    public static void setInterstitialAd2(InterstitialAd interstitialAd) {
        InterstitialAdNewActivity.interstitialAd2 = interstitialAd;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ad_new);

        findViewById(R.id.show1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd1 != null) {
                    interstitialAd1.showAd(InterstitialAdNewActivity.this);
                }
            }
        });
        findViewById(R.id.show2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd2 != null) {
                    interstitialAd2.showAd(InterstitialAdNewActivity.this);
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
        interstitialAd1 = null;
        interstitialAd2 = null;
    }
}
