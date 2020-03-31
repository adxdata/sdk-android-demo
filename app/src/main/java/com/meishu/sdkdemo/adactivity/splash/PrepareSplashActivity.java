package com.meishu.sdkdemo.adactivity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adactivity.draw.VideoFeedActivity;
import com.meishu.sdkdemo.adid.IdProviderFactory;

public class PrepareSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_splash);

        ((EditText) findViewById(R.id.alternativeSplashAdPlaceID)).setText(IdProviderFactory.getDefaultProvider().splash());
        findViewById(R.id.loadSplashAd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                intent.putExtra("alternativePlaceId", ((EditText) findViewById(R.id.alternativeSplashAdPlaceID)).getText().toString().trim());
                startActivity(intent);
            }
        });
    }
}
