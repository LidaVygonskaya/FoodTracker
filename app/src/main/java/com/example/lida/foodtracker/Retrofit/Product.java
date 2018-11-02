package com.example.lida.foodtracker.Retrofit;

import android.widget.DatePicker;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.Month;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Product implements Serializable{
    @SerializedName("bar_code")
    private String barCode;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("composition")
    private String composition;

    private Double quantity;

    private String quantityChoise;

    private Date dateEnd;

    private Integer imgId;

    public Product() {}

    public Product(String name, String description, Double count, String quantityChoise, Date date, Integer imgId) {
        this.name = name;
        this.description = description;
        this.quantity = count;
        this.quantityChoise = quantityChoise;
        this.dateEnd = date;
        this.imgId = imgId;
    }

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

    public Integer getImgId() { return imgId; }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public void setDateEnd(Date dateEnd) { this.dateEnd = dateEnd; }

    public Date getDateEnd() { return dateEnd; }

    public Double getQuantity() { return quantity; }

    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public String getQuantityChoise() { return quantityChoise; }

    public void setQuantityChoise(String quantityChoise) { this.quantityChoise = quantityChoise; }

    public void setImgId(Integer imgId) { this.imgId = imgId; }

    public String getDateEndInStringFormat() {
        return dateEnd.getDate() + "." + dateEnd.getMonth() + "." + dateEnd.getYear();
    }
}
