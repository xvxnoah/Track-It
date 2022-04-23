package com.example.trackit;
import java.util.Date;

public class Transaction {
    private String name;
    private String type;
    private float quantity;
    private Date date;

    //Constructor
    public void Transaction(String name, String type, float quantity, Date date){
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.date = date;
    }

    //Getters
    public String getName(){
        return this.name;
    }

    public String getType(){
        return this.type;
    }

    public float getQuantity(){
        return this.quantity;
    }

    public Date getDate(){
        return this.date;
    }

    //Setters
    public void setName(String name){
        this.name = name;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setQuantity(float quantity){
        this.quantity = quantity;
    }

    public void setDate(Date date){
        this.date = date;
    }
}
