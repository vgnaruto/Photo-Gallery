package com.example.windows10.photogallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static int statusSama = 0;

    private Button bSave;
    private FloatingActionButton bCamera;
    private EditText etJudul, etNote;
    private ImageView ivFoto;
    private static Main2Activity instance;
    private ScaleGestureDetector sgd;
    private Matrix matrix;

    private String currentPhotoPath;
    private int rotationAngle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bSave = findViewById(R.id.button_save);
        bCamera = findViewById(R.id.button_camera);
        etJudul = findViewById(R.id.et_judul);
        ivFoto = findViewById(R.id.iv_foto);
        etNote = findViewById(R.id.et_note);

        bSave.setOnClickListener(this);
        bCamera.setOnClickListener(this);
        ivFoto.setOnTouchListener(this);

        matrix = new Matrix();
        ivFoto.setScaleType(ImageView.ScaleType.MATRIX);
        ivFoto.setImageMatrix(matrix);
        sgd = new ScaleGestureDetector(this, new ScaleListener());

        instance = this;
        ivFoto.post(new Runnable() {
            @Override
            public void run() {
                Intent intentExtra = getIntent();
                Bundle bundle = intentExtra.getExtras();
                if (bundle != null) {
                    statusSama = 1;
                    String data = bundle.getString("data");
                    Gson gson = new Gson();
                    DataImage currentData = gson.fromJson(data, DataImage.class);
                    setPic(currentData);
                    etJudul.setText(currentData.getJudul());
                    etNote.setText(currentData.getNote());
                    currentPhotoPath = currentData.getPath();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == bSave) {
            if (ivFoto.getDrawable() != null && !etJudul.getText().toString().equalsIgnoreCase("")) {
                Intent menuIntent = new Intent(this, MainActivity.class);
                DataImage currentData = new DataImage(currentPhotoPath, etJudul.getText().toString(), etNote.getText().toString());
                Bundle dataBundle = new Bundle();
                Gson gson = new Gson();
                dataBundle.putString("data", gson.toJson(currentData));
                menuIntent.putExtras(dataBundle);
                startActivity(menuIntent);
            } else {
                Toast toast = Toast.makeText(this, "Data tidak lengkap", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else if (view == bCamera) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                File foto = null;
                try {
                    foto = createImageFile();
                } catch (IOException e) {

                }
                if (foto != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.example.windows10.photogallery", foto);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic(null);
        }
    }

    public static Main2Activity getInstance() {
        return instance;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        statusSama = 0;
        MainActivity.status = -1;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == ivFoto) {
            return this.sgd.onTouchEvent(motionEvent);
        }
        return false;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = detector.getCurrentSpanY() / detector.getPreviousSpanY();
            matrix.postScale(scale, scale);
            ivFoto.setImageMatrix(matrix);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "FOTO_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPic(DataImage data) {
        if (data != null) {
            currentPhotoPath = data.getPath();
        }
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 5;
        bmOptions.inPurgeable = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        Bitmap bm = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        try {
            ExifInterface exif = new ExifInterface(currentPhotoPath);
            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

            rotationAngle = 0;
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
