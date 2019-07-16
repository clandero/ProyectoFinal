package com.example.proyectofinal;

public class InstruccionesItem {
    private int mImageResource;
    private String title;
    private String content;
    public InstruccionesItem(int imageResource, String t, String text){
        mImageResource = imageResource;
        title = t;
        content = text;
    }
    public int getImageResource(){
        return mImageResource;
    }
    public String getTitle(){
        return title;
    }
    public String getContent(){
        return content;
    }
}
