package com.example.lida.foodtracker.Utils;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductViewHolder {
    private TextView txtTitle;
    private TextView extraTxt;
    private ImageView imageView;

    public ProductViewHolder(TextView txtTitle, ImageView imageView, TextView extraTxt) {
        this.txtTitle = txtTitle;
        this.extraTxt = extraTxt;
        this.imageView = imageView;
    }

    public TextView getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(TextView txtTitle) {
        this.txtTitle = txtTitle;
    }

    public TextView getExtraTxt() {
        return extraTxt;
    }

    public void setExtraTxt(TextView extraTxt) {
        this.extraTxt = extraTxt;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
