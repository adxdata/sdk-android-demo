package com.meishu.sdkdemo.adactivity.banner;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.meishu.sdk.core.ad.banner.BannerAdListener;
import com.meishu.sdk.core.ad.banner.BannerAdLoader;
import com.meishu.sdk.core.ad.banner.IBannerAd;
import com.meishu.sdk.core.loader.InteractionListener;
import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adid.IdProviderFactory;

public class BannerAdActivity extends AppCompatActivity implements View.OnClickListener, BannerAdListener {
    private static final String TAG = "BannerADActivity";

    private BannerAdLoader bannerLoader;
    private ViewGroup bannerContainer;

    private boolean showCloseButton = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_ad);

        bannerContainer = findViewById(R.id.bannerContainer);
        Button bannerAD = findViewById(R.id.loadBannerAd);
        bannerAD.setOnClickListener(this);
        findViewById(R.id.loadBannerAdWithoutCloseBtn).setOnClickListener(this);

        ((EditText) findViewById(R.id.alternativeBannerAdPlaceID)).setText(IdProviderFactory.getDefaultProvider().banner());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadBannerAd:
            case R.id.loadBannerAdWithoutCloseBtn:
                showCloseButton = v.getId() == R.id.loadBannerAd;

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                }

                String pid  = ((EditText) findViewById(R.id.alternativeBannerAdPlaceID)).getText().toString().trim();
                if (TextUtils.isEmpty(pid)) {
                    pid = IdProviderFactory.getDefaultProvider().banner();
                }

                bannerContainer.removeAllViews();
                bannerLoader = new BannerAdLoader(this, pid, this);
                bannerLoader.loadAd();
                break;
        }
    }

    @Override
    public void onAdLoaded(IBannerAd bannerAd) {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
        // 不显示关闭按钮，仅限美数
        bannerAd.setCloseButtonVisible(showCloseButton);
        // 适应 container 的大小需要设置宽高，仅限美数
        bannerAd.setWidthAndHeight(bannerContainer.getMeasuredWidth(), bannerContainer.getMeasuredHeight());
        bannerContainer.addView(bannerAd.getAdView());
        bannerAd.setInteractionListener(new InteractionListener() {
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
    }

    @Override
    public void onAdError() {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.bannerLoader != null) {
            this.bannerLoader.destroy();
        }
    }
}
