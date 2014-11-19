package com.apps.rodolphe.cameranotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by rodolphe on 18/11/14.
 */
public class StableArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    public StableArrayAdapter(Context context, int textViewResourceId,
                              String[] values) {
        super(context, R.layout.row_layout, values);
        this.context = context;
        this.values = values;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.rowtext);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.rowpic);

        textView.setText("Image capturée le ...");
        imageView.setImageResource(R.drawable.test);

        return rowView;
    }
}