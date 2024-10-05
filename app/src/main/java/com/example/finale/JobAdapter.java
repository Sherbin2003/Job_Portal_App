package com.example.finale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class JobAdapter extends ArrayAdapter<Job> {

    public JobAdapter(Context context, ArrayList<Job> jobs) {
        super(context, 0, jobs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Job job = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_job, parent, false);
        }

        // Lookup view for data population
        TextView jobTitle = convertView.findViewById(R.id.jobTitle);
        TextView companyName = convertView.findViewById(R.id.companyName);

        // Populate the data into the template view using the data object
        if (job != null) {
            jobTitle.setText(job.getJobTitle());
            companyName.setText(job.getCompanyName());
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
