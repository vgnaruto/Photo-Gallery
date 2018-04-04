package com.example.windows10.photogallery;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by toshiba pc on 4/4/2018.
 */

public class StorageManagerHelper implements StorageManager {
    private DBAdapter db;
    public StorageManagerHelper(Context context){
        db = new DBAdapter(context);
    }
    @Override
    public void save(DataImage image) {
        db.insertData(image);
    }

    @Override
    public ArrayList<DataImage> load() {
        return db.read();
    }

    @Override
    public void save(DataImage oldData, DataImage newData) {
        db.update(oldData, newData);
    }

    @Override
    public void delete(DataImage image) {
        db.delete(image);
    }
}
