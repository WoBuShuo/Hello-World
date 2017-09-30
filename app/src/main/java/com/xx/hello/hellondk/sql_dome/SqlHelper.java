package com.xx.hello.hellondk.sql_dome;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Hello on 2017/9/13.
 */

public class SqlHelper extends SQLiteOpenHelper {
    public static final String CREATE_HISTORY="create table History(" +
            "id integer primary key autoincrement," +
            "start_address text," +
            "end_address text," +
            "start_latlng  real," +
            "end_latlng real)";


    public SqlHelper(Context context) {
        super(context, "history.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HISTORY);
        Log.e("---", "onCreate:创建成功 " );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
