package com.manasmalla.ecofun;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EcoFunListAdapter extends BaseAdapter {

    Context context;
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> distance = new ArrayList<>();
    private ArrayList<Integer> activity_image = new ArrayList<>();
    private ArrayList<String> value = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();

    // If bool is true it is GP or else CO2P
    public EcoFunListAdapter(Context context, ArrayList<String> name, ArrayList<String> distance, ArrayList<Integer> activity_image, ArrayList<String> value, ArrayList<String> date){
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.value = value;
        this.distance = distance;
        this.date = date;
        this.name = name;
        this.activity_image = activity_image;
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.simple_ecofun_list_view, parent, false);
            viewHolder.activityNameTextView = (TextView) convertView.findViewById(R.id.activityNameTextView);
            viewHolder.activityDistanceTextView = (TextView) convertView.findViewById(R.id.activityDistanceTextView);
            viewHolder.activityValueTextView = (TextView) convertView.findViewById(R.id.activityValueTextView);
            viewHolder.activity_icon_imageView = (ImageView) convertView.findViewById(R.id.activityIconImageView);
            viewHolder.activityDateTextView = (TextView) convertView.findViewById(R.id.activityDateTextView);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.activityNameTextView.setText(name.get(position));
        viewHolder.activityDistanceTextView.setText(distance.get(position));
        viewHolder.activityValueTextView.setText(value.get(position));
        viewHolder.activity_icon_imageView.setImageResource(activity_image.get(position));
        viewHolder.activityDateTextView.setText(date.get(position));
        return convertView;
    }

    private static class ViewHolder {

        TextView activityNameTextView;
        TextView activityDistanceTextView;
        ImageView activity_icon_imageView;
        TextView activityValueTextView;
        TextView activityDateTextView;

    }

}
