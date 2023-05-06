package com.example.amoremanager;

public class Bookings {
    private String EquipmentName, ImageUri, Date;

    public Bookings(String equipmentName, String imageUri, String date) {
        EquipmentName = equipmentName;
        ImageUri = imageUri;
        Date = date;
    }

    public Bookings() {
    }

    public String getEquipmentName() {
        return EquipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        EquipmentName = equipmentName;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
