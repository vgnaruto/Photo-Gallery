package com.example.windows10.photogallery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Windows 10 on 31/03/2018.
 */

public class DBAdapter {
    DBHelper dbHelper;
    public DBAdapter(Context context){
        dbHelper = new DBHelper(context);
        dbHelper.getWritableDatabase();
    }

    public long insertData(DataImage data){
        Gson gson = new Gson();
        String dataJson = gson.toJson(data);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_2,dataJson);
        long id = db.insert(DBHelper.TB_NAME, null, cv);
        return id;
    }

    public int delete(DataImage data){
        Gson gson = new Gson();
        String dataJson = gson.toJson(data);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] args = {dataJson};
        int count = db.delete(DBHelper.TB_NAME, DBHelper.COLUMN_2+" = ?",args);
        return count;
    }

    public int update(DataImage oldData, DataImage newData){
        Gson gson = new Gson();
        String oldDataJson = gson.toJson(oldData);
        String newDataJson = gson.toJson(newData);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_2, newDataJson);
        String[] args = {oldDataJson};
        int count = db.update(DBHelper.TB_NAME, cv, DBHelper.COLUMN_2+" = ?",args);
        return count;
    }

    public ArrayList<DataImage> read(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DBHelper.COLUMN_2
        };
        Cursor cursor = db.query(
            DBHelper.TB_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        ArrayList<DataImage> allFoto = new ArrayList<>();
        while(cursor.moveToNext()){
            String data = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_2));
            Gson gson = new Gson();
            DataImage currentData = gson.fromJson(data,DataImage.class);
            allFoto.add(currentData);
        }
        cursor.close();
        return  allFoto;
    }

    static class DBHelper extends SQLiteOpenHelper {
        public static final String TB_NAME = "gallery";
        public static final String COLUMN_1 = "id";
        public static final String COLUMN_2 = "data_image";
        private static final String SQL_CREATE =
                "CREATE TABLE " + TB_NAME + " (" +
                        COLUMN_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_2 + " varchar(255));";
        private static final String SQL_DELETE =
                "DROP TABLE IF EXISTS " + TB_NAME;

        public static final int DB_VERSION = 1;
        public static final String DB_NAME = "gallery.db";

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
//            Log.d("SQLITE","ONCREATE");
            db.execSQL(SQL_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
//            Log.d("SQLITE","ONUPGRADE");
            db.execSQL(SQL_DELETE);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}
