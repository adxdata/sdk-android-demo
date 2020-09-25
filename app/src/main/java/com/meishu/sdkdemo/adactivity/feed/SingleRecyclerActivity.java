package com.meishu.sdkdemo.adactivity.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meishu.sdk.core.ad.recycler.RecyclerAdData;
import com.meishu.sdk.core.ad.recycler.RecyclerAdListener;
import com.meishu.sdk.core.ad.recycler.RecyclerAdLoader;
import com.meishu.sdk.core.ad.recycler.RecyclerAdMediaListener;
import com.meishu.sdk.core.loader.AdPlatformError;
import com.meishu.sdk.core.utils.MsAdPatternType;
import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adid.IdProviderFactory;

import java.util.ArrayList;
import java.util.List;

public class SingleRecyclerActivity extends AppCompatActivity implements RecyclerAdListener {

    private final String TAG = getClass().getName();

    private RecyclerAdLoader adLoader;
    private RecyclerAdData adData1;
    private RecyclerAdData adData2;
    private View view1;
    private View view2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recycler);

        String pid  = getIntent().getStringExtra("alternativePlaceId");
        if (TextUtils.isEmpty(pid)) {
            pid = IdProviderFactory.getDefaultProvider().feedImageVertical();
        }

        adLoader = new RecyclerAdLoader(this, pid, 1, this);
        findViewById(R.id.button_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adData1 != null) {
                    adData1.destroy();
                    adData1 = null;
                }
                if (adData2 != null) {
                    adData2.destroy();
                    adData2 = null;
                }
                findViewById(R.id.button_show1).setEnabled(false);
                findViewById(R.id.button_show2).setEnabled(false);
//                adLoader.loadAd();
                adLoader.loadAd();
            }
        });
        findViewById(R.id.button_show1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAd(view1);
            }
        });
        findViewById(R.id.button_show2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAd(view2);
            }
        });
    }

    private View cache(RecyclerAdData adData) {
        if (adData == null) {
            return null;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.single_item, null);
        TextView title = view.findViewById(R.id.title);
        title.setText(adData.getTitle());
        ViewGroup itemRoot = view.findViewById(R.id.item_root);
        ViewGroup videoContainer = view.findViewById(R.id.video_container);
        ArrayList<View> views = new ArrayList<>();
        views.add(itemRoot);
//        adData.bindAdToView(SingleRecyclerActivity.this, container, views, new RecylcerAdInteractionListener() {
//            @Override
//            public void onAdClicked() {
//                Log.d(TAG, "onAdClicked: 广告被点击");
//            }
//        });
        if (adData.getAdPatternType() == MsAdPatternType.VIDEO) {
            adData.bindMediaView(videoContainer, new RecyclerAdMediaListener() {
                @Override
                public void onVideoLoaded() {}
                @Override
                public void onVideoStart() {}
                @Override
                public void onVideoPause() {}
                @Override
                public void onVideoCompleted() {}
                @Override
                public void onVideoError() {}
            });
        }
        return view;
    }

    private void showAd(View view) {
        ViewGroup container = findViewById(R.id.container);
        container.removeAllViews();
        container.addView(view);
    }

    @Override
    public void onAdLoaded(List<RecyclerAdData> recyclerAdData) {
        if (recyclerAdData != null && !recyclerAdData.isEmpty()) {
            if (adData1 == null) {
                adData1 = recyclerAdData.get(0);
                view1 = cache(adData1);
                findViewById(R.id.button_show1).setEnabled(true);
            } else {
                adData2 = recyclerAdData.get(0);
                view2 = cache(adData2);
                findViewById(R.id.button_show2).setEnabled(true);
            }
        }
    }

    @Override
    public void onAdError() {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
    }

    @Override
    public void onAdPlatformError(AdPlatformError e) {

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
