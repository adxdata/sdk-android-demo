package com.meishu.sdkdemo.adactivity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;
import android.widget.Toast;

import com.meishu.sdk.core.utils.RequestUtil;
import com.meishu.sdkdemo.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeeplinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);

        String imei = RequestUtil.getImei(this);
        String androidId = RequestUtil.getAndroidId(this);
        String url = "http://ssp.1rtb.com/tracker?ssp_req_id=24da5758-e2ab-48c6-9d37-0efe8b686b9e&accept_id=99999&adv_id=9999999&media_id=999999&track=dp&event=succ";
        String from = "";
        if (getIntent().getData() != null) {
            from = getIntent().getData().getQueryParameter("from");
            from = from != null ? from : "";
        }
        TextView textParam = findViewById(R.id.text_param);
        textParam.setText("FROM: " + from);
        new OkHttpClient.Builder().build().newCall(
                new Request.Builder()
                        .url(HttpUrl.parse(url).newBuilder()
                                .addQueryParameter("strategy_dealid", from)
                                .addQueryParameter("device_imei", imei)
                                .addQueryParameter("device_adid", androidId)
                                .build())
                        .get()
                        .build())
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DeeplinkActivity.this.getApplicationContext(), "上报失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DeeplinkActivity.this.getApplicationContext(), "上报成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
}
