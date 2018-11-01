package com.example.lida.foodtracker.Retrofit;

import android.media.Image;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Recipe implements Serializable {

    private String name;

    private Map<String, Integer> ingredients;

    private String description;

    private List<Image> images;

    private Integer rating; // оценка сложности

    public Recipe() {}

    public Recipe(String name, Map<String, Integer> ingredients, String description, List<Image> images, Integer rating) {
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
        this.images = images;
        this.rating = rating;
    }

    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Integer> ingredients) { this.ingredients = ingredients; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) { this.images = images; }

    public Integer getRating() { return rating; }

    public void setRating(Integer rating) { this.rating = rating; }

}
