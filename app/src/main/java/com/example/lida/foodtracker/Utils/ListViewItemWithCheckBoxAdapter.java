package com.example.lida.foodtracker.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.lida.foodtracker.R;
import com.example.lida.foodtracker.Retrofit.Product;

import java.util.ArrayList;

public class ListViewItemWithCheckBoxAdapter extends BaseAdapter {
    private ArrayList<String> products;
    private Context ctx;
    private LayoutInflater layoutInflater;

    public ListViewItemWithCheckBoxAdapter(Context ctx, ArrayList<String> products) {
        this.ctx = ctx;
        this.products = products;
        this.layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
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
            view = layoutInflater.inflate(R.layout.checkbox_list_item, parent, false);
        }

        String p = getProduct(position);
        ((TextView) view.findViewById(R.id.product_name)).setText(p);

        CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);
        cbBuy.setOnCheckedChangeListener(myCheckChangeList);

        return view;
    }

    String getProduct(int position) {
        return ((String) getItem(position));
    }

    CompoundButton.OnCheckedChangeListener myCheckChangeList = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //TODO: реализовать что нужно делать с отмеченным продуктом
        }
    };
}
