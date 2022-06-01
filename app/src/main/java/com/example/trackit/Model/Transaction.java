package com.example.trackit.Model;

import android.net.Uri;

public class Transaction {
    private String name;
    private String type;
    private double quantity;
    private String date;
    private int pic;
    private Uri selectedImageUri;

    //Constructor
    public Transaction(String name, String type, double quantity, String date, Uri SelectedImageUri){
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.date = date;
        this.selectedImageUri = selectedImageUri;
    }

    public Transaction(){
        this.name = null;
        this.type = null;
        this.quantity = 0f;
        this.date = null;
        this.pic = -1;
        this.selectedImageUri = null;
    }

    //Getters
    public String getName(){
        return this.name;
    }

    public String getType(){
        return this.type;
    }

    public double getQuantity(){
        return this.quantity;
    }

    public String getDate(){
        return this.date;
    }

    public int getPic() { return this.pic; }

    public Uri getUri() { return this.selectedImageUri; }

    //Setters
    public void setName(String name){
        this.name = name;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setQuantity(double quantity){
        this.quantity = quantity;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setPic(int pic) { this.pic = pic; }

    public void setUri(Uri image) { this.selectedImageUri = image; }
}
