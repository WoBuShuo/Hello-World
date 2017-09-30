package activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.xx.hello.hellondk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Hello on 2017/8/17.
 */

public class SplashActivity extends Activity implements View.OnClickListener {

    private final int returnMun = 6;
    private final int requestMun = 7;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == returnMun) {
                mTime--;
                if (mTime == 0) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    mHandler.removeCallbacksAndMessages(null);
                    return;
                }
                Log.e("===", "handleMessage: " + mTime);
                mReturnBtn.setText("跳过(" + mTime + "s)");
                mHandler.sendEmptyMessageDelayed(6, 1000);

            } else if (msg.what == requestMun) {
                mReturnBtn.setVisibility(View.VISIBLE);
                mAdText.setVisibility(View.VISIBLE);


                Glide.with(SplashActivity.this).load(mAdImageUrl)
                        .transition(new DrawableTransitionOptions().crossFade(500)).into(mImageView);
            }

        }
    };

    private Button mReturnBtn;
    private ImageView mImageView;
    private Button mAdText;
    private String mAdUrl;
    private String mAdImageUrl;
    private int mTime = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        mReturnBtn = (Button) findViewById(R.id.splash_return);
        mImageView = (ImageView) findViewById(R.id.splash_image);
        mAdText = (Button) findViewById(R.id.splash_ad);

        mReturnBtn.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        mHandler.sendEmptyMessageDelayed(returnMun, 1000);

        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url("http://10.0.3.2:8080/text/AdvertismentServlet").build();
        Request request = new Request.Builder().url("http://192.168.1.110:8080/text/AdvertismentServlet").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseData(response.body().string().trim());
            }
        });
    }

    private void responseData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String code = jsonObject.getString("code");
            if (!"200".equals(code)) {
                return;
            }
            mAdImageUrl = jsonObject.getString("adImageUrl");
            mAdUrl = jsonObject.getString("adUrl");
            mHandler.sendEmptyMessageDelayed(requestMun, 500);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splash_image:

                break;
            case R.id.splash_return:
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
                mHandler.removeCallbacksAndMessages(null);
                break;
        }
    }
}
