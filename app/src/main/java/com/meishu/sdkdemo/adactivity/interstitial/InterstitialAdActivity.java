package com.meishu.sdkdemo.adactivity.interstitial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.meishu.sdk.core.ad.interstitial.InterstitialAd;
import com.meishu.sdk.core.ad.interstitial.InterstitialAdListener;
import com.meishu.sdk.core.ad.interstitial.InterstitialAdLoader;
import com.meishu.sdk.core.loader.AdPlatformError;
import com.meishu.sdk.core.loader.InteractionListener;
import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adid.IdProviderFactory;

public class InterstitialAdActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "InterstitialADActivity";

    private InterstitialAd interstitialAd1;
    private InterstitialAd interstitialAd2;

    private String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ad);

        ((EditText) findViewById(R.id.alternativeInterstitailADPlaceID)).setText(IdProviderFactory.getDefaultProvider().insertScreen());

        findViewById(R.id.loadInterstitailAD).setOnClickListener(this);
        findViewById(R.id.showInterstitailAD1).setOnClickListener(this);
        findViewById(R.id.showInterstitailAD2).setOnClickListener(this);
        findViewById(R.id.openNewActivity).setOnClickListener(this);
    }

    private InterstitialAdLoader interstitialAdLoader1;
    private InterstitialAdLoader interstitialAdLoader2;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadInterstitailAD:
                findViewById(R.id.showInterstitailAD1).setEnabled(false);
                findViewById(R.id.showInterstitailAD2).setEnabled(false);
                findViewById(R.id.openNewActivity).setEnabled(false);
                if (interstitialAdLoader1 != null) {
                    interstitialAdLoader1.destroy();
                }
                if (interstitialAdLoader2 != null) {
                    interstitialAdLoader2.destroy();
                }

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                }

                placeId = ((EditText) findViewById(R.id.alternativeInterstitailADPlaceID)).getText().toString().trim();
                if (TextUtils.isEmpty(placeId)) {
                    placeId = IdProviderFactory.getDefaultProvider().insertScreen();
                }

                interstitialAdLoader1 = new InterstitialAdLoader(this, placeId, interstitialAdListener1);
                interstitialAdLoader1.loadAd();
                break;
            case R.id.showInterstitailAD1:
                interstitialAd1.showAd();
                break;
            case R.id.showInterstitailAD2:
                interstitialAd2.showAd();
                break;
            case R.id.openNewActivity:
                Intent intent = new Intent(this, InterstitialAdNewActivity.class);
                InterstitialAdNewActivity.setInterstitialAd1(interstitialAd1);
                InterstitialAdNewActivity.setInterstitialAd2(interstitialAd2);
                startActivity(intent);
                break;
        }
    }

    private InterstitialAdListener interstitialAdListener1 = new InterstitialAdListener() {
        @Override
        public void onAdLoaded(InterstitialAd interstitialAd) {
            Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            interstitialAdLoader2 = new InterstitialAdLoader(InterstitialAdActivity.this, placeId, interstitialAdListener2);
            interstitialAdLoader2.loadAd();
            Log.d(TAG, "onAdLoaded: ");
            interstitialAd1 = interstitialAd;
            findViewById(R.id.showInterstitailAD1).setEnabled(true);
            findViewById(R.id.openNewActivity).setEnabled(true);
            interstitialAd.setInteractionListener(new InteractionListener() {
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
        public void onAdPlatformError(AdPlatformError e) {

        }
    };

    private InterstitialAdListener interstitialAdListener2 = new InterstitialAdListener() {
        @Override
        public void onAdLoaded(InterstitialAd interstitialAd) {
            Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            interstitialAd2 = interstitialAd;
            findViewById(R.id.showInterstitailAD2).setEnabled(true);
            interstitialAd.setInteractionListener(new InteractionListener() {
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
        public void onAdPlatformError(AdPlatformError e) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (interstitialAdLoader1 != null) {
            interstitialAdLoader1.destroy();
        }
        if (interstitialAdLoader2 != null) {
            interstitialAdLoader2.destroy();
        }
    }
}
