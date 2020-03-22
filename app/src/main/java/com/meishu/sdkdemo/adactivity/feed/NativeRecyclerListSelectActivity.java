package com.meishu.sdkdemo.adactivity.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.meishu.sdk.core.utils.MsAdPatternType;
import com.meishu.sdkdemo.R;
import com.meishu.sdkdemo.adid.IdProviderFactory;

public class NativeRecyclerListSelectActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_recycler_list_select);
        findViewById(R.id.img_text_button).setOnClickListener(this);
        findViewById(R.id.img_text).setOnClickListener(this);
        findViewById(R.id.large_img_or_video).setOnClickListener(this);
        findViewById(R.id.three_img).setOnClickListener(this);
        findViewById(R.id.pre_render).setOnClickListener(this);
        findViewById(R.id.single).setOnClickListener(this);

        ((EditText) findViewById(R.id.alternativeImageTextButtonAdPlaceID)).setText(IdProviderFactory.getDefaultProvider().feedImageHorizon());
        ((EditText) findViewById(R.id.alternativeImageTextAdPlaceID)).setText(IdProviderFactory.getDefaultProvider().feedImageHorizon());
        ((EditText) findViewById(R.id.alternativeLargeImageVideoAdPlaceID)).setText(IdProviderFactory.getDefaultProvider().feedImageVertical());
        ((EditText) findViewById(R.id.alternativeThreeImageAdPlaceID)).setText(IdProviderFactory.getDefaultProvider().feedThreeImgs());
        ((EditText) findViewById(R.id.alternativePreRenderAdPlaceID)).setText(IdProviderFactory.getDefaultProvider().feedPreRender());
        ((EditText) findViewById(R.id.alternativeSingleAdPlaceID)).setText(IdProviderFactory.getDefaultProvider().feedImageVertical());
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        String name = IdProviderFactory.getDefaultProvider().platformName();
        switch (v.getId()) {
            case R.id.img_text_button:
                intent = new Intent(this, ImageTextButtonActivity.class);
                intent.putExtra("alternativePlaceId", ((EditText) findViewById(R.id.alternativeImageTextButtonAdPlaceID)).getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.img_text:
                intent = new Intent(this, ImageTextActivity.class);
                intent.putExtra("alternativePlaceId", ((EditText) findViewById(R.id.alternativeImageTextAdPlaceID)).getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.large_img_or_video:
                intent = new Intent(this, TextAboveImageActivity.class);
                intent.putExtra(TextAboveImageActivity.EXTRA_PATTERN, MsAdPatternType.LARGE_IMAGE);
                intent.putExtra("alternativePlaceId", ((EditText) findViewById(R.id.alternativeLargeImageVideoAdPlaceID)).getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.three_img:
                intent = new Intent(this, TextAboveImageActivity.class);
                intent.putExtra(TextAboveImageActivity.EXTRA_PATTERN, MsAdPatternType.THREE_IMAGE);
                intent.putExtra("alternativePlaceId", ((EditText) findViewById(R.id.alternativeThreeImageAdPlaceID)).getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.pre_render:
                if (IdProviderFactory.PLATFORM_MS.equals(name)) {
                    Toast.makeText(this, "此类广告目前不支持，请更改广告提供商", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(this, PreRenderActivity.class);
                intent.putExtra("alternativePlaceId", ((EditText) findViewById(R.id.alternativePreRenderAdPlaceID)).getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.single:
                if (!IdProviderFactory.PLATFORM_MS.equals(name)) {
                    Toast.makeText(this, "此类广告目前不支持，请更改广告提供商", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(this, SingleRecyclerActivity.class);
                intent.putExtra("alternativePlaceId", ((EditText) findViewById(R.id.alternativeSingleAdPlaceID)).getText().toString().trim());
                startActivity(intent);
        }
    }
}
