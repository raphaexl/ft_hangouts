package com.example.ft_hangouts;

public class Contact {
    private String name;
    private String surname;
    private String tel;
    private String email;
    private String about;
    private String address;

    Contact(String name, String surname, String tel, String email, String about, String address){
        this.name = name;
        this.surname = surname;
        this.tel = tel;
        this.email = email;
        this.about = about;
        this.address = address;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
