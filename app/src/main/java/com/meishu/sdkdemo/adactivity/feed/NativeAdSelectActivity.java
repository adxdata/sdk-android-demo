package com.meishu.sdkdemo.adactivity.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.meishu.sdkdemo.adactivity.paster.PasterActivity;
import com.meishu.sdkdemo.R;

@Deprecated
public class NativeAdSelectActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_select_ad);
        findViewById(R.id.native_image_ad).setOnClickListener(this);
        findViewById(R.id.paster_ad).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.native_image_ad:
                intent.setClass(this, ImageNotInRecyclerActivity.class);
                startActivity(intent);
                break;
            case R.id.paster_ad:
                intent.setClass(this, PasterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
