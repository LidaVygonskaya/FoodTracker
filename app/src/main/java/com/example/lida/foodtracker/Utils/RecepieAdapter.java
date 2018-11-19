package com.example.lida.foodtracker.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lida.foodtracker.R;
import com.example.lida.foodtracker.Retrofit.Category;
import com.example.lida.foodtracker.Retrofit.Recepie;

import java.util.Arrays;
import java.util.List;

public class RecepieAdapter extends ArrayAdapter<Recepie>{
    private final Context context;
    private final List<Recepie> recepies;
    private LayoutInflater layoutInflater;


    public RecepieAdapter(Context context, int textViewResourceId, List<Recepie> recepies) {
        super(context, textViewResourceId, recepies);
        this.context = context;
        this.recepies = recepies;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return recepies.size();
    }

    @Override
    public Recepie getItem(int position) {
        return recepies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.recipie_list_item, parent, false);

        }

        TextView txtTitle = (TextView) view.findViewById(R.id.item);
        TextView description = (TextView) view.findViewById(R.id.content);

        Recepie recepie = recepies.get(position);

        txtTitle.setText(convertToNormal(recepie.getName()));
        description.setText(convertToNormal(recepie.getPodCategory()));
        return view;

    }

    private String convertToNormal(String str) {
        String convertedString = "";
        convertedString = str.toLowerCase();
        char array [] = convertedString.toCharArray();
        array[0] = Character.toUpperCase(array[0]);
        convertedString = new String(array);
        convertedString = convertedString.replace("\\", "");
        return convertedString;
    }
}
