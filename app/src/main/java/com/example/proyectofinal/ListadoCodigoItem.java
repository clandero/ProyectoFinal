package com.example.proyectofinal;

public class ListadoCodigoItem {
    private int mImageResource;
    private String mText;
    public ListadoCodigoItem(int imageResource, String text){
        mImageResource = imageResource;
        mText = text;
    }
    public int getImageResource(){
        return mImageResource;
    }
    public String getTextResource(){
        return mText;
    }
}




