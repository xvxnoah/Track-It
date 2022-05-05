package com.example.trackit.Transactions;
import java.util.Date;

public class Transaction {
    private String name;
    private String type;
    private double quantity;
    private Date date;

    //Constructor
    public Transaction(String name, String type, double quantity, Date date){
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.date = date;
    }

    public Transaction(){
        this.name = null;
        this.type = null;
        this.quantity = 0;
        this.date = null;
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

    public void setQuantity(double quantity){
        this.quantity = quantity;
    }

    public void setDate(Date date){
        this.date = date;
    }
}
