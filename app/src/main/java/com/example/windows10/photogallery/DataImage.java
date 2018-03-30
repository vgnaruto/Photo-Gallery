package com.example.windows10.photogallery;

/**
 * Created by Windows 10 on 30/03/2018.
 */

public class DataImage {
    private String image;
    private String judul;

    public DataImage(String image, String judul) {
        this.image = image;
        this.judul = judul;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }
}
