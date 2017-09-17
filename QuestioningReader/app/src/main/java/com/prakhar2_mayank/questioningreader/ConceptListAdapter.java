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
    ArrayList<String> conceptList;

    /**
     * Constructor for setting the context and other objects used for
     * rendering the UI.
     */
    public ConceptListAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        conceptList = new ArrayList<String>();
    }

    /**
     * Number of concepts
     * @return Number of concepts
     */
    @Override
    public int getCount() {
        return conceptList.size();
    }

    @Override
    public Object getItem(int position) {
        return conceptList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Use the new list of concepts
     * @param uriList List of string of concepts
     */
    public void updateData(ArrayList<String> uriList) {
        // update the adapter's dataset
        this.conceptList = uriList;
        Log.d("RecentFilesAdapter", "file count: " + this.conceptList.size());
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

        //Set the current concept in this row.
        String fileUri = (String) getItem(position);

        holder.conceptName.setText(fileUri);
        holder.conceptIndex.setText("" + (position + 1));

        return convertView;
    }

    /**
     * Holder for the row view
     */
    private static class ViewHolder {
        public TextView conceptName;
        public TextView conceptIndex;
    }
}
