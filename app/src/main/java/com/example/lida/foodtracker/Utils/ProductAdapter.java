package com.example.lida.foodtracker.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.CalendarContract;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lida.foodtracker.R;
import com.example.lida.foodtracker.Retrofit.App;
import com.example.lida.foodtracker.Retrofit.Product;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        TextView extraTxt = (TextView) view.findViewById(R.id.content);

        Product product = products.get(position);
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
                //imageView.setImageResource(R.drawable.carrot);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                imageView.setImageResource(R.drawable.carrot);
            }
        });
        //imageView.setImageResource(product.getImgId());

        Calendar startDate = Calendar.getInstance();
        startDate.set(product.getDateEnd().getYear(), product.getDateEnd().getMonth(), product.getDateEnd().getDate());
        long start = startDate.getTimeInMillis();
        Calendar endDate = Calendar.getInstance();
        long end = endDate.getTimeInMillis();

        Long days = (long) (start - end) / (1000 * 60 * 60 * 24);
        String dayToEnd, count;
        if (days < 0) {
            dayToEnd = "<br/>Дней до конца срока годности: " + getColoredSpanned(days.toString(), "#800000");
        } else if (days > 5) {
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

    private String getColored(String text, String color) {
        return "<font color=" + color + ">" + text + "</font>";
    }
}
