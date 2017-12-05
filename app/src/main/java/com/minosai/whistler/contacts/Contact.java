package com.minosai.whistler.contacts;

import android.graphics.Bitmap;

/**
 * Created by minos.ai on 04/12/17.
 */

public class Contact {

    private String contactName;
    private String contactNumber;
    private Bitmap contactPhoto;

    public Contact(String contactName, String contactNumber, Bitmap contactPhoto) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.contactPhoto = contactPhoto;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Bitmap getContactPhoto() {
        return contactPhoto;
    }

    public void setContactPhoto(Bitmap contactPhoto) {
        this.contactPhoto = contactPhoto;
    }

    public void sendMessage(String msg){
//        TODO: send sms
    }
}
