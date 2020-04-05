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

public class EcoFunDashboardListAdapter extends BaseAdapter {

    Context context;
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> level = new ArrayList<>();
    private ArrayList<Bitmap> profile_image = new ArrayList<>();
    private ArrayList<String> value = new ArrayList<>();
    private ArrayList<String> character = new ArrayList<>();
    private ArrayList<Integer> char_image = new ArrayList<>();

    // If bool is true it is GP or else CO2P
    public EcoFunDashboardListAdapter(Context context, ArrayList<String> name, ArrayList<String> level, ArrayList<Bitmap> profile_image, ArrayList<String> value, ArrayList<String> character, ArrayList<Integer> char_image) {
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.value = value;
        this.level = level;
        this.profile_image = profile_image;
        this.char_image = char_image;
        this.character = character;
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
            convertView = inflater.inflate(R.layout.dashboard_ecofun_list_view, parent, false);
            viewHolder.activityNameTextView = (TextView) convertView.findViewById(R.id.userNameTextView);
            viewHolder.activityLevelTextView = (TextView) convertView.findViewById(R.id.levelDashboardTextView);
            viewHolder.activityValueTextView = (TextView) convertView.findViewById(R.id.userGPValueTextView);
            viewHolder.activityCharacterTextView = (TextView) convertView.findViewById(R.id.userCharTextView);
            viewHolder.profile_icon_imageView = (CircleImageView) convertView.findViewById(R.id.profileIconImageView);
            viewHolder.char_imageView = (ImageView) convertView.findViewById(R.id.charIconImageView);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.activityNameTextView.setText(name.get(position));
        viewHolder.activityLevelTextView.setText(level.get(position));
        viewHolder.activityValueTextView.setText(value.get(position));
        viewHolder.profile_icon_imageView.setImageBitmap(profile_image.get(position));
        viewHolder.activityCharacterTextView.setText(character.get(position));
        viewHolder.char_imageView.setImageResource(char_image.get(position));
        return convertView;
    }

    private static class ViewHolder {

        TextView activityNameTextView;
        TextView activityLevelTextView;
        CircleImageView profile_icon_imageView;
        ImageView char_imageView;
        TextView activityValueTextView;
        TextView activityCharacterTextView;

    }

}
