package com.meishu.sdkdemo.nativead;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.meishu.sdk.core.ad.recycler.RecyclerAdData;
import com.meishu.sdk.core.ad.recycler.RecyclerAdListener;
import com.meishu.sdk.core.ad.recycler.RecyclerAdLoader;
import com.meishu.sdk.core.ad.recycler.RecyclerAdMediaListener;
import com.meishu.sdk.core.ad.recycler.RecylcerAdInteractionListener;
import com.meishu.sdk.core.utils.MsAdPatternType;
import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adid.IdProviderFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TextAboveImageActivity extends AppCompatActivity implements RecyclerAdListener {
    private static final String TAG = "TextAboveImageActivity";
    public static final String EXTRA_PATTERN = "EXTRA_PATTERN";
    private RecyclerAdLoader recyclerAdLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_recycler_list);
        initView();

        String posId = IdProviderFactory.getDefaultProvider().feedImageVertical();
        int pattern = getIntent().getIntExtra(EXTRA_PATTERN, MsAdPatternType.LARGE_IMAGE);
        switch (pattern) {
            case MsAdPatternType.LARGE_IMAGE:
                posId = IdProviderFactory.getDefaultProvider().feedImageVertical();
                break;
            case MsAdPatternType.THREE_IMAGE:
                posId = IdProviderFactory.getDefaultProvider().feedThreeImgs();
                break;
        }
