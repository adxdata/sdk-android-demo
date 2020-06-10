package com.meishu.sdkdemo.adactivity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adactivity.draw.VideoFeedActivity;
import com.meishu.sdkdemo.adid.IdProviderFactory;

public class PrepareSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_splash);

        ((EditText) findViewById(R.id.alternativeSplashAdPlaceID)).setText(IdProviderFactory.getDefaultProvider().splash());
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.loadSplashAd && IdProviderFactory.getDefaultProvider().platformName().equals(IdProviderFactory.PLATFORM_BD)) {
                    Toast.makeText(PrepareSplashActivity.this, "百度不支持", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                intent.putExtra("id", v.getId());
                intent.putExtra("alternativePlaceId", ((EditText) findViewById(R.id.alternativeSplashAdPlaceID)).getText().toString().trim());
                startActivity(intent);
            }
        };
        findViewById(R.id.loadSplashAd).setOnClickListener(clickListener);
        findViewById(R.id.loadAndShowSplashAd).setOnClickListener(clickListener);
    }
}
