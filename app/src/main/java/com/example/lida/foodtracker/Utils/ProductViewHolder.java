package com.example.lida.foodtracker.Utils;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductViewHolder {
   // private CheckBox checkBox;
    private TextView txtTitle;
    private TextView extraTxt;
    private ImageView imageView;

    public ProductViewHolder() {}

    public ProductViewHolder(TextView txtTitle, ImageView imageView, TextView extraTxt/*, CheckBox checkBox*/) {
     //   this.checkBox = checkBox;
        this.txtTitle = txtTitle;
        this.extraTxt = extraTxt;
        this.imageView = imageView;
    }

   /* public CheckBox getCheckBox() {
        checkBox.setVisibility(View.VISIBLE);
        checkBox.setFocusable(false);
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }*/    public TextView getTxtTitle() {
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
