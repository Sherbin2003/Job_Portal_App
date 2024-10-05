package com.example.finale;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostedJobsAdapter extends RecyclerView.Adapter<PostedJobsAdapter.ViewHolder> {

    private List<Job> jobPosts;
    private OnDeleteClickListener onDeleteClickListener; // Interface for delete button click

    public PostedJobsAdapter(List<Job> jobPosts, OnDeleteClickListener onDeleteClickListener) {
        this.jobPosts = jobPosts;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Job job = jobPosts.get(position);
        holder.bind(job);
    }

    @Override
    public int getItemCount() {
        return jobPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView companyNameTextView;
        private Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewJobTitle);
            companyNameTextView = itemView.findViewById(R.id.textViewCompanyName);
            deleteButton = itemView.findViewById(R.id.buttonDelete);

            // Set click listener for delete button
            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(position);
                }
            });
        }

        public void bind(Job job) {
            titleTextView.setText(job.getJobTitle());
            companyNameTextView.setText(job.getCompanyName());
        }
    }

    // Interface for delete button click
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
}


