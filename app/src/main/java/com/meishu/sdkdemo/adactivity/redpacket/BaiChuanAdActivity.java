package com.meishu.sdkdemo.adactivity.redpacket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ali.auth.third.core.exception.AlibabaSDKException;
import com.meishu.sdk.ecommerce.BaichuanSdk;
import com.meishu.sdkdemo.R;

public class BaiChuanAdActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baichuan_ad);
        initView();
    }

    private void initView() {
        findViewById(R.id.getBaichuanAd).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.getBaichuanAd){
            getAd();
        }
    }

    private void getAd() {
        BaichuanSdk.getInstance().getAd(this);
    }
}
