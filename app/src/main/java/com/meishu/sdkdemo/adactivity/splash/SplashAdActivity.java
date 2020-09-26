package com.meishu.sdkdemo.adactivity.splash;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.meishu.sdkdemo.R;

public class SplashAdActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash_ad);
        findViewById(R.id.openSplashAD).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.openSplashAD:
                intent.setClass(this,SplashActivity.class);
                startActivity(intent);
                break;
        }
    }
}
