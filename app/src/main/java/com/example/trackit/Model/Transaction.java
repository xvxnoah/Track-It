package com.example.trackit.Model;

import android.net.Uri;
import android.os.Parcelable;

import java.io.Serializable;

public class Transaction {
    private String uniqueID;
    private String name;
    private String type;
    private String budget;
    private double quantity;
    private String date;
    private int pic;

    //Constructor
    public Transaction(String uniqueID, String name, String type, double quantity, String date){
        this.uniqueID = uniqueID;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.date = date;
    }

    public Transaction(){
        this.name = null;
        this.type = null;
        this.quantity = 0f;
        this.date = null;
        this.pic = -1;
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

    public String getUniqueID() { return this.uniqueID; }

    public String getBudget() { return this.budget; }

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

    public void setUniqueID(String uniqueID) { this.uniqueID = uniqueID; }

    public void setBudget(String budget) { this.budget = budget; }
}
