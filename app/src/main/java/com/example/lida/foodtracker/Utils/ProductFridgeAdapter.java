package com.example.lida.foodtracker.Utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lida.foodtracker.R;


public class ProductFridgeAdapter extends RecyclerView.Adapter<ProductFridgeAdapter.ProductFridgeViewHolder> {
    private String[] dataSet;
    public ProductFridgeAdapter(String[] myDataset) {
        this.dataSet = myDataset;
    }
    @NonNull
    @Override
    public ProductFridgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        //ProductFridgeViewHolder vh = new ProductFridgeViewHolder(v);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductFridgeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ProductFridgeViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ProductFridgeViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }
}
