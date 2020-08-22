package com.meishu.sdkdemo.adactivity.feed;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.meishu.sdk.core.ad.media.NativeMediaAd;
import com.meishu.sdk.core.ad.media.NativeMediaAdData;
import com.meishu.sdk.core.ad.media.NativeMediaAdListener;
import com.meishu.sdk.core.ad.media.NativeMediaAdMediaListener;
import com.meishu.sdk.core.utils.MsAdPatternType;
import com.meishu.sdk.platform.gdt.GDTConstants;
import com.meishu.sdkdemo.R;

import java.util.List;

@Deprecated
public class NativeVideoActivity extends AppCompatActivity {
    private static final String TAG = "NativeVedioActivity";

    private ViewGroup mMediaView;
    private ImageView mImagePoster;
    private ViewGroup mADInfoContainer;
    private Button mDownloadButton;
    private TextView mTextCountDown;
    private TextView textLoadResult;
    private AQuery mAQuery;
    private NativeMediaAd mADManager;
    private NativeMediaAdData mMediaAdData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_video);
        initView();
        initNativeVideoAD();
        loadAD();
    }

    private void initView() {
        mMediaView = findViewById(R.id.media_video);
        mImagePoster = findViewById(R.id.img_poster);
        Log.d(TAG, "initView: " + mImagePoster);
        mADInfoContainer = this.findViewById(R.id.ad_info_container_top);
        mDownloadButton = mADInfoContainer.findViewById(R.id.btn_download);
        mTextCountDown = findViewById(R.id.text_count_down);
        textLoadResult = findViewById(R.id.text_load_result);
        mAQuery = new AQuery(findViewById(R.id.root));
    }

    private void initNativeVideoAD() {
        NativeMediaAdListener listener = new NativeMediaAdListener() {

            @Override
            public void onAdLoaded(List<NativeMediaAdData> mediaAdDatas) {
                if (mediaAdDatas != null && mediaAdDatas.size() > 0) {
                    mMediaAdData = mediaAdDatas.get(0);
                    initADUI();
                    if (mMediaAdData.getAdPatternType() == MsAdPatternType.VIDEO) {
                        mMediaAdData.preLoadVideo();
                        textLoadResult.setText("getAdPatternType() == MsAdPatternType.VIDEO：" + "这是一条视频广告");
                    } else if (mMediaAdData.getAdPatternType() == MsAdPatternType.IMAGE
                            || mMediaAdData.getAdPatternType() == MsAdPatternType.LARGE_IMAGE
                            || mMediaAdData.getAdPatternType() == MsAdPatternType.SMALL_IMAGE) {
                        /* 如果该条原生广告只是一个普通图文的广告，不带视频素材，那么渲染普通的UI即可。 */
                        textLoadResult.setText("getAdPatternType() == MsAdPatternType.IMAGE：" + "这是一条两图两文广告");
                    } else if (mMediaAdData.getAdPatternType() == MsAdPatternType.THREE_IMAGE) {
                        textLoadResult.setText("getAdPatternType() == MsAdPatternType.IMAGE：" + "这是一条三小图广告");
                    }
                }
            }

            @Override
            public void onAdError() {

            }

            @Override
            public void onVideoLoaded(NativeMediaAdData mediaAdData) {
                Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                bindMediaView();
            }

            @Override
            public void onAdClicked(NativeMediaAdData mediaAdData) {
                Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            }

            @Override
            public void onAdExposure() {
                Log.d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            }

            @Override
            public void onAdClosed() {

            }
        };

        // TODO 使用 loader
        mADManager = new NativeMediaAd(this, GDTConstants.APPID, GDTConstants.NativeVideoPosID, listener);
    }

    private void loadAD() {
        if (mADManager != null) {
            try {
                mADManager.loadAD();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "加载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initADUI() {
        int patternType = mMediaAdData.getAdPatternType();
        if (patternType == MsAdPatternType.IMAGE
                || patternType == MsAdPatternType.SMALL_IMAGE
                || patternType == MsAdPatternType.LARGE_IMAGE
                || patternType == MsAdPatternType.VIDEO) {
            mAQuery.id(R.id.img_logo).image(mMediaAdData.getIconUrl(), false, true);
            mAQuery.id(R.id.img_poster).image(mMediaAdData.getImgUrl(), false, true, 0, 0, new BitmapAjaxCallback() {
                @Override
                protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                    // AQuery框架有一个问题，就是即使在图片加载完成之前将ImageView设置为了View.GONE，在图片加载完成后，这个ImageView会被重新设置为VIEW.VISIBLE。
                    // 所以在这里需要判断一下，如果已经把ImageView设置为隐藏，开始播放视频了，就不要再显示广告的大图。否则将影响到sdk的曝光上报，无法产生收益。
                    // 开发者在用其他的图片加载框架时，也应该注意检查下是否有这个问题。
                    if (iv.getVisibility() == View.VISIBLE) {
                        iv.setImageBitmap(bm);
                    }
                }
            });
            mAQuery.id(R.id.text_title).text(mMediaAdData.getTitle());
            mAQuery.id(R.id.text_desc).text(mMediaAdData.getDesc());
        } else if (patternType == MsAdPatternType.THREE_IMAGE && mMediaAdData.getImgList() != null) {
            mAQuery.id(R.id.img_1).image(mMediaAdData.getImgList().get(0), false, true);
            mAQuery.id(R.id.img_2).image(mMediaAdData.getImgList().get(1), false, true);
            mAQuery.id(R.id.img_3).image(mMediaAdData.getImgList().get(2), false, true);
            mAQuery.id(R.id.native_3img_title).text(mMediaAdData.getTitle());
            mAQuery.id(R.id.native_3img_desc).text(mMediaAdData.getDesc());
        }
        /**
         * 注意：在渲染时，必须先调用onExposured接口曝光广告，否则点击接口onClicked将无效
         */
        mMediaAdData.onExposured(mADInfoContainer);
        mDownloadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mMediaAdData.onClicked(view);
            }
        });
    }

    /**
     * 将广告实例和MediaView绑定，播放视频。
     * <p>
     * 注意：播放视频前需要将广告的大图隐藏，将MediaView显示出来，否则视频将无法播放，也无法上报视频曝光，无法产生计费。
     */
    private void bindMediaView() {
        if (mMediaAdData.getAdPatternType() == MsAdPatternType.VIDEO) {

            mImagePoster.setVisibility(View.GONE);
            mMediaView.setVisibility(View.VISIBLE);

            /**
             * bindView(MediaView view, boolean useDefaultController):
             *    - useDefaultController: false，不会调用广点通的默认视频控制条
             *    - useDefaultController: true，使用SDK内置的播放器控制条，此时开发者需要把demo下的res文件夹里面的图片拷贝到自己项目的res文件夹去
             *
             * 在这里绑定MediaView后，SDK会根据视频素材的宽高比例，重新给MediaView设置新的宽高
             */
            mMediaAdData.bindView(mMediaView, true);
            mMediaAdData.play();

            /** 设置视频播放过程中的监听器 */
            mMediaAdData.setMediaListener(new NativeMediaAdMediaListener() {
                @Override
                public void onVideoReady(long var1) {
                    Log.d(TAG, "onVideoReady: 视频已就绪");
                }

                @Override
                public void onVideoStart() {
                    mTextCountDown.setVisibility(View.VISIBLE);
                }

                @Override
                public void onVideoPause() {
                    mTextCountDown.setVisibility(View.GONE);
                }

                @Override
                public void onVideoComplete() {
                    mTextCountDown.setVisibility(View.GONE);
                }

                @Override
                public void onReplayButtonClicked() {
                    Log.d(TAG, "onReplayButtonClicked: 视频重新播放");
                }

                @Override
                public void onADButtonClicked() {
                    Log.d(TAG, "onADButtonClicked: 广告被点击");
                }

                @Override
                public void onFullScreenChanged(boolean isFullScreen) {
                    Log.d(TAG, "onFullScreenChanged: 是否全屏：" + isFullScreen);
                }
            });
        }
    }
}
