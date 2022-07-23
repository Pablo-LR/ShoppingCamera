package com.example.project.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseImage extends DatabaseHelper {

    Context context;

    public DatabaseImage(@Nullable  Context context) {
        super(context);
        this.context = context;
    }

    public long insertImage(String path, String name, String description, int price) {
        long id = 0;

        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("PATH", path);
            values.put("NAME", name);
            values.put("DESCRIPTION", description);
            values.put("PRICE", price);

            id = db.insert(TABLE_NAME, null, values);

            db.close();
        } catch (Exception e) {
            e.toString();
        }
        return id;
    }

    public ArrayList<ImageElement> getAllImages() {

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ArrayList<ImageElement> imagesArray = new ArrayList<>();
        ImageElement imageElement = null;
        Cursor cursorImages = null;

        cursorImages = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if(cursorImages.moveToFirst()) {
            do {
                imageElement = new ImageElement();
                imageElement.setId(cursorImages.getInt(0));
                imageElement.setPath(cursorImages.getString(1));
                imageElement.setName(cursorImages.getString(2));
                imageElement.setDescription(cursorImages.getString(3));
                imageElement.setPrice(cursorImages.getInt(4));

                imagesArray.add(imageElement);
            } while (cursorImages.moveToNext());
        }

        cursorImages.close();
        db.close();
        return imagesArray;
    }

    public ImageElement getImage(String path) {

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ImageElement imageElement = new ImageElement();
        Cursor cursorImages = null;

        cursorImages = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE PATH=?", new String[]{path + ""});

        if(cursorImages.getCount() > 0) {
            cursorImages.moveToFirst();
            imageElement.setId(cursorImages.getInt(0));
            imageElement.setPath(cursorImages.getString(1));
            imageElement.setName(cursorImages.getString(2));
            imageElement.setDescription(cursorImages.getString(3));
            imageElement.setPrice(cursorImages.getInt(4));
        }
        cursorImages.close();
        db.close();
        return imageElement;
    }

    public boolean updateImage(String path, String name, String description, int price) {

        boolean done = false;

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        try {
            db.execSQL("UPDATE " + TABLE_NAME + " SET NAME = '" + name + "', DESCRIPTION = '" + description + "', PRICE = '" + price + "' WHERE path = '" + path + "' ");
            done = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return done;
    }

    public void deleteImage(String path) {
        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE PATH=?", new String[]{path + ""});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
