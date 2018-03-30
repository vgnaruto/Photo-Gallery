package com.example.windows10.photogallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static MainActivity instance;

    private FloatingActionButton addButton;
    private ListView listFoto;
    private FotoAdapter fa;

    private static int indeks = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listFoto = findViewById(R.id.list_view_gallery);
        addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(this);

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();

        if(fa == null) {
            fa = new FotoAdapter(this);
            listFoto.setAdapter(fa);
            fa.presenter.loadData();
        }

        if (bundle != null) {
            String data = bundle.getString("data");
            Gson gson = new Gson();
            DataImage currentData = gson.fromJson(data, DataImage.class);
            if(Main2Activity.statusSama==0) {
                fa.addFoto(currentData);
            }else if(Main2Activity.statusSama == 1 && MainActivity.indeks != -1){
                fa.updateFoto(currentData,MainActivity.indeks);
                Main2Activity.statusSama = 0;
                MainActivity.indeks = -1;
            }
        }

        instance = this;
    }

    @Override
    public void onClick(View view) {
        if (view == addButton) {
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        }
    }

    public Bitmap base64ToBitmap(String data) {
        byte[] b = Base64.decode(data, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void updateList(ArrayList<DataImage> data) {
        fa.setDataImages(data);
        listFoto.setAdapter(fa);
    }

    public void edit(Bundle dataBundle, int indeks){
        MainActivity.indeks = indeks;
        Intent acitivityIntent = new Intent(this, Main2Activity.class);
        acitivityIntent.putExtras(dataBundle);
        startActivity(acitivityIntent);
    }
}
