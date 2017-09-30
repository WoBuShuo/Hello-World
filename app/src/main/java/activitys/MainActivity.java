package activitys;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xx.hello.hellondk.R;
import com.xx.hello.hellondk.UpVersionService;
import com.xx.hello.hellondk.UpdateVersionDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "=============";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
                requestData();
        Button button = (Button) findViewById(R.id.up_data);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ListActivity.class);
                startActivity(intent);

            }
        });
    }

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    private void requestData() {
        String versionCode = null;
        String versionName = null;
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode + "";
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String postBody = ""
                + "version_code=" + versionCode + "\n"
                + "version_name=" + versionName + "\n";
        OkHttpClient client = new OkHttpClient();


        FormBody.Builder builder = new FormBody.Builder();
        builder.add("version_code",versionCode);
        Log.e(TAG, "requestData: "+versionCode );
        builder.add("version_name",versionName);
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url("http://192.168.1.110:8080/text/AServlet").post(formBody).build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onResponse: 请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                analysisJson(response.body().string().trim());
                Log.e(TAG, "onResponse: 访问成功");
            }
        });
    }

    private void analysisJson(String responseString) {
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            int code = jsonObject.getInt("code");
            if (code == 400) {
                Log.e(TAG, "analysisJson: " + "当前版本是最新的哦！");


            } else if (code == 200) {
                Log.e(TAG, "analysisJson: " + "有版本更新！");
            } else {
                Log.e(TAG, "analysisJson: " + "服务器错误！");
                return;
            }

            String newVersionUrl = jsonObject.getString("data");
            jsonObject = new JSONObject(newVersionUrl);
            final String upDataLog = jsonObject.getString("upDataLog");
            final String url = jsonObject.getString("url");
            final String versionSize = jsonObject.getString("versionSize");
            final String versionId = jsonObject.getString("versionId");

            Log.e(TAG, "analysisJson: 开始下载,请在下载完成后确认安装！");

            io.reactivex.Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<String> e) throws Exception {
//                    Log.e(TAG, "subscribe: " + Thread.currentThread());
//                    showUpDialog(versionId,versionSize,upDataLog,url);
                    e.onNext(url);
                    e.onNext(versionId);
                    e.onNext(versionSize);
                    e.onNext(upDataLog);
                }
            }).buffer(4,1).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<String>>(){

                @Override
                public void accept(List<String> strings) throws Exception {
                    Log.e(TAG, "输出: "+strings.size() );
                    for (String s:strings){
                        Log.e(TAG, "accept: "+s );
                    }
                    showUpDialog(versionId,versionSize,upDataLog,url);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showUpDialog(String versionId,String versionSize,String upDataLog,final String url) {
        new UpdateVersionDialog().showDialog(this, upDataLog, versionId, versionSize, new UpdateVersionDialog.ConfirmDialogListener() {
            @Override
            public void ok() {
                Toast.makeText(MainActivity.this, "开始下载,请在下载完成后确认安装！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, UpVersionService.class);
                intent.putExtra("url", url);
                startService(intent);
            }

            @Override
            public void cancel() {

            }
        });
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
