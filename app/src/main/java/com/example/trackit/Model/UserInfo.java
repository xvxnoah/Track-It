package com.example.trackit.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

public class UserInfo extends Observable {
    private volatile static UserInfo uniqueInstance;
    private String name;
    private String email;
    private double quantity;
    private double moneySaved;
    private double moneyWasted;
    private ArrayList<Transaction> transactions;
    private ArrayList<Budget> budgets;
    private boolean DriveLogin;
    private String imageStr;
    private String transactionStr;

    public static UserInfo getInstance(){
        if(uniqueInstance == null){
            synchronized (UserInfo.class){
                if(uniqueInstance == null){
                    uniqueInstance = new UserInfo();
                }
            }
        }
        return uniqueInstance;
    }

    public static void setUniqueInstance(UserInfo user){
        uniqueInstance = user;
    }

    public void UserInfo(){
        this.name = null;
        this.email = null;
        this.quantity = 0;
        this.moneySaved = 0;
        this.moneyWasted = 0;
        this.transactions = new ArrayList<Transaction>();
        this.budgets = new ArrayList<Budget>();
        this.DriveLogin = false;
        this.imageStr = "null";
    }

    public void UserInfo(double moneySaved, double moneyWasted, ArrayList<Transaction> transactions, ArrayList<Budget> budgets, boolean DriveLogin){
        this.moneySaved = moneySaved;
        this.moneyWasted = moneyWasted;
        this.transactions = transactions;
        this.budgets = budgets;
        this.DriveLogin = DriveLogin;
        this.imageStr = "null";
        this.transactionStr = null;
    }

    //Getters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getImageStr(){
        return imageStr;
    }

    //Setters
    public void setName(String name){
        this.name = name;
    }

    public void setImageStr(String imageStr) {
        this.imageStr = imageStr;
    }

    public void setQuantity(double quantity){
        this.quantity = quantity;
    }

    public void setMoneySaved(double moneySaved){
        this.moneySaved = moneySaved;
    }

    public void setMoneyWasted(double moneyWasted){
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

        setChanged();
        notifyObservers(transaction);
    }

    public boolean deleteTransaction(Transaction transaction){
        String IDdelete = transaction.getUniqueID();
        String ID;

        for(Transaction t : getTransactions()){
            ID = t.getUniqueID();

            if(ID.equals(IDdelete)){
                transactions.remove(t);

                if(transaction.getQuantity() < 0){
                    this.updateWasted(transaction.getQuantity());

                    if(transaction.getBudget() != null){
                        this.updateBudget(transaction.getBudget(), Math.abs(transaction.getQuantity()), true);
                    }

                } else{
                    this.updateSave(-transaction.getQuantity());

                    if(transaction.getBudget() != null){
                        this.updateBudget(transaction.getBudget(), transaction.getQuantity(), false);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean updateTransaction(Transaction old, Transaction updated){
        String thisID;
        String oldID = old.getUniqueID();

        for(Transaction t : getTransactions()){
            thisID = t.getUniqueID();

            if(thisID.equals(oldID)){
                deleteTransaction(t);

                addTransaction(updated);

                if(updated.getQuantity() < 0){
                    updateWasted(-updated.getQuantity());

                    if(updated.getBudget() != null){
                        updateBudget(updated.getBudget(), Math.abs(updated.getQuantity()), false);
                    }
                } else{
                    updateSave(updated.getQuantity());

                    if(updated.getBudget() != null){
                        updateBudget(updated.getBudget(), updated.getQuantity(), true);
                    }
                }

                return true;
            }
        }
        return false;
    }

    public void addBudget(Budget budget){
        if(this.budgets==null){
            this.budgets = new ArrayList<Budget>();
        }
        this.budgets.add(budget);
    }

    public void updateWasted(double quantity) {
        this.quantity = this.quantity - quantity;
        this.moneyWasted = this.moneyWasted + quantity;
    }

    public void updateSave(double quantity) {
        this.quantity = this.quantity + quantity;
        this.moneySaved = this.moneySaved + quantity;
    }

    public boolean updateBudget(String budget, double quantity, boolean sumar) {
        ArrayList<Budget> Budgets = getBudgets();
        Budget actual;
        Iterator<Budget> iter = null;
        Budget updatedBudget = null;
        if (Budgets != null) {
            iter = Budgets.iterator();
        }
        boolean found = false;
        int pos = 0;
        if (Budgets != null) {
            while (iter.hasNext()) {
                actual = iter.next();
                if(actual.getName().equals(budget)){
                    updatedBudget = actual;
                    found = true;
                }
                if(!found){
                    pos++;
                }
            }
        }
        this.budgets.remove(pos);
        if(updatedBudget != null && (updatedBudget.getQuantity()>=quantity || sumar)){
            updatedBudget.updateQuantity(quantity, sumar);
            this.budgets.add(updatedBudget);
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteBudget(String budget){
        ArrayList<Budget> Budgets = getBudgets();
        Budget actual;
        Iterator<Budget> iter = null;

        if (Budgets != null) {
            iter = Budgets.iterator();
        }

        boolean found = false;
        int pos = 0;
        if (Budgets != null) {
            while (iter.hasNext()) {
                actual = iter.next();
                if(actual.getName().equals(budget)){
                    found = true;
                }
                if(!found){
                    pos++;
                }
            }
        }

        if(found){
            this.budgets.remove(pos);
            return true;
        }
        return false;
    }

    public boolean returnAlertStat(String budget){
        ArrayList<Budget> Budgets = getBudgets();
        Budget actual;
        Iterator<Budget> iter = null;
        Budget updated = null;

        if (Budgets != null) {
            iter = Budgets.iterator();
        }

        boolean found = false;
        int pos = 0;
        if (Budgets != null) {
            while (iter.hasNext()) {
                actual = iter.next();
                if(actual.getName().equals(budget)){
                    updated = actual;
                    found = true;
                }
                if(!found){
                    pos++;
                }
            }
        }

        if(found){
            if(updated != null){
                if(updated.isAlert()){
                    return true;
                }
            }
        }
        return false;
    }
}
