package com.example.lida.foodtracker.Retrofit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable{
    @SerializedName("bar_code")
    private String barCode;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("composition")
    private String composition;

    @SerializedName("date_end")
    private Date dateEnd;

    @SerializedName("quantity")
    private Integer quantity;

    @Override
    public String toString() {
        return name;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public Date getDateEnd() { return dateEnd; }

    public void setDateEnd(Date date) { this.dateEnd = date; }

    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
