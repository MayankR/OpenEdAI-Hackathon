package com.prakhar2_mayank.questioningreader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mayank on 11/08/17.
 */
public class ConceptListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    ArrayList<String> uriList;

    public ConceptListAdapter(Context context, LayoutInflater inflater) {
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
            convertView = mInflater.inflate(R.layout.scan_concept_row, parent, false);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.conceptName = (TextView) convertView.findViewById(R.id.concept_text);
            holder.conceptIndex = (TextView) convertView.findViewById(R.id.concept_index);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }

        String fileUri = (String) getItem(position);

        holder.conceptName.setText(fileUri);
        holder.conceptIndex.setText("" + (position + 1));

        return convertView;
    }

    private static class ViewHolder {
        public TextView conceptName;
        public TextView conceptIndex;
    }
}
