package com.example.lida.foodtracker.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lida.foodtracker.R;
import com.example.lida.foodtracker.Retrofit.App;
import com.example.lida.foodtracker.Retrofit.Product;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends ArrayAdapter<Product> {
    private final Context context;

    private final List<Product> products;
    private LayoutInflater layoutInflater;

    private boolean isMultipleChoise = false;

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

        Product product = products.get(position);

        TextView txtTitle;
        ImageView imageView;
        TextView extraTxt;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.product_list_item, parent, false);

            txtTitle = (TextView) view.findViewById(R.id.item);
            imageView = (ImageView) view.findViewById(R.id.icon);
            extraTxt = (TextView) view.findViewById(R.id.content);

            view.setTag(new ProductViewHolder(txtTitle, imageView, extraTxt));

        } else {
            ProductViewHolder viewHolder = (ProductViewHolder) view.getTag();
            txtTitle = viewHolder.getTxtTitle();
            extraTxt = viewHolder.getExtraTxt();
            imageView = viewHolder.getImageView();
        }

        txtTitle.setText(product.getName());

        String productBarcode = product.getBarCode() + ".jpg";
        Call<ResponseBody> call = App.getApi().getImage(productBarcode);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        imageView.setImageBitmap(bitmap);
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

        Long days = product.getDayToEnd();
        String dayToEnd, count;
        if (days < 0) {
            dayToEnd = "<br/>Дней до конца срока годности: " + getColoredSpanned(days.toString(), "#800000");
        } else if (days > 10) {
            dayToEnd = "<br/>Дней до конца срока годности: " + getColoredSpanned(days.toString(), "#008000");
        } else {
            dayToEnd = "<br/>Дней до конца срока годности: " + getColoredSpanned(days.toString(), "#f28d18");
        }
        if (product.getQuantityChoise().equals("шт") || product.getQuantityChoise().equals("г") || product.getQuantityChoise().equals("мл")) {
            count = "Кол-во: " + product.getQuantity().intValue() + " " + product.getQuantityChoise();
        } else {
            count = "Кол-во: " + product.getQuantity() + " " + product.getQuantityChoise();
        }

        txtTitle.setText(product.getName());
        extraTxt.setText(Html.fromHtml(count + dayToEnd));
        return view;
    }

    private String getColoredSpanned(String text, String color) {
        return "<font color=" + color + "><b>" + text + "</b></font>";
    }

    public void setMultipleChoise(boolean isMultipleChoise) {
        this.isMultipleChoise = isMultipleChoise;
    }

    public boolean isMultipleChoise() {
        return isMultipleChoise;
    }
}
