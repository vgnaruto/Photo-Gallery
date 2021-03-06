package com.example.windows10.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Windows 10 on 30/03/2018.
 */

public class FotoAdapter extends BaseAdapter {
    private static ArrayList<DataImage> dataImages = new ArrayList<>();
    public MainPresenter presenter;
    public MainActivity ui;

    public FotoAdapter(MainActivity ui) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(MainActivity.getInstance()).inflate(R.layout.list_foto, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataImage currentData = (DataImage) getItem(position);
                Bundle dataBundle = new Bundle();
                Gson gson = new Gson();
                dataBundle.putString("data", gson.toJson(currentData));
                MainActivity.oldData = currentData;
                MainActivity.getInstance().edit(dataBundle, position);
            }
        });
        vh.ibSampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.delete(position);
            }
        });

        vh.updateView((DataImage) getItem(position));

        return convertView;
    }

    public void addFoto(DataImage data) {
        presenter.addFoto(data);
    }

    public void setDataImages(ArrayList<DataImage> data) {
        this.dataImages = data;
        presenter.setDataImage(dataImages);
    }

    public void updateFoto(DataImage data, int posisi) {
        dataImages.set(posisi, data);
    }

    private class ViewHolder {
        protected ImageView ivFoto;
        protected TextView tvJudul;
        protected ImageButton ibSampah;

        public ViewHolder(View v) {
            this.ivFoto = v.findViewById(R.id.iv_foto);
            this.tvJudul = v.findViewById(R.id.tv_judul);
            this.ibSampah = v.findViewById(R.id.b_sampah);
        }

        public void updateView(DataImage data) {
//            Bitmap bitmap = MainActivity.getInstance().base64ToBitmap(data.getPath());
//            this.ivFoto.setImageBitmap(bitmap);
            setPic(data);
            String judul = data.getJudul();
            this.tvJudul.setText("Judul : " + judul);
        }

        private void setPic(DataImage data) {
            String currentPhotoPath = data.getPath();
            Matrix matrix = new Matrix();
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = 8;
            bmOptions.inPurgeable = true;
            BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            Bitmap bm = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            try {
                ExifInterface exif = new ExifInterface(currentPhotoPath);
                String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

                int rotationAngle = 0;
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotationAngle = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotationAngle = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotationAngle = 270;
                        break;
                }
                matrix.setRotate(rotationAngle,bm.getWidth()/2, bm.getHeight()/2);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bm,0,0,bmOptions.outWidth,bmOptions.outHeight,matrix,true);
                ivFoto.setImageBitmap(rotatedBitmap);
            } catch (IOException e) {

            }
        }
    }
}
