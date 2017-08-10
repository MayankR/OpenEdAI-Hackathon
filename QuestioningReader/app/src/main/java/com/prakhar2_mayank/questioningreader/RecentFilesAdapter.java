package com.prakhar2_mayank.questioningreader;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mayank on 10/08/17.
 */
public class RecentFilesAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    ArrayList<String> uriList;

    public RecentFilesAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        uriList = new ArrayList<String>();
    }

    @Override
    public int getCount() {
//        return 6;
        return uriList.size();
    }

    @Override
    public Object getItem(int position) {
        return uriList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateData(ArrayList<String> uriList) {
        // update the adapter's dataset
        this.uriList = uriList;
        Log.d("RecentFilesAdapter", "file count: " + this.uriList.size());
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.recent_file_row, parent, false);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.fileName = (TextView) convertView.findViewById(R.id.file_name);
            holder.fileIcon = (ImageView) convertView.findViewById(R.id.file_icon);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }

        String fileUri = (String) getItem(position);


        if(fileUri.endsWith("pdf")) {
            holder.fileIcon.setImageResource(R.drawable.pdf);
        }
        if(fileUri.length() > 25) {
            fileUri  = fileUri.substring(0, 24) + "...";
        }
        holder.fileName.setText(fileUri);

        return convertView;
    }

    private static class ViewHolder {
        public TextView fileName;
        public ImageView fileIcon;
    }
}
