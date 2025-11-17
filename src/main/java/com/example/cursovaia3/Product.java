package com.example.cursovaia3;

public class Product extends BaseEntity{
    private String category;

    public Product(int number, String name, String category) {
        super(number, name);
        this.category = category;
    }
    public Product() {
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }



}