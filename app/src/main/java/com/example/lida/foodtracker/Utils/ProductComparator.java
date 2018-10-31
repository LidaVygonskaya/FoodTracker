package com.example.lida.foodtracker.Utils;

import com.example.lida.foodtracker.Retrofit.Product;

import java.util.Comparator;

public class ProductComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        if (p1.getDateEnd().getYear() < p2.getDateEnd().getYear()) {
            return 1;
        } else if (p1.getDateEnd().getYear() > p2.getDateEnd().getYear()) {
            return -1;
        }

        if (p1.getDateEnd().getMonth() < p2.getDateEnd().getMonth()) {
            return 1;
        } else if (p1.getDateEnd().getMonth() > p2.getDateEnd().getMonth()) {
            return -1;
        }

        if (p1.getDateEnd().getDay() < p2.getDateEnd().getDay()) {
            return 1;
        } else if (p1.getDateEnd().getDay() > p2.getDateEnd().getDay()) {
            return -1;
        }

        return 0;
    }
}
