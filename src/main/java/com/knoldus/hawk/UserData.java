package com.knoldus.hawk;

public class UserData {
    byte[] RFID;
    byte[] ClickedImage;

    UserData(byte[] RFID,byte[] ClickedImage){
        this.RFID = RFID;
        this.ClickedImage = ClickedImage;
    }

    public byte[] getRFID() {
        return RFID;
    }

    public byte[] getClickedImage() {
        return ClickedImage;
    }
}
