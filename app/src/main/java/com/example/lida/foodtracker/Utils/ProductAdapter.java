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
import com.example.lida.foodtracker.Retrofit.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

public class ProductAdapter extends ArrayAdapter<Product> {
    private final Context context;

    private final List<Product> products;
    private LayoutInflater layoutInflater;

    public ProductAdapter(Context context, int textViewResourceId, List<Product> products) {
        super(context, textViewResourceId, products);
        this.context = context;
        this.products = products;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
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

        Product product = products.get(position);
        txtTitle.setText(product.getName());
        imageView.setImageResource(product.getImgId());
        extratxt.setText(product.getDescription() + "\nКол-во: " + product.getQuantity() +
                        "\nДо: " + product.getDateEnd().getDate() + "." + product.getDateEnd().getMonth() + "." + product.getDateEnd().getYear());
        return view;
    }
}
