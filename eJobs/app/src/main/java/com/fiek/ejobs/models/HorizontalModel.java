package com.fiek.ejobs.models;

public class HorizontalModel {
    private String name;
    private int professionImg;

    public HorizontalModel() {
    }

    public HorizontalModel(String name, int professionImg) {
        this.name = name;
        this.professionImg = professionImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProfessionImg() {
        return professionImg;
    }

    public void setProfessionImg(int professionImg) {
        this.professionImg = professionImg;
    }
}
