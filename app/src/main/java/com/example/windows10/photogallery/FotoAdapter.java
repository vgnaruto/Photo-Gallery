package com.example.windows10.photogallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Windows 10 on 30/03/2018.
 */

public class FotoAdapter extends BaseAdapter {
    private static ArrayList<DataImage> dataImages = new ArrayList<>();
    public MainPresenter presenter;
    public MainActivity ui;

    public FotoAdapter(MainActivity ui){
        this.ui = ui;
        presenter = new MainPresenter(dataImages, this.ui);
    }
    @Override
    public int getCount() {
        return dataImages.size();
    }

    @Override
    public Object getItem(int i) {
        return dataImages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null){
            convertView = LayoutInflater.from(MainActivity.getInstance()).inflate(R.layout.list_foto,parent,false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataImage currentData = (DataImage)getItem(position);
                Bundle dataBundle = new Bundle();
                Gson gson = new Gson();
                dataBundle.putString("data",gson.toJson(currentData));
                MainActivity.getInstance().edit(dataBundle,position);
            }
        });

        vh.updateView((DataImage)getItem(position));

        return convertView;
    }
    public void addFoto(DataImage data){
        presenter.addFoto(data);
    }
    public void setDataImages(ArrayList<DataImage> data){
        this.dataImages = data;
    }
    public void updateFoto(DataImage data, int posisi){
        dataImages.set(posisi,data);
    }
    private class ViewHolder{
        protected ImageView ivFoto;
        protected TextView tvJudul;

        public ViewHolder(View v){
            this.ivFoto = v.findViewById(R.id.iv_foto);
            this.tvJudul = v.findViewById(R.id.tv_judul);
        }

        public void updateView(DataImage data){
            Bitmap bitmap = MainActivity.getInstance().base64ToBitmap(data.getImage());
            String judul = data.getJudul();
            this.ivFoto.setImageBitmap(bitmap);
            this.tvJudul.setText("Judul : "+judul);
        }
    }
}
