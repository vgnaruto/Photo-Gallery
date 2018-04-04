package com.example.windows10.photogallery;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static MainActivity instance;
    private static int isNew = 0;
    public static DataImage oldData = null;
    public static int status = -1;

    private FloatingActionButton addButton;
    private ListView listFoto;
    private FotoAdapter fa;
    private StorageManagerHelper smHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listFoto = findViewById(R.id.list_view_gallery);
        addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(this);

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();

        smHelper = new StorageManagerHelper(this);

        fa = new FotoAdapter(this);
        listFoto.setAdapter(fa);
        if(isNew == 0){
            isNew = 1;
            fa.setDataImages(smHelper.load());
        }

        if (bundle != null) {
            String data = bundle.getString("data");
            Gson gson = new Gson();
            DataImage currentData = gson.fromJson(data, DataImage.class);
            //INSERT
            if (Main2Activity.statusSama == 0) {
                fa.addFoto(currentData);
                smHelper.save(currentData);
            }
            //EDIT
            else if (Main2Activity.statusSama == 1 && MainActivity.status != -1) {
                fa.updateFoto(currentData, MainActivity.status);
                smHelper.save(MainActivity.oldData, currentData);
                MainActivity.oldData = null;
                Main2Activity.statusSama = 0;
                MainActivity.status = -1;
            }
        }
        fa.presenter.loadData();
        instance = this;
    }

    @Override
    public void onClick(View view) {
        if (view == addButton) {
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        }
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void updateList(ArrayList<DataImage> data) {
        fa.setDataImages(data);
        listFoto.setAdapter(fa);
    }

    public void edit(Bundle dataBundle, int indeks) {
        MainActivity.status = indeks;
        Intent acitivityIntent = new Intent(this, Main2Activity.class);
        acitivityIntent.putExtras(dataBundle);
        startActivity(acitivityIntent);
    }

    public void delete(DataImage data) {
        smHelper.delete(data);
    }

    @Override
    public void onBackPressed() {
    }
}
