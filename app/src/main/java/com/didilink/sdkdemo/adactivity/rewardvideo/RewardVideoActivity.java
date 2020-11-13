package com.didilink.sdkdemo.adactivity.rewardvideo;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.didilink.sdk.core.ad.reward.RewardAdMediaListener;
import com.didilink.sdk.core.ad.reward.RewardVideoAd;
import com.didilink.sdk.core.ad.reward.RewardVideoAdListener;
import com.didilink.sdk.core.ad.reward.RewardVideoLoader;
import com.didilink.sdk.core.loader.AdPlatformError;
import com.didilink.sdk.core.loader.InteractionListener;
import com.didilink.sdk.core.utils.LogUtil;
import com.didilink.sdkdemo.R;
import com.didilink.sdkdemo.adid.IdProviderFactory;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class RewardVideoActivity extends AppCompatActivity implements View.OnClickListener, RewardVideoAdListener {
    private static final String TAG = "RewardVideoActivity";

    private RewardVideoAd ad;
    private RewardVideoAd ad2;
    private RewardVideoLoader rewardVideoLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_video);
        findViewById(R.id.change_orientation).setOnClickListener(this);
        findViewById(R.id.load_video).setOnClickListener(this);

        String posId;
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == ORIENTATION_PORTRAIT) {
            posId = IdProviderFactory.getDefaultProvider().rewardPortrait();
        } else if (currentOrientation == ORIENTATION_LANDSCAPE) {
            posId = IdProviderFactory.getDefaultProvider().rewardLandscape();
        } else {
            LogUtil.e(TAG, "orientation error");
            return;
        }

        ((EditText) findViewById(R.id.alternativeRewardVideoAdPlaceID)).setText(posId);
        rewardVideoLoader = new RewardVideoLoader(this, posId, this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.change_orientation:
                int currentOrientation = getResources().getConfiguration().orientation;
                if (currentOrientation == ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if (currentOrientation == ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
            case R.id.load_video:
                this.ad = null;
                this.ad2 = null;

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                }
                String pid  = ((EditText) findViewById(R.id.alternativeRewardVideoAdPlaceID)).getText().toString().trim();
                if (TextUtils.isEmpty(pid)) {
                    return;
                }

                if (null == rewardVideoLoader) {
                    rewardVideoLoader   = new RewardVideoLoader(this, pid, this);
                } else if (!rewardVideoLoader.getPosId().equals(pid)) {
                    rewardVideoLoader.destroy();
                    rewardVideoLoader   = new RewardVideoLoader(this, pid, this);
                }

                rewardVideoLoader.loadAd();
                rewardVideoLoader.loadAd();
                break;
        }
    }

    @Override
    public void onAdLoaded(RewardVideoAd ad) {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
        if (this.ad == null) {
            this.ad = ad;
            View show1 = findViewById(R.id.show1);
            show1.setEnabled(true);
            show1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RewardVideoActivity.this.ad.showAd();
                }
            });
        } else {
            this.ad2 = ad;
            View show2 = findViewById(R.id.show2);
            show2.setEnabled(true);
            show2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RewardVideoActivity.this.ad2.showAd();
                }
            });
        }
//        ad.setInteractionListener(new InteractionListener() {
//            @Override
//            public void onAdClicked() {
//                Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
//            }
//        });
        ad.setInteractionListener(new InteractionListener() {
            @Override
            public void onAdClicked() {
                Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            }
        });
        ad.setMediaListener(new RewardAdMediaListener() {
            @Override
            public void onVideoCompleted() {
                Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            }
        });
    }

    @Override
    public void onVideoCached() {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
    }

    @Override
    public void onAdExposure() {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
    }

    @Override
    public void onReward() {
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
    public void onAdPlatformError(AdPlatformError e) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 单独销毁广告对象
//        if (ad != null) {
//            ad.destroy();
//        }
//        if (ad2 != null) {
//            ad2.destroy();
//        }
        // 销毁 loader 会销毁已加载的所有广告，建议使用此方法，而不是每个广告单独销毁
        // 一定要调用此方法，否则会产生内存泄漏
        if (rewardVideoLoader != null) {
            rewardVideoLoader.destroy();
        }
    }
}
