package com.manasmalla.ecofun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EcoFunLiscenceListAdapter extends BaseAdapter {

    Context context;
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> level = new ArrayList<>();
    private ArrayList<Integer> profile_image = new ArrayList<>();

    // If bool is true it is GP or else CO2P
    public EcoFunLiscenceListAdapter(Context context, ArrayList<String> name, ArrayList<String> description, ArrayList<Integer> profile_image) {
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.level = description;
        this.profile_image = profile_image;
        this.name = name;
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
            convertView = inflater.inflate(R.layout.simple_liscences_listview, parent, false);
            viewHolder.activityNameTextView = (TextView) convertView.findViewById(R.id.liscenceCreditTextViewTextView);
            viewHolder.activityLevelTextView = (TextView) convertView.findViewById(R.id.liscenceCreditDescriptionTextViewTextView);
            viewHolder.profile_icon_imageView = (ImageView) convertView.findViewById(R.id.liscenceCreditImageView);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.activityNameTextView.setText(name.get(position));
        viewHolder.activityLevelTextView.setText(level.get(position));
        viewHolder.profile_icon_imageView.setImageResource(profile_image.get(position));
        return convertView;
    }

    private static class ViewHolder {

        TextView activityNameTextView;
        TextView activityLevelTextView;
        ImageView profile_icon_imageView;
    }

}
