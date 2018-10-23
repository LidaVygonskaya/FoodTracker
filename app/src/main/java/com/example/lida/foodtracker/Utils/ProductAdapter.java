package com.example.lida.foodtracker.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lida.foodtracker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ProductAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> productNames;
    private final Integer imageId;
    private LayoutInflater layoutInflater;

    /* Перепиши позже под массив картинок
    private final Integer[] imgid;
    */

    public ProductAdapter(Context context, List<String> productNames, Integer imageId) {
        super(context, R.layout.activity_main, productNames);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.productNames = productNames;
        this.imageId = imageId;
        this.layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.product_list_item, parent, false);
        }
        //LayoutInflater inflater = context.getLayoutInflater();
        //View rowView=inflater.inflate(R.layout.activity_main, null,true);

        TextView txtTitle = (TextView) view.findViewById(R.id.item);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        TextView extratxt = (TextView) view.findViewById(R.id.textView1);

        txtTitle.setText(productNames.get(position));
        imageView.setImageResource(imageId);
        extratxt.setText("Description " + productNames.get(position));
        return view;

    };

}
