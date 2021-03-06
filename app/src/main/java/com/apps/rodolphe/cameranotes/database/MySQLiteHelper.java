package com.apps.rodolphe.cameranotes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_PATHS = "paths";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_SHAPE = "shape";

    private static final String DATABASE_NAME = "paths.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PATHS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_PATH
            + " text not null, " + COLUMN_SHAPE
            + " text);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATHS);
        onCreate(db);
    }

} 