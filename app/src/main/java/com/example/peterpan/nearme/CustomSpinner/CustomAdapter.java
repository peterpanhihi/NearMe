package com.example.peterpan.nearme.CustomSpinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peterpan.nearme.R;

/**
 * Created by Peterpan on 5/11/2016 AD.
 */
public class CustomAdapter extends BaseAdapter {
    Context context;
    String types[];
    int icons[];
    LayoutInflater inflter;

    public CustomAdapter(Context context, String[] types, int[] icons) {
        this.context = context;
        this.types = types;
        this.icons = icons;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        int count =  types.length;
        return count > 0 ? count - 1 : count;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        TextView type = (TextView) view.findViewById(R.id.types_text);
        ImageView icon = (ImageView) view.findViewById(R.id.icon_type_img);
        type.setText(types[i]);
        icon.setImageResource(icons[i]);
        return view;
    }
}
