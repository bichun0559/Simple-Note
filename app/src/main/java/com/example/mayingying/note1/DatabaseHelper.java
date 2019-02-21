package com.example.mayingying.note1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mayingying on 2018/5/9.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME_NOTES = "note";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_NOTE_CONTENT = "content";
    public static final String COLUMN_NAME_NOTE_DATE = "date";
    public static final String COLUMN_NAME_NOTE_BG = "bg";
    public static final String COLUMN_NAME_NOTE_DEL="del";
    public DatabaseHelper(Context context) {
        super(context, "note", null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME_NOTES + "(" + COLUMN_NAME_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME_NOTE_CONTENT + " TEXT NOT NULL DEFAULT\"\","
                + COLUMN_NAME_NOTE_DATE + " TEXT NOT NULL DEFAULT\"\","
                + COLUMN_NAME_NOTE_BG+ " TEXT NOT NULL DEFAULT\"\","+ COLUMN_NAME_NOTE_DEL
                +")";
        Log.d("SQL", sql);
        db.execSQL(sql);
       /* ContentValues values=new ContentValues();
        values.put("_id",1);
        values.put("content","欢迎使用简易便签·v·");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String dateString = sdf.format(date);
        values.put("date",dateString);
        values.put("bg",0);
        values.put("del",0);
        db.insert("note",null,values);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // TODO Auto-generated method stub
    }
}



