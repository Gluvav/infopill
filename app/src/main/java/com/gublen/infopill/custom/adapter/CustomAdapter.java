package com.gublen.infopill.custom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gublen.infopill.R;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Pill> {

    private Context context;
    private List<Pill> pillList;

    public CustomAdapter(Context context, List<Pill> pillList) {
        super(context, 0, pillList);
        this.context = context;
        this.pillList = pillList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        Pill item = pillList.get(position);

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView subitemTextView = convertView.findViewById(R.id.subtitleTextView);

        titleTextView.setText(item.getTitle());
        subitemTextView.setText(item.getSubitem());

        return convertView;
    }
}
