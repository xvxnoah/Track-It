package com.example.trackit.Model;

import java.util.ArrayList;

public class UserInfo {
    private String name;
    private String email;
    private double quantity;
    private double moneySaved;
    private double moneyWasted;
    private ArrayList<Transaction> transactions;
    private ArrayList<Budget> budgets;
    private boolean DriveLogin;

    public void UserInfo(){
        this.name = null;
        this.email = null;
        this.quantity = 0;
        this.moneySaved = 0;
        this.moneyWasted = 0;
        this.transactions = new ArrayList<Transaction>();
        this.budgets = new ArrayList<Budget>();
        this.DriveLogin = false;
    }

    public void UserInfo(float moneySaved, float moneyWasted, ArrayList<Transaction> transactions, ArrayList<Budget> budgets ,boolean DriveLogin){
        this.moneySaved = moneySaved;
        this.moneyWasted = moneyWasted;
        this.transactions = transactions;
        this.budgets = budgets;
        this.DriveLogin = DriveLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    //Getters
    public String getName(){
        return this.name;
    }

    public double getQuantity(){
        return this.quantity;
    }

    public double getMoneySaved(){
        return this.moneySaved;
    }

    public double getMoneyWasted(){
        return this.moneyWasted;
    }

    public ArrayList<Transaction> getTransactions(){
        return this.transactions;
    }

    public ArrayList<Budget> getBudgets() {
        return budgets;
    }

    //Setters
    public void setName(String name){
        this.name = name;
    }

    public void setQuantity(double quantity){
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

    public void setBudgets(ArrayList<Budget> budgets) {
        this.budgets = budgets;
    }

    public void setDriveLogin(boolean DriveLogin){
        this.DriveLogin = DriveLogin;
    }

    public void addTransaction(Transaction transaction){
        if(this.transactions==null){
            this.transactions = new ArrayList<Transaction>();
        }
        this.transactions.add(transaction);
    }

    public void updateWasted(double quantity) {
        this.quantity = this.quantity - quantity;
        this.moneyWasted = this.moneyWasted + quantity;
    }

    public void updateSave(double quantity) {
        this.quantity = this.quantity + quantity;
        this.moneySaved = this.moneySaved + quantity;
    }
}
