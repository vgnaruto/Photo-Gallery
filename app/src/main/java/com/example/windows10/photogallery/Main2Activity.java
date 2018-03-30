package com.example.windows10.photogallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Button bSave;
    private FloatingActionButton bCamera;
    private EditText etJudul;
    private ImageView ivFoto;
    private static Main2Activity instance;

    public static int statusSama = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bSave = findViewById(R.id.button_save);
        bCamera = findViewById(R.id.button_camera);
        etJudul = findViewById(R.id.et_judul);
        ivFoto = findViewById(R.id.iv_foto);

        bSave.setOnClickListener(this);
        bCamera.setOnClickListener(this);

        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if(bundle != null){
            statusSama = 1;
            String data = bundle.getString("data");
            Gson gson = new Gson();
            DataImage currentData = gson.fromJson(data, DataImage.class);
            etJudul.setText(currentData.getJudul());
            ivFoto.setImageBitmap(base64ToBitmap(currentData.getImage()));
        }

        instance = this;
    }

    @Override
    public void onClick(View view) {
        if (view == bSave) {
            if (ivFoto.getDrawable() != null && !etJudul.getText().toString().equalsIgnoreCase("")) {
                Intent menuIntent = new Intent(this, MainActivity.class);
                DataImage currentData = new DataImage(bitmapToBase64(), etJudul.getText().toString());
                Bundle dataBundle = new Bundle();
                Gson gson = new Gson();
                dataBundle.putString("data", gson.toJson(currentData));
                menuIntent.putExtras(dataBundle);
                startActivity(menuIntent);
            }else{
                Toast toast = Toast.makeText(this,"Data tidak lengkap",Toast.LENGTH_SHORT);
                toast.show();
            }
        } else if (view == bCamera) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            this.ivFoto.setImageBitmap(imageBitmap);
        }
    }

    public String bitmapToBase64() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bm = ((BitmapDrawable) ivFoto.getDrawable()).getBitmap();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap base64ToBitmap(String data) {
        byte[] b = Base64.decode(data, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
    }

    public static Main2Activity getInstance(){
        return instance;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        statusSama = 0;
        MainActivity.indeks = -1;
    }
}
