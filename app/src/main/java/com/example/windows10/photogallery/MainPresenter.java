package com.example.windows10.photogallery;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Windows 10 on 30/03/2018.
 */

public class MainPresenter {
    public ArrayList<DataImage> dataImages;
    public MainActivity ui;

    public MainPresenter(ArrayList<DataImage> data,MainActivity ui){
        this.dataImages = data;
        this.ui = ui;
    }
    public void addFoto(DataImage image){
        dataImages.add(image);
        MainActivity.getInstance().updateList(dataImages);
    }
    public void loadData(){
        ui.updateList(dataImages);
    }
    public void delete(int position){
        ui.delete(dataImages.get(position));
        dataImages.remove(position);
        ui.updateList(dataImages);
    }
    public void setDataImage(ArrayList<DataImage> datas){
        this.dataImages = datas;
    }
}
