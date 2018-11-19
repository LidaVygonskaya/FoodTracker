package com.example.lida.foodtracker.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lida.foodtracker.R;
import com.example.lida.foodtracker.Retrofit.Category;
import com.example.lida.foodtracker.Retrofit.Promocode;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category>{
    private final Context context;
    private final List<Category> categories;
    private LayoutInflater layoutInflater;


    public CategoryAdapter(Context context, int textViewResourceId, List<Category> categories) {
        super(context, textViewResourceId, categories);
        this.context = context;
        this.categories = categories;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.category_list_item, parent, false);

        }

        TextView txtTitle = (TextView) view.findViewById(R.id.item);

        Category category = categories.get(position);
        txtTitle.setText(category.getName());
        return view;

    }

}
