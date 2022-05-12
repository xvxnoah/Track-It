package com.example.trackit.Model;

public class Budget {
    private String type;
    private double quantity;
    private boolean alert;

    public Budget(String type, double quantity, boolean alert){
        this.type = type;
        this.quantity = quantity;
        this.alert = alert;
    }

    public Budget(){
        this.type = null;
        this.quantity = 0;
        this.alert = false;
    }

    //Getters
    public String getType() {
        return type;
    }

    public double getQuantity() {
        return quantity;
    }

    public boolean isAlert() {
        return alert;
    }

    //Setters
    public void setType(String type) {
        this.type = type;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }
}
