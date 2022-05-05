package com.example.trackit.Transactions;

public class TransactionVo {
    private String category;
    private String title;
    private Double amount;
    private String date;
    private int pic;

    public TransactionVo(){

    }

    public TransactionVo(String category, String title, Double amount, String date, int pic) {
        this.category = category;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.pic = pic;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}
