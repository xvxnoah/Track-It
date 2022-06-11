package com.example.trackit.Model;

public class Budget {
    private String name;
    private String type;
    private double objective;
    private double quantity;
    private boolean alert;

    public Budget(String name, String type, double objective, boolean alert){
        this.name = name;
        this.type = type;
        this.objective = objective;
        this.quantity = 0;
        this.alert = alert;
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

    public String getName() {
        return this.name;
    }

    public void setName(String nameBudget){
        this.name = nameBudget;
    }

    public void updateQuantity(double quantity) {
        this.quantity = this.quantity + quantity;
        if(quantity >= objective){
            alert = true;
        }
    }
}
