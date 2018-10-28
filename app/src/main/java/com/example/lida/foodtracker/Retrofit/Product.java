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

    //@SerializedName("date_end")
    private Integer dayEnd;

    private Integer yearEnd;

    private Integer monthEnd;

    //@SerializedName("quantity")
    private Integer quantity;

    private Date dateEnd;

    private Integer imgId;

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

    public void setData(DatePicker datePicker) {
        this.dayEnd = datePicker.getDayOfMonth();
        this.monthEnd = datePicker.getMonth();
        this.yearEnd = datePicker.getYear();
    }

    public Map<String, Integer> getData() {
        Map<String, Integer> data = new HashMap<>();
        data.put("day", this.dayEnd);
        data.put("month", this.monthEnd);
        data.put("year", this.yearEnd);
        return data;
    }

    public void setDateEnd() {
        this.dateEnd = new Date(this.yearEnd, this.monthEnd, this.dayEnd);
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public void setImgId(Integer imgId) { this.imgId = imgId; }
}
