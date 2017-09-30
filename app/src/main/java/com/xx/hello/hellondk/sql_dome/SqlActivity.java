package com.xx.hello.hellondk.sql_dome;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xx.hello.hellondk.R;
import com.xx.hello.hellondk.phone.Invoke;
import com.xx.hello.hellondk.phone.InvokeHttp;

/**
 * Created by Hello on 2017/9/13.
 */

public class SqlActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kotlin);

        aaa();
        bbb();
        xxx();


        Button in = (Button) findViewById(R.id.sql_in);
        Button out = (Button) findViewById(R.id.sql_out);
        Button select = (Button) findViewById(R.id.sql_select);
        in.setOnClickListener(this);
        out.setOnClickListener(this);
        select.setOnClickListener(this);
        dbHelper = new SqlHelper(this);
    }

    private String bbb() {
        String a="ni";
        String b="hao";
        return a+b;
    }

    private void aaa() {
        int a = 1;
        int b = 2;
        int c = a + b;

    }

    private void xxx() {
        if (new InvokeHttp().hasPermission(this, Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE)) {
            new InvokeHttp().Test(this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS
                    , Manifest.permission.READ_PHONE_STATE}, 110);
        }
    }

    private SqlHelper dbHelper;

    @Override
    public void onClick(View v) {
        SQLiteDatabase writableDatabase;
        switch (v.getId()) {
            case R.id.sql_in:
                //22.962145,113.982667
                writableDatabase = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("start_address", "百德新街");
                values.put("end_address", "雪厂街");
                values.put("start_latlng", "112.33235542");
                values.put("end_latlng", "112.33235542");
                writableDatabase.insert("History", null, values);
                break;
            case R.id.sql_out:
                writableDatabase = dbHelper.getWritableDatabase();
                writableDatabase.delete("History", null, null);

                break;
            case R.id.sql_select:
                writableDatabase = dbHelper.getWritableDatabase();
                Cursor cursor = writableDatabase.query("History", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        Log.e(TAG, "start_address=" + cursor.getInt(cursor.getColumnIndex("id")));

                        Log.e(TAG, "start_address=" + cursor.getString(cursor.getColumnIndex("start_address")));
                        Log.e(TAG, "end_address=" + cursor.getString(cursor.getColumnIndex("end_address")));
                        Log.e(TAG, "start_latlng=" + cursor.getDouble(cursor.getColumnIndex("start_latlng")));
                        Log.e(TAG, "end_latlng=" + cursor.getDouble(cursor.getColumnIndex("end_latlng")));
                    } while (cursor.moveToNext());
                }
                break;
        }
    }

    private String TAG = "===";
    private int i = 0;
    private String a = "aaa";

    public String getTAG() {
        return a;
    }

//iput v0,p0,Lcom/xx/hello/hellondk/sqlActivity;->a:Ljava/lang/String;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 110) {
            if (grantResults[1] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "没有权限有些功能无法使用哦", Toast.LENGTH_SHORT).show();
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new InvokeHttp().Test(this);
            }
        }
    }
}
