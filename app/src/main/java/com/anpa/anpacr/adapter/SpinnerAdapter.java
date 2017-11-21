package com.anpa.anpacr.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anpa.anpacr.R;
import com.anpa.anpacr.domain.GenericNameValue;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<GenericNameValue>{

    // Your sent context
    private Activity context;
    // Your custom values for the spinner (User)
    private ArrayList<GenericNameValue> values;

    public SpinnerAdapter(Activity context, int textViewResourceId,
    		ArrayList<GenericNameValue> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
       return values.size();
    }

    public GenericNameValue getItem(int position){
       return values.get(position);
    }

    public long getItemId(int position){
       return Long.valueOf(values.get(position).getValue());
    }


    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getName());
        label.setTextSize(16);

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
    	View row = convertView;
        if (row == null) {
        LayoutInflater inflater = context.getLayoutInflater();
        row = inflater.inflate(R.layout.spinner_item, parent, false);
       }
        TextView name = (TextView) row.findViewById(R.id.txt_spinner);
        name.setText(values.get(position).getName());

        return row;
    }
}
