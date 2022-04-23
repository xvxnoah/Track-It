package com.example.trackit;

import java.util.ArrayList;

public class UserInfo {
    private float moneySaved;
    private float moneyWasted;
    private ArrayList<Transaction> transactions;
    private boolean DriveLogin;

    public void UserInfo(float moneySaved, float moneyWasted, ArrayList<Transaction> transactions, boolean DriveLogin){
        this.moneySaved = moneySaved;
        this.moneyWasted = moneyWasted;
        this.transactions = transactions;
        this.DriveLogin = DriveLogin;
    }

    //Getters
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
