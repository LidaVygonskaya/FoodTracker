package com.example.lida.foodtracker.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lida.foodtracker.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

public class ProductAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> productNames, descriptions;
    private final List<Integer> counts;
    private final List<Date> dates;
    private final List<Integer> imageIds;
    private LayoutInflater layoutInflater;

    public ProductAdapter(Context context, List<String> productNames, List<String> descriptions,
                          List<Integer> counts, List<Date> dates,
                          List<Integer> imageIds) {
        this.context = context;
        this.productNames = productNames;
        this.descriptions = descriptions;
        this.counts = counts;
        this.dates = dates;
        this.imageIds = imageIds;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return productNames.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.product_list_item, parent, false);
        }

        TextView txtTitle = (TextView) view.findViewById(R.id.item);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        TextView extratxt = (TextView) view.findViewById(R.id.textView1);

        txtTitle.setText(productNames.get(position));
        imageView.setImageResource(imageIds.get(position));
        extratxt.setText(descriptions.get(position) + "\nКол-во: " + counts.get(position) +
                "\nДо: " + dates.get(position).getDate() + "." + dates.get(position).getMonth() + "." + dates.get(position).getYear());
        return view;

    };

}
