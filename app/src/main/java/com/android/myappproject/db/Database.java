package com.android.myappproject.db;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {

    public static DatabaseHelper dbHelper;
    public static SQLiteDatabase database;

    public static String tableName;

    public static Activity activity;

    public Database(Activity activity){
        this.activity = activity;
    }


    public static void createDatabase(String name) {
        println("createDatabase 호출됨.");

        dbHelper = new DatabaseHelper(activity);
        database = dbHelper.getWritableDatabase();

        println("데이터베이스 생성함 : " + name);
    }

    public static void createTable(String name) {
        println("createTable 호출됨.");

        if (database == null) {
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }

/*        database.execSQL("create table if not exists " + name + "("
                + " _id integer PRIMARY KEY autoincrement, "
                + " name text, "
                + " age integer, "
                + " mobile text)");*/

            database.execSQL(
                "create table if not exists rec("
                + " _id integer PRIMARY KEY autoincrement, "
                + " name text, "
                + " date text, "
                + " time text, "
                + " checklist text)"
            );

        println("테이블 생성함 : " + name);

        tableName = name;
    }

    public static void insertRecord(String name, String date, String time, String checklist) {
        println("insertRecord 호출됨.");

        if (database == null) {
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }

        if (tableName == null) {
            println("테이블을 먼저 생성하세요.");
            return;
        }

/*        database.execSQL("insert into " + tableName
                + "(name, age, mobile) "
                + " values "
                + "('John', 20, '010-1000-1000')");*/

        database.execSQL("insert into " + tableName
                + "(name, date, time, checklist) "
                + " values "
                + "('"+name+"','"+date+"','"+time+"','"+checklist+"')");
        println("레코드 추가함.");
    }

    public static void println(String data) {
        Log.i("sqlitedbexample",data + "\n");
    }

    public static Map<String, String> executeQuery(String setname, String setday) {
        println("executeQuery 호출됨.");

        //Cursor cursor = database.rawQuery("select _id, name, age, mobile from "+tableName, null);
        //Cursor cursor = database.rawQuery("select _id, name, date, time, checklist from "+tableName+" where name = 'user'", null);

        //Cursor cursor = database.rawQuery("select _id, name, date, time, checklist from "+tableName+" where name = '"+setname+"'", null);
        Cursor cursor = database.rawQuery("select _id, name, date, time, checklist from "+tableName+" where name = '"+setname+"' and date = '"+setday+"'", null);
        int recordCount = cursor.getCount();
        //println("레코드 개수 : " + recordCount+","+name+","+date);

        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < recordCount; i++) {
            cursor.moveToNext();

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String date = cursor.getString(2);
            Log.i("wefwfwefw",date+","+setday);
            String time = cursor.getString(3);
            String checklist = cursor.getString(4);

            map.put(time, checklist);

            println("dfgege" + i + " : " + id + ", " + name + ", " + date + ", " + time+","+checklist);
        }


        cursor.close();
        return map;
    }
}
