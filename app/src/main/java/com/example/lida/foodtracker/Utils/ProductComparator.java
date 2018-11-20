package com.example.lida.foodtracker.Utils;

import com.example.lida.foodtracker.Retrofit.Product;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class ProductComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        Integer p1p2 = p1.getDayToEnd().compareTo(p2.getDayToEnd());
        Long p1_ = p1.getDayToEnd();
        Long p2_ = p2.getDayToEnd();

        if (p1_ < 0) {
            if (p2_ >= 0) {
                return 1;
            }
            return p1p2;
        }
        if (p2_ < 0) {
            return -1;
        }
        return p1p2;
    }
}
