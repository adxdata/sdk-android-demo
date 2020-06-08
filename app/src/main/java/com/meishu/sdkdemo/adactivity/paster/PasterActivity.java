package com.meishu.sdkdemo.adactivity.paster;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
        
        ((EditText) findViewById(R.id.alternativePasterAdPlaceID)).setText(IdProviderFactory.getDefaultProvider().paster());
        findViewById(R.id.loadPasterAd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                }
                
                String pid  = ((EditText) findViewById(R.id.alternativePasterAdPlaceID)).getText().toString().trim();
                if (TextUtils.isEmpty(pid)) {
                    pid = IdProviderFactory.getDefaultProvider().paster();
                }

                videoContainer = findViewById(R.id.video_container);

                if (pasterAd != null) {
                    pasterAd.destroy();
                    videoContainer.removeAllViews();
                }

                pasterAdLoader = new PasterAdLoader(PasterActivity.this, videoContainer, pid, PasterActivity.this);
                pasterAdLoader.loadAd();
            }
        });
    }

    @Override
    public void onVideoLoaded() {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
        pasterAd.start();
        Toast.makeText(PasterActivity.this, "开始播放", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVideoComplete() {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
        Toast.makeText(PasterActivity.this, "视频播放完毕", Toast.LENGTH_SHORT).show();
        pasterAdLoader.destroy();
    }

    @Override
    public void onAdLoaded(final PasterAd ad) {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
        pasterAd = ad;
        ad.setInteractionListener(new InteractionListener() {
            @Override
            public void onAdClicked() {
                // 点击时可以把广告关掉
//                pasterAdLoader.destroy();
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
        if (pasterAd != null) {
            pasterAd.destroy();
        }
    }
}
