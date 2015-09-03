package edu.sfsu.cs.orange.ocr.intent.adt;

import java.io.Serializable;

/**
 * Created by J on 2015-05-10.
 */
public class UserUseAddIntentSerial implements Serializable {

    private int ListViewPosition;


    public void setListViewPosition(int position){
        ListViewPosition=position;
    }

    public int getListViewPosition(){
        return ListViewPosition;
    }
}
