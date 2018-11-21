package com.example.lida.foodtracker.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lida.foodtracker.R;
import com.example.lida.foodtracker.Retrofit.Promocode;

import java.util.List;

public class PromocodeAdapter extends ArrayAdapter<Promocode> {
    private final Context context;
    private final List<Promocode> promocodes;
    private LayoutInflater layoutInflater;


    public PromocodeAdapter(Context context, int textViewResourceId, List<Promocode> promocodes) {
        super(context, textViewResourceId, promocodes);
        this.context = context;
        this.promocodes = promocodes;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return promocodes.size();
    }

    @Override
    public Promocode getItem(int position) {
        return promocodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.promocode_list_item, parent, false);
            //TODO: Заменить layout
        }

        TextView txtTitle = (TextView) view.findViewById(R.id.item);
        TextView extraTxt = (TextView) view.findViewById(R.id.content);
        ImageView imageView = view.findViewById(R.id.icon);
        imageView.setImageResource(R.drawable.logo);

        Promocode promocode = promocodes.get(position);
        txtTitle.setText(promocode.getTitle());
        extraTxt.setText(promocode.getDesc());
        return view;

    }


}
