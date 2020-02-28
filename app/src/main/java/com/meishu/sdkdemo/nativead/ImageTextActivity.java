package com.meishu.sdkdemo.nativead;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.meishu.sdk.core.ad.recycler.RecyclerAdData;
import com.meishu.sdk.core.ad.recycler.RecylcerAdInteractionListener;
import com.meishu.sdk.core.ad.recycler.RecyclerAdListener;
import com.meishu.sdk.core.ad.recycler.RecyclerAdLoader;
import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adid.IdProviderFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class ImageTextActivity extends AppCompatActivity implements RecyclerAdListener {
    private static final String TAG = "ImageTextActivity";
    private RecyclerAdLoader recyclerAdLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_recycler_list);
        initView();
        recyclerAdLoader = new RecyclerAdLoader(this, IdProviderFactory.getDefaultProvider().feedImageHorizon(), 2,this);//信息流
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
                    ImageTextActivity.this.recyclerAdLoader.loadAd();
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
                    view = LayoutInflater.from(mContext).inflate(R.layout.item_ad_unified_img_text, null);
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
            String iconUrl = null;
            if (!TextUtils.isEmpty(ad.getIconUrl())) {
                iconUrl = ad.getIconUrl();
            } else if (ad.getImgUrls() != null && ad.getImgUrls().length > 0) {
                iconUrl = ad.getImgUrls()[0];
            }
            if (!TextUtils.isEmpty(iconUrl)) {
                logoAQ.id(R.id.small_img).image(iconUrl, false, true);
            }
            holder.name.setText("title: " + ad.getTitle());
            holder.content.setText("content: " + ad.getContent());
            holder.actionText.setText("action_text: " + ad.getActionText());
            holder.appName.setText("app_name: " + ad.getAppName());
            holder.packageName.setText("package_name: " + ad.getPackageName());
            holder.from.setText("from: " + ad.getFrom());
            holder.fromLogo.setText("from_logo: " + ad.getFromLogo());
            holder.iconTitle.setText("icon_title: " + ad.getIconTitle());
            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(holder.container);
            ad.bindAdToView(ImageTextActivity.this, holder.container,
                    clickableViews, new RecylcerAdInteractionListener() {
                        @Override
                        public void onAdClicked() {
                            Log.d(TAG, "onAdClicked: 广告被点击");
                        }

                    });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    class CustomHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView name;
        public TextView desc;
        public TextView content;
        public TextView actionText;
        public TextView appName;
        public TextView packageName;
        public TextView from;
        public TextView fromLogo;
        public TextView iconTitle;
        public ImageView logo;
        public ViewGroup container;
        public AQuery logoAQ;

        public CustomHolder(View itemView, int adType) {
            super(itemView);
            switch (adType) {
                case TYPE_AD:
                    logo = itemView.findViewById(R.id.small_img);
                    name = itemView.findViewById(R.id.text_title);
                    desc = itemView.findViewById(R.id.text_desc);
                    content = itemView.findViewById(R.id.text_content);
                    actionText = itemView.findViewById(R.id.text_action_text);
                    appName = itemView.findViewById(R.id.text_app_name);
                    packageName = itemView.findViewById(R.id.text_package_name);
                    from = itemView.findViewById(R.id.text_from);
                    fromLogo = itemView.findViewById(R.id.text_from_logo);
                    iconTitle = itemView.findViewById(R.id.text_icon_title);
                    container = itemView.findViewById(R.id.native_ad_container);
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
