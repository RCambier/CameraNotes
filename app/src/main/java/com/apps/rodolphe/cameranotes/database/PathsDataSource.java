package com.apps.rodolphe.cameranotes.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class PathsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_PATH };

    public PathsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Path createPath(String path) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PATH, path);
        long insertId = database.insert(MySQLiteHelper.TABLE_PATHS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PATHS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Path newPath = cursorToPath(cursor);
        cursor.close();
        return newPath;
    }

    public void deletePath(Path path) {
        long id = path.getId();
        System.out.println("Path deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_PATHS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Path> getAllPaths() {
        List<Path> paths = new ArrayList<Path>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PATHS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Path path = cursorToPath(cursor);
            paths.add(path);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return paths;
    }

    private Path cursorToPath(Cursor cursor) {
        Path path = new Path();
        path.setId(cursor.getLong(0));
        path.setPath(cursor.getString(1));
        return path;
    }
} 