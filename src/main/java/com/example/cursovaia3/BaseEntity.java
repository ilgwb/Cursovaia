package com.example.cursovaia3;

public class BaseEntity {
    private int number;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public BaseEntity(int number, String name) {
        this.number = number;
        this.name = name;
    }
    public BaseEntity() {
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }



}
