package com.example.windows10.photogallery;

import java.util.ArrayList;

/**
 * Created by toshiba pc on 4/4/2018.
 */

public interface StorageManager {
    void save(DataImage image);
    ArrayList<DataImage> load();
    void save(DataImage oldData, DataImage newData);
    void delete(DataImage image);
}
