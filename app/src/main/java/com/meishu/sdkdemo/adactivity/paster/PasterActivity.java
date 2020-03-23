package com.meishu.sdkdemo.adactivity.paster;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

        findViewById(R.id.btn_rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration mConfiguration = v.getResources().getConfiguration(); //获取设置的配置信息
                int ori = mConfiguration.orientation; //获取屏幕方向
                if (ori == Configuration.ORIENTATION_LANDSCAPE) {
                    //横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
                } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
                    //竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
                }
            }
        });
        findViewById(R.id.btn_resume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pasterAd != null) {
                    pasterAd.resume();
                }
            }
        });
        findViewById(R.id.btn_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pasterAd != null) {
                    pasterAd.pause();
                }
            }
        });
        findViewById(R.id.btn_mute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pasterAd != null) {
                    pasterAd.mute();
                }
            }
        });
        findViewById(R.id.btn_unmute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pasterAd != null) {
                    pasterAd.unmute();
                }
            }
        });

        videoContainer = findViewById(R.id.video_container);
        pasterAdLoader = new PasterAdLoader(this, videoContainer, IdProviderFactory.getDefaultProvider().paster(), this);
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
//                pasterAdLoader.destroy();
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
