package com.example.trackit;

import java.util.ArrayList;

public class UserInfo {
    private String name;
    private float quantity;
    private float moneySaved;
    private float moneyWasted;
    private ArrayList<Transaction> transactions;
    private boolean DriveLogin;

    public void UserInfo(){
        this.name = null;
        this.quantity = 0;
        this.moneySaved = 0;
        this.moneyWasted = 0;
        this.transactions = new ArrayList<Transaction>();
        this.DriveLogin = false;
    }

    public void UserInfo(float moneySaved, float moneyWasted, ArrayList<Transaction> transactions, boolean DriveLogin){
        this.moneySaved = moneySaved;
        this.moneyWasted = moneyWasted;
        this.transactions = transactions;
        this.DriveLogin = DriveLogin;
    }

    //Getters
    public String getName(){
        return this.name;
    }

    public float getQuantity(){
        return this.quantity;
    }

    public float getMoneySaved(){
        return this.moneySaved;
    }

    public float getMoneyWasted(){
        return this.moneyWasted;
    }

    public ArrayList<Transaction> getTransactions(){
        return this.transactions;
    }

    public boolean getDriveLogin(){
        return this.DriveLogin;
    }

    //Setters
    public void setName(String name){
        this.name = name;
    }

    public void setQuantity(float quantity){
        this.quantity = quantity;
    }

    public void setMoneySaved(float moneySaved){
        this.moneySaved = moneySaved;
    }

    public void setMoneyWasted(float moneyWasted){
        this.moneyWasted = moneyWasted;
    }

    public void setTransactions(ArrayList<Transaction> transactions){
        this.transactions = transactions;
    }

    public void setDriveLogin(boolean DriveLogin){
        this.DriveLogin = DriveLogin;
    }

}
