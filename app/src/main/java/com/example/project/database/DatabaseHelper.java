package com.example.project.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DB_IMAGES.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "IMAGES";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "PATH TEXT, " +
                "NAME TEXT, " +
                "DESCRIPTION TEXT, " +
                "PRICE INTEGER) ;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
