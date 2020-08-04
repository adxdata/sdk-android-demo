package com.meishu.sdkdemo.adactivity.fullscreenvideo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.meishu.sdk.core.ad.fullscreenvideo.FullScreenVideoAd;
import com.meishu.sdk.core.ad.fullscreenvideo.FullScreenVideoAdListener;
import com.meishu.sdk.core.ad.fullscreenvideo.FullScreenVideoAdLoader;
import com.meishu.sdk.core.loader.InteractionListener;
import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adid.IdProviderFactory;

public class FullScreenVideoActivity extends Activity implements FullScreenVideoAdListener {

    private final static String TAG = "FullScreenVideoActivity";
    private FullScreenVideoAdLoader fullScreenVideoAdLoader;
    private FullScreenVideoAd fullScreenVideoAd;

    private Button btnLoad;
    private Button btnShow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        btnLoad = findViewById(R.id.loadFullScreenVideoAd);
        btnShow = findViewById(R.id.showFullScreenVideoAd);
        EditText editPid = findViewById(R.id.alternativeFullScreenVideoAdPlaceID);
        editPid.setText(IdProviderFactory.getDefaultProvider().fullScreenVideo());

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnShow.setEnabled(false);
                if (fullScreenVideoAdLoader != null) {
                    fullScreenVideoAdLoader.destroy();
                }
                fullScreenVideoAdLoader = new FullScreenVideoAdLoader(FullScreenVideoActivity.this, editPid.getText().toString(), FullScreenVideoActivity.this);
                fullScreenVideoAdLoader.loadAd();
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fullScreenVideoAd != null) {
                    fullScreenVideoAd.showAd();
                }
            }
        });
    }

    @Override
    public void onAdLoaded(FullScreenVideoAd fullScreenVideoAd) {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
        btnShow.setEnabled(true);
        this.fullScreenVideoAd = fullScreenVideoAd;
        fullScreenVideoAd.setInteractionListener(new InteractionListener() {
            @Override
            public void onAdClicked() {
                Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            }
        });
    }

    @Override
    public void onAdError() {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
    }

    @Override
    public void onAdExposure() {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
    }

    @Override
    public void onAdClosed() {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
    }
}
