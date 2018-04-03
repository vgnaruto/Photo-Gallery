package com.example.windows10.photogallery;

/**
 * Created by Windows 10 on 30/03/2018.
 */

public class DataImage {
    private String path;
    private String judul;
    private String note;

    public DataImage(String path, String judul, String note) {
        this.path = path;
        this.judul = judul;
        this.note = note;
    }

    public String getPath() {
        return path;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }
}
