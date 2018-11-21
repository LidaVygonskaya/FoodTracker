package com.example.lida.foodtracker.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.lida.foodtracker.R;
import com.example.lida.foodtracker.Retrofit.Category;
import com.example.lida.foodtracker.Retrofit.Recepie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecepieAdapter extends ArrayAdapter<Recepie> implements Filterable{
    private final Context context;
    private List<Recepie> recepies;
    private List<Recepie> saveRecepies;
    private LayoutInflater layoutInflater;


    public RecepieAdapter(Context context, int textViewResourceId, List<Recepie> recepies) {
        super(context, textViewResourceId, recepies);
        this.context = context;
        this.recepies = recepies;
        this.saveRecepies = recepies;
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

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                recepies = saveRecepies;
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = recepies;
                    results.count = recepies.size();
                } else {
                    List<Recepie> filterResultsData = new ArrayList<Recepie>();
                    for (Recepie res: recepies) {
                        if (res.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filterResultsData.add(res);
                        }
                    }

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                recepies = (ArrayList<Recepie>)results.values;
                notifyDataSetChanged();
            }
        };
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
