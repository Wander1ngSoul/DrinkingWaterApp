package com.example.drinkingwaterapp;

public class User {
    public String firstName;
    public String lastName;
    public String patronymic;
    public String email;
    public String phone;
    public String password;
    public String id;

    public User() {
    }

    public User(String firstName, String lastName, String patronymic,
                String email, String phone, String password, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.id = id;
    }
}