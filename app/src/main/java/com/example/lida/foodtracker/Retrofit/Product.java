package com.example.lida.foodtracker.Retrofit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;


public class Product implements Serializable{
    private static final int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;

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

    private Long dayToEnd;

    private boolean checked = false;

    public Product() {}

    public Product(String name, String description, Double count, String quantityChoise, Date date, Integer imgId) {
        this.name = name;
        this.description = description;
        this.quantity = count;
        this.quantityChoise = quantityChoise;
        this.dateEnd = date;
        this.imgId = imgId;
        this.checked = false;
    }

    public Product(String name, String description, Double count, String quantityChoise, Date date, Integer imgId, boolean checked) {
        this.name = name;
        this.description = description;
        this.quantity = count;
        this.quantityChoise = quantityChoise;
        this.dateEnd = date;
        this.imgId = imgId;
        this.checked = checked;
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
        if (this.composition != null) {
            return composition;
        } else {
            return "Упс! Не можем найти состав";
        }
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

    public Long getDayToEnd() {
        updateDayToEnd();
        return dayToEnd;
    }

    private void updateDayToEnd() {
        Calendar startDate = Calendar.getInstance();
        startDate.set(getDateEnd().getYear(), getDateEnd().getMonth(), getDateEnd().getDate());
        long start = startDate.getTimeInMillis();
        Calendar endDate = Calendar.getInstance();
        long end = endDate.getTimeInMillis();

        dayToEnd = (start - end) / MILLISECONDS_IN_DAY;
    }

    public boolean isChecked() { return checked; }

    public void setChecked(boolean checked) { this.checked = checked; }

    public void toggleChecked() { checked = !checked; }
}
