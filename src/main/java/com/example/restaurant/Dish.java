package com.example.restaurant;

public class Dish {
    private int did;

    private String dname;
    private String description;
    private double price;
    private DishType category;

    public Dish(int did, String dname, String description, double price, String categoryString) {
        this.did = did;
        this.dname = dname;
        this.description = description;
        this.price = price;
        this.category = DishType.valueOf(categoryString);
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public DishType category() {
        return category;
    }

    public void setCategory(DishType category){
        this.category = category;
    }
}
