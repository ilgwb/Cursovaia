package com.example.cursovaia3;

import java.util.Objects;

public class Product extends BaseEntity{
    private String category;

    public Product(int number, String name, String category) {
        super(number, name);
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(getNumber(), product.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(category);
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