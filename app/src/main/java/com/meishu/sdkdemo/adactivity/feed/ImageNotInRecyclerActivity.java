package com.meishu.sdkdemo.adactivity.feed;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.meishu.sdk.core.ad.image.ImageAdData;
import com.meishu.sdk.core.ad.image.ImageAdInteractionListener;
import com.meishu.sdk.core.ad.image.ImageAdListener;
import com.meishu.sdk.core.ad.image.ImageAdLoader;
import com.meishu.sdk.core.loader.AdPlatformError;
import com.meishu.sdk.core.utils.MsAdPatternType;
import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adid.IdProviderFactory;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ImageNotInRecyclerActivity extends AppCompatActivity implements ImageAdListener {
    private static final String TAG = "ImageNotInRecyclerActiv";
    private AQuery mAQuery;

    private RelativeLayout mContainer;


    private Button mDownloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad);

        initView();
        ImageAdLoader nativeAD = new ImageAdLoader(this, IdProviderFactory.getDefaultProvider().image(), this);//文字类广告，文字内容取信息流广告中的title
        nativeAD.loadData();
    }

    private void initView() {
        mDownloadButton = findViewById(R.id.btn_download);
        mContainer = findViewById(R.id.native_ad_container);
        mAQuery = new AQuery(findViewById(R.id.root));

    }

    private List<ImageAdData> loadedAdDatas = new ArrayList<>();

    @Override
    public void onAdLoaded(List<ImageAdData> adDatas) {
        if (adDatas != null && adDatas.size() > 0) {
            loadedAdDatas.addAll(adDatas);
            initAd(adDatas.get(0));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (ImageAdData adData : loadedAdDatas) {//不再使用广告时，必须回收资源
            adData.destroy();
        }
    }

    @Override
    public void onAdExposure() {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
    }

    @Override
    public void onAdClosed() {

    }

    @Override
    public void onAdPlatformError(AdPlatformError error) {

    }

    @Override
    public void onAdError() {
        Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
    }

    private void initAd(final ImageAdData ad) {

        renderAdUi(ad);

        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(mDownloadButton);
        ad.bindAdToView(this, mContainer, clickableViews, new ImageAdInteractionListener() {

            @Override
            public void onAdClicked() {
                Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            }

        });

    }

    private void renderAdUi(ImageAdData ad) {
        int patternType = ad.getAdPatternType();
        if (!TextUtils.isEmpty(ad.getTitle())) {
            mAQuery.id(R.id.text_title).text(ad.getTitle());
        }
        if (!TextUtils.isEmpty(ad.getDesc())) {
            mAQuery.id(R.id.text_desc).text(ad.getDesc());
        }
        if (!TextUtils.isEmpty(ad.getIconUrl())) {
            mAQuery.id(R.id.img_logo).image(ad.getIconUrl(), false, true);
        }
        String buttonText;
        if (ad.getInteractionType() == 0) {
            buttonText = "浏览";
        } else {
            buttonText = "下载";
        }
        mAQuery.id(R.id.btn_download).text(buttonText);
        mAQuery.id(R.id.ad_type_mark).text("图片类型");

        String[] imgList = ad.getImgList();
        if (patternType == MsAdPatternType.IMAGE || patternType == MsAdPatternType.SMALL_IMAGE || patternType == MsAdPatternType.LARGE_IMAGE) {
            showPoster();
            mAQuery.id(R.id.img_poster).image(imgList[0], false, true, 0, 0,
                    new BitmapAjaxCallback() {
                        @Override
                        protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                            if (iv.getVisibility() == View.VISIBLE) {
                                iv.setImageBitmap(bm);
                            }
                        }
                    });
        } else if (patternType == MsAdPatternType.THREE_IMAGE) {
            hideAll();
            if (!TextUtils.isEmpty(imgList[0])) {
                mAQuery.id(R.id.img_1).getView().setVisibility(View.VISIBLE);
                mAQuery.id(R.id.img_1).image(imgList[0], false, true, 0, 0,
                        new BitmapAjaxCallback() {
                            @Override
                            protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                                if (iv.getVisibility() == View.VISIBLE) {
                                    iv.setImageBitmap(bm);
                                }
                            }
                        });

            }
            if (!TextUtils.isEmpty(imgList[1])) {
                mAQuery.id(R.id.img_2).getView().setVisibility(View.VISIBLE);
                mAQuery.id(R.id.img_2).image(imgList[1], false, true, 0, 0,
                        new BitmapAjaxCallback() {
                            @Override
                            protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                                if (iv.getVisibility() == View.VISIBLE) {
                                    iv.setImageBitmap(bm);
                                }
                            }
                        });
            }
            if (imgList.length > 2 && !TextUtils.isEmpty(imgList[2])) {
                mAQuery.id(R.id.img_3).getView().setVisibility(View.VISIBLE);
                mAQuery.id(R.id.img_3).image(imgList[2], false, true, 0, 0,
                        new BitmapAjaxCallback() {
                            @Override
                            protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                                if (iv.getVisibility() == View.VISIBLE) {
                                    iv.setImageBitmap(bm);
                                }
                            }
                        });
            }
        } else if (patternType == MsAdPatternType.VIDEO) {
            mAQuery.id(R.id.ad_type_mark).text("视频类型，不展示");
        } else {
            mAQuery.id(R.id.ad_type_mark).text("不支持的类型，不展示");
        }
    }

    private void showPoster() {
        mAQuery.id(R.id.img_poster).getView().setVisibility(View.VISIBLE);
        mAQuery.id(R.id.img_1).getView().setVisibility(View.GONE);
        mAQuery.id(R.id.img_2).getView().setVisibility(View.GONE);
        mAQuery.id(R.id.img_3).getView().setVisibility(View.GONE);
    }

    private void hideAll() {
        mAQuery.id(R.id.img_poster).getView().setVisibility(View.GONE);
        mAQuery.id(R.id.img_1).getView().setVisibility(View.GONE);
        mAQuery.id(R.id.img_2).getView().setVisibility(View.GONE);
        mAQuery.id(R.id.img_3).getView().setVisibility(View.GONE);
    }
}
