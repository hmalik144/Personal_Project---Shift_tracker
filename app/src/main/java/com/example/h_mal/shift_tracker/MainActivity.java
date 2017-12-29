package com.example.h_mal.shift_tracker;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.support.design.widget.FloatingActionButton;

import com.example.h_mal.shift_tracker.Data.ShiftsContract.ShiftsEntry;
import com.example.h_mal.shift_tracker.Data.ShiftsDbHelper;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ACHIEVEMENT_LOADER = 0;

    ShiftsCursorAdapter mCursorAdapter;
    ShiftsDbHelper shiftsDbhelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItem.class);
                startActivity(intent);
            }
        });

        ListView productListView = (ListView) findViewById(R.id.list_item_view);

        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);

        mCursorAdapter = new ShiftsCursorAdapter(this, null);
        productListView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(ACHIEVEMENT_LOADER, null, this);

    }

    private void insertProduct() {
        ContentValues values = new ContentValues();
        values.put(ShiftsEntry.COLUMN_SHIFT_DESCRIPTION, "Random Location");
        values.put(ShiftsEntry.COLUMN_SHIFT_DATE, "1970/01/01");
        values.put(ShiftsEntry.COLUMN_SHIFT_TIME_IN, "00:00");
        values.put(ShiftsEntry.COLUMN_SHIFT_TIME_OUT, "12:00");
        values.put(ShiftsEntry.COLUMN_SHIFT_BREAK, 30);
        values.put(ShiftsEntry.COLUMN_SHIFT_DURATION, 12);

        getContentResolver().insert(ShiftsEntry.CONTENT_URI, values);
    }


    private void deleteAllProducts() {
        int rowsDeleted = getContentResolver().delete(ShiftsEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void clickOnViewItem(long id) {
        Intent intent = new Intent(MainActivity.this, AddItem.class);
        Uri currentProductUri = ContentUris.withAppendedId(ShiftsEntry.CONTENT_URI, id);
        intent.setData(currentProductUri);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.delete_all:
                deleteAllProducts();
                return true;

            case R.id.insert_placeholder_data:
                insertProduct();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ShiftsEntry._ID,
                ShiftsEntry.COLUMN_SHIFT_DESCRIPTION,
                ShiftsEntry.COLUMN_SHIFT_DATE,
                ShiftsEntry.COLUMN_SHIFT_TIME_IN,
                ShiftsEntry.COLUMN_SHIFT_TIME_OUT,
                ShiftsEntry.COLUMN_SHIFT_BREAK,
                ShiftsEntry.COLUMN_SHIFT_DURATION};

        return new CursorLoader(this,
                ShiftsEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Leave?")
                .setMessage("Are you sure you want to exit Shifts?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                }).create().show();
    }

}
