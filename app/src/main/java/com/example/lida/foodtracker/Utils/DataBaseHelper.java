package com.example.lida.foodtracker.Utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.lida.foodtracker.Retrofit.Category;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lida on 18.07.17.
 */

public class DataBaseHelper extends SQLiteAssetHelper {
    private static String DB_NAME = "foodtracker.db";
    private final String LOG_TAG = "dbLog";
    private SQLiteDatabase myDataBase;
    private static final String CATEGORIES_TABLE = "kuking_category";
    private static final String[] CATEGORIES_COLUMNS = {"id_category", "category_name"};
    private static final String[] RECEPIES_COLUMNS = {"id_recepts",
            "recept_category", "podcategory", "recept_sostav", "recept_instuction"};
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, 1);
        Log.d(LOG_TAG, "hello from database helper");
    }

/*
    public Cursor getInformation(String productName) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String sqlTables = "Calories";
        qb.setTables(sqlTables);
        String selection = "_id" + "=" + "'" + productName + "'";
        Cursor c = qb.query(db,COLUMNS, selection, null,null,null,null);
        c.moveToFirst();
        return c;
    }
    */

    public String[] getProductNames() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String sqlTables = "Calories";
        String[] columns = new String[] {"_id"};
        qb.setTables(sqlTables);
        Cursor c = qb.query(db, columns, null, null, null, null, null);
        c.moveToFirst();
        ArrayList<String> productNames = new ArrayList<String>();
        while (!c.isAfterLast()) {
            productNames.add(c.getString(c.getColumnIndex("_id")));
            c.moveToNext();
        }
        c.close();
        return productNames.toArray(new String[productNames.size()]);
    }



    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<Category>();
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(CATEGORIES_TABLE);
        Cursor c = qb.query(db, CATEGORIES_COLUMNS, null, null, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {

            Integer id = c.getInt(c.getColumnIndex(CATEGORIES_COLUMNS[0]));
            String name = c.getString(c.getColumnIndex(CATEGORIES_COLUMNS[1]));
            categories.add(new Category(id, name));
            c.moveToNext();
        }
        c.close();
        return categories;
    }
}