//        recyclerAdLoader = new RecyclerAdLoader(this, posId, 2,this, MsAdPatternType.IMAGE, 0, true);//信息流
        recyclerAdLoader = new RecyclerAdLoader(this, posId, 2,this, true);//信息流
        recyclerAdLoader.loadAd();
    }

    private CustomAdapter mAdapter;
    private AQuery mAQuery;
    private boolean mIsLoading = true;

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        List<NormalItem> list = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            list.add(new NormalItem("No." + i + " Init Data"));
        }
        mAdapter = new CustomAdapter(this, list);
        recyclerView.setAdapter(mAdapter);
        mAQuery = new AQuery(this);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!mIsLoading && newState == SCROLL_STATE_IDLE && !recyclerView.canScrollVertically(1)) {
                    mIsLoading = true;
                    TextAboveImageActivity.this.recyclerAdLoader.loadAd();
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private List<RecyclerAdData> loadedAdDatas = new ArrayList<>();
    private static final int MSG_REFRESH_LIST = 1;
    private H mHandler = new H();

    @Override
    public void onAdLoaded(List<RecyclerAdData> adDatas) {
        this.mIsLoading = false;
        loadedAdDatas.addAll(adDatas);
        Message msg = mHandler.obtainMessage(MSG_REFRESH_LIST, adDatas);
        mHandler.sendMessage(msg);
    }

    @Override
    public void onAdExposure() {
        Log.d(TAG, "onAdExposure: 广告曝光");
    }

    @Override
    public void onAdClosed() {
        Log.d(TAG, "onAdClosed: 广告被关闭");
    }

    @Override
    public void onAdError() {
        Log.d(TAG, "onAdError: 没有加载到广告");
        this.mIsLoading = false;
    }

    class NormalItem {
        private String mTitle;

        public NormalItem(int index) {
            this("No." + index + " Normal Data");
        }

        public NormalItem(String title) {
            this.mTitle = title;
        }

        public String getTitle() {
            return mTitle;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.loadedAdDatas != null) {
            for (RecyclerAdData adData : loadedAdDatas) {
                adData.destroy();
            }
        }
    }

    private static final int TYPE_AD = 1;
    private static final int TYPE_DATA = 0;

    class CustomAdapter extends RecyclerView.Adapter<CustomHolder> {

        private List<Object> mData;
        private Context mContext;
        private TreeSet mADSet = new TreeSet();

        public CustomAdapter(Context context, List list) {
            mContext = context;
            mData = list;
        }

        public void addNormalItem(NormalItem item) {
            mData.add(item);
        }

        public void addAdToPosition(RecyclerAdData recyclerAdData, int position) {
            if (position >= 0 && position < mData.size()) {
                mData.add(position, recyclerAdData);
                mADSet.add(position);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return mADSet.contains(position) ? TYPE_AD : TYPE_DATA;
        }

        @Override
        public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case TYPE_AD:
                    view = LayoutInflater.from(mContext).inflate(R.layout.item_ad_unified_large_img_or_video, null);
                    break;

                case TYPE_DATA:
                    view = LayoutInflater.from(mContext).inflate(R.layout.item_data, null);
                    break;

                default:
                    view = null;
            }
            return new CustomHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(CustomHolder holder, int position) {
            switch (getItemViewType(position)) {
                case TYPE_AD:
                    initItemView(position, holder);
                    break;
                case TYPE_DATA:
                    holder.title.setText(((NormalItem) mData.get(position))
                            .getTitle());
                    break;
            }
        }

        private void initItemView(int position, final CustomHolder holder) {
            final RecyclerAdData ad = (RecyclerAdData) mData.get(position);
            AQuery logoAQ = holder.logoAQ;
            holder.title_top.setText(ad.getTitle());
            holder.desc_top.setText(ad.getDesc());
            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(holder.container);
            // 视频广告
            if (ad.getAdPatternType() == MsAdPatternType.VIDEO) {
                showVideo(holder);
                showTop(logoAQ);
            } else if (ad.getAdPatternType() == MsAdPatternType.IMAGE
                    || ad.getAdPatternType() == MsAdPatternType.LARGE_IMAGE
                    || ad.getAdPatternType() == MsAdPatternType.SMALL_IMAGE) {
                showPoster(holder);
                logoAQ.id(R.id.img_poster).image(ad.getImgUrls()[0], false, true, 0, 0,
                        new BitmapAjaxCallback() {
                            @Override
                            protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                                if (iv.getVisibility() == View.VISIBLE) {
                                    iv.setImageBitmap(bm);
                                }
                            }
                        });
                showTop(logoAQ);
            } else if (ad.getAdPatternType() == MsAdPatternType.THREE_IMAGE) {
                String[] imgs = ad.getImgUrls();
                if (imgs != null && imgs.length == 1) {
                    showPoster(holder);
                    logoAQ.id(R.id.img_poster).image(ad.getImgUrls()[0], false, true, 0, 0,
                            new BitmapAjaxCallback() {
                                @Override
                                protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                                    if (iv.getVisibility() == View.VISIBLE) {
                                        iv.setImageBitmap(bm);
                                    }
                                }
                            });
                    showTop(logoAQ);
                } else if (imgs != null && imgs.length > 1) {
                    hideAll(holder);
                    logoAQ.id(R.id.ad_info_container_bottom_text_title).text(ad.getTitle());
                    logoAQ.id(R.id.ad_info_container_bottom_text_desc).text(ad.getDesc());
                    if (imgs.length > 0 && !TextUtils.isEmpty(imgs[0])) {
                        holder.img1.setVisibility(View.VISIBLE);
                        logoAQ.id(R.id.img_1).image(ad.getImgUrls()[0], false, true, 0, 0,
                                new BitmapAjaxCallback() {
                                    @Override
                                    protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                                        if (iv.getVisibility() == View.VISIBLE) {
                                            iv.setImageBitmap(bm);
                                        }
                                    }
                                });
                        showBottom(logoAQ);
                    }
                    if (imgs.length > 1 && !TextUtils.isEmpty(imgs[1])) {
                        holder.img2.setVisibility(View.VISIBLE);
                        logoAQ.id(R.id.img_2).image(ad.getImgUrls()[1], false, true, 0, 0,
                                new BitmapAjaxCallback() {
                                    @Override
                                    protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                                        if (iv.getVisibility() == View.VISIBLE) {
                                            iv.setImageBitmap(bm);
                                        }
                                    }
                                });
                    }
                    if (imgs.length > 2 && !TextUtils.isEmpty(imgs[2])) {
                        holder.img3.setVisibility(View.VISIBLE);
                        logoAQ.id(R.id.img_3).image(ad.getImgUrls()[2], false, true, 0, 0,
                                new BitmapAjaxCallback() {
                                    @Override
                                    protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                                        if (iv.getVisibility() == View.VISIBLE) {
                                            iv.setImageBitmap(bm);
                                        }
                                    }
                                });
                    }
                } else {

                }
            }
            ad.bindAdToView(holder.container.getContext(), holder.container,
                    clickableViews, new RecylcerAdInteractionListener() {
                        @Override
                        public void onAdClicked() {
                            Log.d(TAG, "onAdClicked: 广告被点击");
                        }

                    });

            setAdListener(holder, ad);

        }

        private void setAdListener(final CustomHolder holder, final RecyclerAdData ad) {
            // 视频广告
            if (ad.getAdPatternType() == MsAdPatternType.VIDEO) {
                ad.bindMediaView(holder.mediaView, new RecyclerAdMediaListener() {

                    @Override
                    public void onVideoLoaded() {
                        Log.d(TAG, "onVideoLoaded: 视频加载完成");
                    }

                    @Override
                    public void onVideoStart() {
                        Log.d(TAG, "onVideoStart: 视频开始播放");
                    }

                    @Override
                    public void onVideoPause() {
                        Log.d(TAG, "onVideoPause: 视频暂停");
                    }

                    @Override
                    public void onVideoCompleted() {
                        Log.d(TAG, "onVideoCompleted: 视频结束");
                        holder.replay.setEnabled(true);
                    }

                    @Override
                    public void onVideoError() {
                        Log.e(TAG, "onVideoError: ", new Exception("视频出错"));
                    }
                });
                holder.replay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.replay();
                        holder.replay.setEnabled(false);
                    }
                });
                holder.mute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.mute();
                    }
                });
                holder.unmute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.unmute();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private void showVideo(CustomHolder holder) {
        holder.poster.setVisibility(View.INVISIBLE);
        holder.img1.setVisibility(View.GONE);
        holder.img2.setVisibility(View.GONE);
        holder.img3.setVisibility(View.GONE);
        holder.mediaView.setVisibility(View.VISIBLE);
        holder.layoutVideo.setVisibility(View.VISIBLE);
    }

    private void showPoster(CustomHolder holder) {
        holder.poster.setVisibility(View.VISIBLE);
        holder.img1.setVisibility(View.GONE);
        holder.img2.setVisibility(View.GONE);
        holder.img3.setVisibility(View.GONE);
        holder.mediaView.setVisibility(View.INVISIBLE);
    }

    private void showTop(AQuery logoAQ) {
        logoAQ.id(R.id.ad_info_container_top).visibility(VISIBLE);
        logoAQ.id(R.id.ad_info_container_top_text_title).visibility(VISIBLE);
        logoAQ.id(R.id.ad_info_container_top_text_desc).visibility(VISIBLE);
        logoAQ.id(R.id.ad_info_container_bottom).visibility(GONE);
        logoAQ.id(R.id.ad_info_container_bottom_text_title).visibility(GONE);
        logoAQ.id(R.id.ad_info_container_bottom_text_desc).visibility(GONE);
    }

    private void showBottom(AQuery logoAQ) {
        logoAQ.id(R.id.ad_info_container_top).visibility(GONE);
        logoAQ.id(R.id.ad_info_container_top_text_title).visibility(GONE);
        logoAQ.id(R.id.ad_info_container_top_text_desc).visibility(GONE);
        logoAQ.id(R.id.ad_info_container_bottom).visibility(VISIBLE);
        logoAQ.id(R.id.ad_info_container_bottom_text_title).visibility(VISIBLE);
        logoAQ.id(R.id.ad_info_container_bottom_text_desc).visibility(VISIBLE);
    }

    private void hideAll(CustomHolder holder) {
        holder.poster.setVisibility(View.INVISIBLE);
        holder.img1.setVisibility(View.GONE);
        holder.img2.setVisibility(View.GONE);
        holder.img3.setVisibility(View.GONE);
        holder.mediaView.setVisibility(View.INVISIBLE);
    }

    class CustomHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ViewGroup mediaView;
        public ViewGroup topAdInfoContainer;
        public ViewGroup bottomAdInfoContainer;
        public TextView title_top;
        public TextView desc_top;
        public ImageView poster;
        public ImageView img1;
        public ImageView img2;
        public ImageView img3;
        public ViewGroup container;
        public ViewGroup layoutVideo;
        public Button replay;
        public Button mute;
        public Button unmute;
        public AQuery logoAQ;

        public CustomHolder(View itemView, int adType) {
            super(itemView);
            switch (adType) {
                case TYPE_AD:
                    mediaView = itemView.findViewById(R.id.api_media_view);
                    topAdInfoContainer = itemView.findViewById(R.id.ad_info_container_top);
                    bottomAdInfoContainer = itemView.findViewById(R.id.ad_info_container_bottom);
                    poster = itemView.findViewById(R.id.img_poster);
                    img1 = itemView.findViewById(R.id.img_1);
                    img2 = itemView.findViewById(R.id.img_2);
                    img3 = itemView.findViewById(R.id.img_3);
                    title_top = itemView.findViewById(R.id.ad_info_container_top_text_title);
                    desc_top = itemView.findViewById(R.id.ad_info_container_top_text_desc);
                    container = itemView.findViewById(R.id.native_ad_container);
                    replay = itemView.findViewById(R.id.button_replay);
                    mute = itemView.findViewById(R.id.button_mute);
                    unmute = itemView.findViewById(R.id.button_unmute);
                    layoutVideo = itemView.findViewById(R.id.layout_video);
                    logoAQ = new AQuery(itemView);

                case TYPE_DATA:
                    title = itemView.findViewById(R.id.title);
                    break;

            }
        }
    }

    private static final int ITEM_COUNT = 30;
    private static final int FIRST_AD_POSITION = 5;
    private static final int AD_DISTANCE = 10;

    private class H extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_LIST:

                    int count = mAdapter.getItemCount();
                    for (int i = 0; i < ITEM_COUNT; i++) {
                        mAdapter.addNormalItem(new NormalItem(count + i));
                    }

                    List<RecyclerAdData> ads = (List<RecyclerAdData>) msg.obj;
                    if (ads != null && ads.size() > 0 && mAdapter != null) {
                        for (int i = 0; i < ads.size(); i++) {
                            mAdapter.addAdToPosition(ads.get(i), count + i * AD_DISTANCE + FIRST_AD_POSITION);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    break;

                default:
            }
        }
    }
}
