package com.example.h_mal.shift_tracker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.h_mal.shift_tracker.Data.ShiftsContract.ShiftsEntry;

/**
 * Created by h_mal on 26/12/2017.
 */

public class ShiftsCursorAdapter extends CursorAdapter {

    private final MainActivity activity;

    private Context mContext;

    public ShiftsCursorAdapter(MainActivity context, Cursor c) {
        super(context, c, 0);
        this.activity = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mContext = context;

        TextView descriptionTextView = (TextView) view.findViewById(R.id.editText);
        TextView dateTextView = (TextView) view.findViewById(R.id.textView5);
        TextView timeTextView = (TextView) view.findViewById(R.id.textView5out);
        TextView durationTextView = (TextView) view.findViewById(R.id.textView7);

        final String descriptionColumnIndex = cursor.getString(cursor.getColumnIndexOrThrow(ShiftsEntry.COLUMN_SHIFT_DESCRIPTION));
        final String dateColumnIndex = cursor.getString(cursor.getColumnIndexOrThrow(ShiftsEntry.COLUMN_SHIFT_DATE));
        final String timeInColumnIndex = cursor.getString(cursor.getColumnIndexOrThrow(ShiftsEntry.COLUMN_SHIFT_TIME_IN));
        final String timeOutColumnIndex = cursor.getString(cursor.getColumnIndexOrThrow(ShiftsEntry.COLUMN_SHIFT_TIME_OUT));
        final String durationColumnIndex = cursor.getString(cursor.getColumnIndexOrThrow(ShiftsEntry.COLUMN_SHIFT_DURATION));
        final String breakOutColumnIndex = cursor.getString(cursor.getColumnIndexOrThrow(ShiftsEntry.COLUMN_SHIFT_BREAK));

        descriptionTextView.setText(descriptionColumnIndex);
        dateTextView.setText(dateColumnIndex);
        timeTextView.setText(timeInColumnIndex + "-" + timeOutColumnIndex);
        durationTextView.setText(durationColumnIndex + " Hours worked (+ "+ breakOutColumnIndex +" minutes break)");

        final long id = cursor.getLong(cursor.getColumnIndexOrThrow(ShiftsEntry._ID));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.clickOnViewItem(id);
            }
        });

    }

}
