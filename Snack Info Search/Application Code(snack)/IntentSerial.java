package edu.sfsu.cs.orange.ocr.intent.adt;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by J on 2015-05-10.
 */
public class IntentSerial implements Serializable {

    private String proName;
    private Drawable conpanyIm;
    private int second;

    public void setProName(String name){
        proName=name;
    }

    public String getProName(){
        return proName;
    }
    
    public void setSecond(int second){
    	this.second = second;
    }

    public void setConpanyIm(Drawable conim){
        conpanyIm= conim;
    }
    
    public Drawable getConpanyIm(){
        return conpanyIm;
    }
    
    public int getSecond(){
    	return second; 
    }
}
