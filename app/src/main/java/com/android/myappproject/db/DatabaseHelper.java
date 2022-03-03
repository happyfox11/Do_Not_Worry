package com.android.myappproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String NAME = "prev.db";
    public static int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    public void onCreate(@NonNull SQLiteDatabase db) {
        println("onCreate 호출됨");


/*        String sql = "create table if not exists rec("
                + " _id integer PRIMARY KEY autoincrement, "
                + " name text, "
                + " age integer, "
                + " mobile text)";*/

                String sql = "create table if not exists rec("
                + " _id integer PRIMARY KEY autoincrement, "
                + " name text, "
                + " date text, "
                + " time text, "
                + " checklist text)";

        db.execSQL(sql);
    }

    public void onOpen(SQLiteDatabase db) {
        println("onOpen 호출됨");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        println("onUpgrade 호출됨 : " + oldVersion + " -> " + newVersion);

        if (newVersion > 1) {
            db.execSQL("DROP TABLE IF EXISTS rec");
        }
    }

    public void println(String data) {
        Log.d("DatabaseHelper", data);
    }
}
