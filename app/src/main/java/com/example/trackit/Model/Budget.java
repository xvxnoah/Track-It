package com.example.trackit.Model;

import android.graphics.Color;

public class Budget {
    private String name;
    private String type;
    private double objective;
    private double quantity;
    private boolean alert;
    private int color;

    public Budget(String name, String type, double objective, boolean alert, int color){
        this.name = name;
        this.type = type;
        this.objective = objective;
        this.quantity = 0;
        this.alert = alert;
        this.color = color;
    }

    public Budget(){
        this.type = null;
        this.objective = 0;
        this.quantity = 0;
        this.alert = false;
    }

    //Getters
    public String getType() {
        return type;
    }

    public double getObjective() {
        return objective;
    }

    public double getQuantity() { return quantity; }

    public int getColor() { return color; }

    public boolean isAlert() {
        return alert;
    }

    //Setters
    public void setType(String type) {
        this.type = type;
    }

    public void setObjective(double objective) {
        this.objective = objective;
    }

    public void setQuantity(double quantity) { this.quantity = quantity; }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public void setColor(int color) { this.color = color; }

    public String getName() {
        return this.name;
    }

    public void setName(String nameBudget){
        this.name = nameBudget;
    }

    public void updateQuantity(double quantity, boolean sumar) {
        if(sumar){
            this.quantity = this.quantity + quantity;
        }else{
            this.quantity = this.quantity - quantity;
        }
        if(quantity >= objective){
            alert = true;
        }
    }
}
