package com.meishu.sdkdemo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.meishu.sdk.core.ad.reward.RewardAdMediaListener;
import com.meishu.sdk.core.ad.reward.RewardVideoAd;
import com.meishu.sdk.core.ad.reward.RewardVideoAdListener;
import com.meishu.sdk.core.ad.reward.RewardVideoLoader;
import com.meishu.sdk.core.loader.InteractionListener;
import com.meishu.sdkdemo.adid.IdProviderFactory;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class RewardVideoActivity extends AppCompatActivity implements View.OnClickListener, RewardVideoAdListener {
    private static final String TAG = "RewardVideoActivity";

    private RewardVideoAd ad;
    private String posId = "300002";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_video);
        findViewById(R.id.change_orientation).setOnClickListener(this);
        findViewById(R.id.load_video).setOnClickListener(this);

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == ORIENTATION_PORTRAIT) {
            posId = IdProviderFactory.getDefaultProvider().rewardPortrait();
        } else if (currentOrientation == ORIENTATION_LANDSCAPE) {
            posId = IdProviderFactory.getDefaultProvider().rewardLandscape();
        }
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
                RewardVideoLoader loader = new RewardVideoLoader(this, posId, this);
                loader.loadAd();
                break;
        }
    }

    @Override
    public void onAdLoaded(RewardVideoAd ad) {
        this.ad = ad;
        ad.setInteractionListener(new InteractionListener() {
            @Override
            public void onAdClicked() {
                Log.d(TAG, "onAdClicked: ");
            }
        });
        ad.setMediaListener(new RewardAdMediaListener() {
            @Override
            public void onVideoCompleted() {
                Log.d(TAG, "onVideoCompleted: ");
            }
        });
        ad.showAd();
    }

    @Override
    public void onVideoCached() {
        Log.d(TAG, "onVideoCached: ");
    }

    @Override
    public void onAdExposure() {
        Log.d(TAG, "onAdExposure: ");
    }

    @Override
    public void onReward() {
        Log.d(TAG, "onReward: ");
    }

    @Override
    public void onAdClosed() {
        Log.d(TAG, "onAdClosed: ");
    }

    @Override
    public void onAdError() {
        Log.d(TAG, "onAdError: ");
    }

    @Override
    protected void onDestroy() {
        if (this.ad != null) {
            this.ad.destroy();
        }
        super.onDestroy();
    }
}
