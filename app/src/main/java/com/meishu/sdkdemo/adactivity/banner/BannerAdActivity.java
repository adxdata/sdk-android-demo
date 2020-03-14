package com.meishu.sdkdemo.adactivity.banner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.meishu.sdk.core.ad.banner.BannerAdListener;
import com.meishu.sdk.core.ad.banner.BannerAdLoader;
import com.meishu.sdk.core.ad.banner.IBannerAd;
import com.meishu.sdk.core.loader.InteractionListener;
import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adid.IdProviderFactory;

public class BannerAdActivity extends AppCompatActivity implements View.OnClickListener, BannerAdListener {
    private static final String TAG = "BannerADActivity";

    private BannerAdLoader bannerLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_ad);
        Button bannerAD = findViewById(R.id.loadBannerAd);
        bannerAD.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadBannerAd:
                ((ViewGroup) findViewById(R.id.bannerContainer)).removeAllViews();
                bannerLoader = new BannerAdLoader(this, IdProviderFactory.getDefaultProvider().banner(), this);
                bannerLoader.loadAd();
                break;
        }
    }

    @Override
    public void onAdLoaded(IBannerAd bannerAd) {
        ((ViewGroup) findViewById(R.id.bannerContainer)).addView(bannerAd.getAdView());
        bannerAd.setInteractionListener(new InteractionListener() {
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
        Log.d(TAG, "onError: 没有加载到广告");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.bannerLoader != null) {
            this.bannerLoader.destroy();
        }
    }
}
