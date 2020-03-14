package com.meishu.sdkdemo.adactivity.draw;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.dingmouren.layoutmanagergroup.viewpager.OnViewPagerListener;
import com.dingmouren.layoutmanagergroup.viewpager.ViewPagerLayoutManager;
import com.meishu.sdk.core.ad.draw.DrawAdListener;
import com.meishu.sdk.core.ad.draw.DrawAdLoader;
import com.meishu.sdk.core.ad.draw.IDrawAd;
import com.meishu.sdk.core.utils.LogUtil;
import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adid.IdProviderFactory;
import com.meishu.sdkdemo.view.FullScreenVideoView;

import java.util.ArrayList;
import java.util.List;

public class VideoFeedActivity extends AppCompatActivity implements DrawAdListener {

    private final String TAG = getClass().getSimpleName();

    private RecyclerView recyclerView;
    private AdAdapter adAdapter;
    private ViewPagerLayoutManager layoutManager;
    private DrawAdLoader drawAdLoader;
    private int selectedPos = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_video_feed);

        initView();

        drawAdLoader = new DrawAdLoader(this, IdProviderFactory.getDefaultProvider().videoFeed(), this);
        drawAdLoader.loadAd();
    }

    private void initView() {
        recyclerView = findViewById(R.id.video_feed);
        layoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adAdapter = new AdAdapter();
        recyclerView.setAdapter(adAdapter);
        layoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                play();
            }
            @Override
            public void onPageRelease(boolean b, int i) {
            }
            @Override
            public void onPageSelected(int i, boolean b) {
                play();
                selectedPos = i;
                if (i % 5 == 0) {
                    drawAdLoader.loadAd();
                }
            }
            private void play() {
                View view = recyclerView.getChildAt(0);
                FrameLayout videoContainer = view.findViewById(R.id.layout_container);
                View video = videoContainer.getChildAt(0);
                if (video instanceof VideoView) {
                    ImageView imgThumb = view.findViewById(R.id.img_thumb);
                    imgThumb.setVisibility(View.GONE);
                    VideoView videoView = (VideoView) videoContainer.getChildAt(0);
                    videoView.start();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (drawAdLoader != null) {
            drawAdLoader.destroy();
        }
        if (layoutManager != null) {
            layoutManager.setOnViewPagerListener(null);
        }
    }

    @Override
    public void onAdLoaded(IDrawAd ad) {
        LogUtil.d(TAG, "onAdLoaded");
        ItemData data = new ItemData();
        data.ad = ad;
        data.type = ItemData.ITEM_TYPE_AD;
        adAdapter.addData(selectedPos + 1, data);
        adAdapter.notifyDataSetChanged();
        Toast.makeText(this, "广告已加载", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdError() {
        LogUtil.d(TAG, "onAdError");
    }

    @Override
    public void onAdExposure() {
        LogUtil.d(TAG, "onAdExposure");
    }

    @Override
    public void onAdClosed() {
        LogUtil.d(TAG, "onAdClosed");
    }

    class ItemData {

        public static final int ITEM_TYPE_DATA = 0;
        public static final int ITEM_TYPE_AD = 1;

        private String url;
        private int type;
        private int thumbResId;
        private IDrawAd ad;
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        private ImageView imgThumb;
        private FrameLayout container;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.img_thumb);
            container = itemView.findViewById(R.id.layout_container);
        }
    }

    class AdAdapter extends RecyclerView.Adapter<ItemHolder> {

        private final static int DATA_COUNT = 20;
        private List<ItemData> itemData;

        public AdAdapter() {
            itemData = new ArrayList<>(DATA_COUNT);
            for (int i = 0; i < DATA_COUNT; i++) {
                ItemData item = new ItemData();
                item.url = "android.resource://" + getPackageName() + "/" + R.raw.video12;
                item.thumbResId = R.mipmap.video12;
                item.type = ItemData.ITEM_TYPE_DATA;
                itemData.add(item);
            }
        }

        public void addData(ItemData data) {
            itemData.add(data);
        }

        public void addData(int pos, ItemData data) {
            itemData.add(pos, data);
        }

        public ItemData getData(int pos) {
            return itemData.get(pos);
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ad_video, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder viewHolder, int i) {
            ItemData item = itemData.get(i);
            View view = null;
            if (itemData.get(i).type == ItemData.ITEM_TYPE_DATA) {
                FullScreenVideoView videoView = new FullScreenVideoView(viewHolder.itemView.getContext());
                videoView.setZOrderOnTop(true);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                videoView.setLayoutParams(layoutParams);
                videoView.setVideoURI(Uri.parse(item.url));
                view = videoView;
            } else if (itemData.get(i).type == ItemData.ITEM_TYPE_AD) {
                view = item.ad.getAdView();
            }
            viewHolder.imgThumb.setImageResource(item.thumbResId);
            viewHolder.imgThumb.setVisibility(View.VISIBLE);
            viewHolder.container.removeAllViews();
            viewHolder.container.addView(view);
        }

        @Override
        public int getItemCount() {
            return itemData.size();
        }
    }
}
