package com.example.lost_found_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private List<Report> reportList;
    private Context context;

    public ReportAdapter(List<Report> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTextView, obj, date,status;
        public ImageView img;

        public ReportViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            obj = itemView.findViewById(R.id.objectNameTextView);
            date = itemView.findViewById(R.id.dateTextView);
            status = itemView.findViewById(R.id.statusTextView);
            // Set OnClickListener for the item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        openDetailActivity(reportList.get(position));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_reports, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.userNameTextView.setText(report.getUsername());
        holder.obj.setText(MessageFormat.format("Object Name - {0}", report.getObjectName()));
        holder.status.setText(MessageFormat.format("Type - {0}", report.getReportType()));
        holder.date.setText(report.getReportDate());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    private void openDetailActivity(Report report) {
        Intent intent = new Intent(context, Item_details.class); // Replace with your detail activity class
        intent.putExtra("REPORT_EXTRA", report); // Pass the report object to the detail activity
        context.startActivity(intent);
    }

    public void setReportList(List<Report> reportList) {
        this.reportList = reportList;
        notifyDataSetChanged();
    }
}
