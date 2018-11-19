package com.example.lida.foodtracker.Retrofit;

import java.io.Serializable;

public class Recepie implements Serializable{
    private Integer id;
    private Integer category;
    private String podCategory;
    private String name;
    private String ingredients;
    private String instruction;

    public Recepie(Integer id, Integer category, String podCategory, String name, String ingredients, String instruction) {
        this.id = id;
        this.category = category;
        this.podCategory = podCategory;
        this.name = name;
        this.ingredients = ingredients;
        this.instruction = instruction;
    }

    public Recepie() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getPodCategory() {
        return podCategory;
    }

    public void setPodCategory(String podCategory) {
        this.podCategory = podCategory;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
