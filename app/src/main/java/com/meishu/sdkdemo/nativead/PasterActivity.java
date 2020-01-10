package com.meishu.sdkdemo.nativead;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.meishu.sdk.core.ad.paster.PasterAd;
import com.meishu.sdk.core.ad.paster.PasterAdListener;
import com.meishu.sdk.core.ad.paster.PasterAdLoader;
import com.meishu.sdk.core.loader.InteractionListener;
import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adid.IdProviderFactory;

/**
 * 视频贴片
 */
public class PasterActivity extends AppCompatActivity implements PasterAdListener {

    private static final String TAG = "PasterActivity";
    private PasterAd pasterAd;
    private ViewGroup videoContainer;
    private PasterAdLoader pasterAdLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paster);
        videoContainer = findViewById(R.id.video_container);
        pasterAdLoader = new PasterAdLoader(this, videoContainer, IdProviderFactory.getDefaultProvider().videoImg(), this);
        pasterAdLoader.loadAd();
    }

    @Override
    public void onVideoLoaded() {
        pasterAd.start();
        Toast.makeText(PasterActivity.this, "开始播放", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVideoComplete() {
        Toast.makeText(PasterActivity.this, "视频播放完毕", Toast.LENGTH_SHORT).show();
        pasterAdLoader.destroy();
    }

    @Override
    public void onAdLoaded(final PasterAd ad) {
        pasterAd = ad;
        ad.setInteractionListener(new InteractionListener() {
            @Override
            public void onAdClicked() {
                // 点击时可以把广告关掉
                pasterAdLoader.destroy();
                Log.d(TAG, "onAdClicked: 广告被点击");
            }
        });
    }

    @Override
    public void onAdExposure() {
        Log.d(TAG, "onAdExposure: ");
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
    protected void onResume() {
        super.onResume();
        if (pasterAd != null) {
            pasterAd.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pasterAd != null) {
            pasterAd.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pasterAdLoader != null) {
            pasterAdLoader.destroy();
        }
    }
}
