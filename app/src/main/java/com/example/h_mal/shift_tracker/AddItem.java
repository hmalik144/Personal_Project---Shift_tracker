package com.example.h_mal.shift_tracker;

import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.h_mal.shift_tracker.Data.ShiftsContract.ShiftsEntry;

/**
 * Created by h_mal on 26/12/2017.
 */

public class AddItem extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private static final int EXISTING_PRODUCT_LOADER = 0;

    private Uri mCurrentProductUri;

    private EditText mLocationEditText;

    private EditText mDateEditText;

    private TextView mDurationTextView;

    private EditText mTimeInEditText;
    private EditText mTimeOutEditText;
    private EditText mBreakEditText;

    private static final int RESULT_LOAD_IMAGE = 1;

    private boolean mProductHasChanged = false;

    private static final int PICK_IMAGE_REQUEST = 0;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };
    private int mDay;
    private int mMonth;
    private int mYear;
    private int mHoursIn;
    private int mMinutesIn;
    private int mHoursOut;
    private int mMinutesOut;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mLocationEditText = (EditText) findViewById(R.id.locationEditText);
        mDateEditText = (EditText) findViewById(R.id.dateEditText);
        mTimeInEditText = (EditText) findViewById(R.id.timeInEditText);
        mBreakEditText = (EditText) findViewById(R.id.breakEditText);
        mTimeOutEditText = (EditText) findViewById(R.id.timeOutEditText);
        mDurationTextView = (TextView) findViewById(R.id.ShiftDuration);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if (mCurrentProductUri == null) {
            setTitle(getString(R.string.add_item_title));

            invalidateOptionsMenu();
        } else {

            setTitle(getString(R.string.edit_item_title));


            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        if (mDateEditText.getText().toString().equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            mDateEditText.setText(sdf.format(new Date()));
        }

        mBreakEditText.setText("0");

        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To show current date in the datepicker
                Calendar mcurrentDate=Calendar.getInstance();
                mYear=mcurrentDate.get(Calendar.YEAR);
                mMonth=mcurrentDate.get(Calendar.MONTH);
                mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(AddItem.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        mDateEditText.setText(String.format("%02d", selectedday) + "/" + String.format("%02d", (selectedmonth = selectedmonth + 1)) + "/" + selectedyear);
                        setDate(selectedyear, selectedmonth, selectedday);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();  }
        });

        mTimeInEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimeInEditText.getText().toString().equals("")) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    mHoursIn = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    mMinutesIn = mcurrentTime.get(Calendar.MINUTE);
                } else {
                    mHoursIn = Integer.parseInt((mTimeInEditText.getText().toString().subSequence(0,2)).toString());
                    mMinutesIn = Integer.parseInt((mTimeInEditText.getText().toString().subSequence(3,5)).toString());
                }
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddItem.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String ddTime = String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute);
                        setTime(selectedMinute, selectedHour);
                        mTimeInEditText.setText(ddTime);
                    }
                }, mHoursIn, mMinutesIn, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        mTimeOutEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimeOutEditText.getText().toString().equals("")) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    mHoursOut = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    mMinutesOut = mcurrentTime.get(Calendar.MINUTE);
                }else {
                    mHoursOut = Integer.parseInt((mTimeOutEditText.getText().toString().subSequence(0,2)).toString());
                    mMinutesOut = Integer.parseInt((mTimeOutEditText.getText().toString().subSequence(3,5)).toString());
                }
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddItem.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String ddTime = String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute);
                        setTime2(selectedMinute,selectedHour);
                        mTimeOutEditText.setText(ddTime);
                    }
                }, mHoursOut, mMinutesOut, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        mTimeInEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft )
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                setDuration();

            }
        });

        mTimeOutEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft )
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                setDuration();

            }
        });

        mBreakEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft )
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                setDuration();

            }
        });

        Button SubmitProduct = (Button) findViewById(R.id.submit);
        SubmitProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProduct();

            }
        });

        mLocationEditText.setOnTouchListener(mTouchListener);

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Discard changes?")
                .setMessage("Are you sure you want to discard changes?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        AddItem.super.onBackPressed();
                    }
                }).create().show();
    }

    private void saveProduct() {

        String descriptionString = mLocationEditText.getText().toString().trim();
        String dateString = mDateEditText.getText().toString().trim();
        String timeInString = mTimeInEditText.getText().toString().trim();
        String timeOutString = mTimeOutEditText.getText().toString().trim();
        String breakMins = mBreakEditText.getText().toString().trim();
        int breaks = Integer.parseInt(breakMins);
        float duration = calculateDuration(mHoursIn,mMinutesIn,mHoursOut,mMinutesOut,breaks);

        if (
                TextUtils.isEmpty(descriptionString)) {
            Toast.makeText(AddItem.this, "please insert all product data", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ShiftsEntry.COLUMN_SHIFT_DESCRIPTION, descriptionString);
        values.put(ShiftsEntry.COLUMN_SHIFT_DATE, dateString);
        values.put(ShiftsEntry.COLUMN_SHIFT_TIME_IN, timeInString);
        values.put(ShiftsEntry.COLUMN_SHIFT_TIME_OUT, timeOutString);
        values.put(ShiftsEntry.COLUMN_SHIFT_DURATION, duration);
        values.put(ShiftsEntry.COLUMN_SHIFT_BREAK, breaks);

        if (mCurrentProductUri == null) {

            Uri newUri = getContentResolver().insert(ShiftsEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getString(R.string.insert_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.update_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        NavUtils.navigateUpFromSameTask(AddItem.this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setDuration (){
        int breaks = 0;
        if (!mBreakEditText.getText().toString().equals("")){
            breaks = Integer.parseInt(mBreakEditText.getText().toString());
        }
        if (mTimeOutEditText.getText().toString().equals("")) {
            Calendar mcurrentTime = Calendar.getInstance();
            mHoursOut = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            mMinutesOut = mcurrentTime.get(Calendar.MINUTE);
        }else {
            mHoursOut = Integer.parseInt((mTimeOutEditText.getText().toString().subSequence(0,2)).toString());
            mMinutesOut = Integer.parseInt((mTimeOutEditText.getText().toString().subSequence(3,5)).toString());
        }
        if (mTimeInEditText.getText().toString().equals("")) {
            Calendar mcurrentTime = Calendar.getInstance();
            mHoursIn = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            mMinutesIn = mcurrentTime.get(Calendar.MINUTE);
        } else {
            mHoursIn = Integer.parseInt((mTimeInEditText.getText().toString().subSequence(0,2)).toString());
            mMinutesIn = Integer.parseInt((mTimeInEditText.getText().toString().subSequence(3,5)).toString());
        }
        mDurationTextView.setText(calculateDuration(mHoursIn,mMinutesIn,mHoursOut,mMinutesOut,breaks) + " hours");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.delete_all:
                DeleteDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void DeleteDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete");
        builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {

        if (mCurrentProductUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, "error deleting product", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }


    private void setDate (int year, int month, int day){
        mYear = year;
        mMonth = month;
        mDay = day;
    }


    private void setTime (int minutes, int hours){
        mMinutesIn = minutes;
        mHoursIn = hours;

    }

    private void setTime2 (int minutes, int hours){
        mMinutesOut = minutes;
        mHoursOut = hours;

    }

    private float calculateDuration (int hoursIn, int minutesIn, int hoursOut, int minutesOut, int breaks){
        float duration;
        if (hoursOut > hoursIn){
            duration = (((float)hoursOut + ((float)minutesOut/60)) - ((float) hoursIn + ((float)minutesIn/60))) - ((float)breaks / 60);
        }else{
            duration = ((((float)hoursOut + ((float)minutesOut/60)) - ((float)hoursIn + ((float)minutesIn/60)) - ((float)breaks / 60)) + 24);
        }

        String s = String.format("%.2f",duration);
        return Float.parseFloat(s);
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
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int descriptionColumnIndex = cursor.getColumnIndex(ShiftsEntry.COLUMN_SHIFT_DESCRIPTION);
            int dateColumnIndex = cursor.getColumnIndex(ShiftsEntry.COLUMN_SHIFT_DATE);
            int timeInColumnIndex = cursor.getColumnIndex(ShiftsEntry.COLUMN_SHIFT_TIME_IN);
            int timeOutColumnIndex = cursor.getColumnIndex(ShiftsEntry.COLUMN_SHIFT_TIME_OUT);
            int breakColumnIndex = cursor.getColumnIndex(ShiftsEntry.COLUMN_SHIFT_BREAK);
            int durationColumnIndex = cursor.getColumnIndex(ShiftsEntry.COLUMN_SHIFT_DURATION);

            String description = cursor.getString(descriptionColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String timeIn = cursor.getString(timeInColumnIndex);
            String timeOut = cursor.getString(timeOutColumnIndex);
            int breaks = cursor.getInt(breakColumnIndex);
            float duration = cursor.getFloat(durationColumnIndex);

            mLocationEditText.setText(description);
            mDateEditText.setText(date);
            mTimeInEditText.setText(timeIn);
            mTimeOutEditText.setText(timeOut);
            mBreakEditText.setText(Integer.toString(breaks));
            mDurationTextView.setText(Float.toString(duration) + " Hours");

        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
