package com.didilink.sdkdemo.adactivity.draw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.didilink.sdkdemo.R;
import com.didilink.sdkdemo.adid.IdProviderFactory;

public class PrepareVideoFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_video_feed);

        ((EditText) findViewById(R.id.alternativeVideoFeedAdPlaceID)).setText(IdProviderFactory.getDefaultProvider().videoFeed());
        findViewById(R.id.loadVideoFeedAd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VideoFeedActivity.class);
                intent.putExtra("alternativePlaceId", ((EditText) findViewById(R.id.alternativeVideoFeedAdPlaceID)).getText().toString().trim());
                startActivity(intent);
            }
        });
    }
}
