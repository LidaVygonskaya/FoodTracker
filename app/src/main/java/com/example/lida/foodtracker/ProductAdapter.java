package com.example.lida.foodtracker;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    List<String> arrOne;
    Context context;
    List<String> arrTwo;
    private static LayoutInflater inflater=null;

   /* public ProductAdapter(MainActivity mainActivity, List<String> arrOne, List<String> arrTwo) {
        this.arrOne=arrOne;
        context=mainActivity;
        this.arrTwo=arrTwo;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }*/
    public ProductAdapter(Activity mainActivity, List<String> arrOne, List<String> arrTwo) {
        this.arrOne=arrOne;
        context=mainActivity;
        this.arrTwo=arrTwo;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return arrOne.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder
    {
        TextView tv;
        TextView tv2;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_item, null);
        holder.tv=(TextView) rowView.findViewById(R.id.text1);
        holder.tv2=(TextView) rowView.findViewById(R.id.count);
        holder.tv.setText(arrOne.get(position));
        holder.tv2.setText(arrTwo.get(position));
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                //Toast.makeText(context, "You Clicked "+arrOne[position], Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }
}
