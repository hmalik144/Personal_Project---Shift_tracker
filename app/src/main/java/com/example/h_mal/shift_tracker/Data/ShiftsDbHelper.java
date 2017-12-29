package com.example.h_mal.shift_tracker.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.h_mal.shift_tracker.Data.ShiftsContract.ShiftsEntry;

/**
 * Created by h_mal on 26/12/2017.
 */

public class ShiftsDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ShiftsDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "shifts.db";

    private static final int DATABASE_VERSION = 1;


    public ShiftsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_PRODUCTS_TABLE =  "CREATE TABLE " + ShiftsEntry.TABLE_NAME + " ("
                + ShiftsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ShiftsEntry.COLUMN_SHIFT_DESCRIPTION + " TEXT NOT NULL, "
                + ShiftsEntry.COLUMN_SHIFT_DATE + " DATE NOT NULL, "
                + ShiftsEntry.COLUMN_SHIFT_TIME_IN + " TIME NOT NULL, "
                + ShiftsEntry.COLUMN_SHIFT_TIME_OUT + " TIME NOT NULL, "
                + ShiftsEntry.COLUMN_SHIFT_BREAK + " INTEGER NOT NULL DEFAULT 0, "
                + ShiftsEntry.COLUMN_SHIFT_DURATION + " FLOAT NOT NULL DEFAULT 0)";

        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
