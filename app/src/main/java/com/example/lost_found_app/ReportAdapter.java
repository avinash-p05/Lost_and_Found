package com.example.lost_found_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private List<Report> reportList;

    public ReportAdapter(List<Report> reportList) {
        this.reportList = reportList;
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTextView, loc, reportDescription, status, date, obj,contact;

        public ReportViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            loc = itemView.findViewById(R.id.location_report);
            reportDescription = itemView.findViewById(R.id.reportDes);
            status = itemView.findViewById(R.id.statusTextView);
            obj = itemView.findViewById(R.id.objectNameTextView);
            date = itemView.findViewById(R.id.dateTextView);
            contact = itemView.findViewById(R.id.contactTextView);
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
        holder.loc.setText(MessageFormat.format("Location - {0}",report.getLocation()));
        holder.reportDescription.setText(MessageFormat.format("Description - {0}",report.getReportDescription()));
        holder.status.setText(MessageFormat.format("Type - {0}",report.getReportType()));
        holder.contact.setText(MessageFormat.format("Contact - {0}",report.getContact()));
        holder.obj.setText(MessageFormat.format("Object Name - {0}",report.getObjectName()));
        holder.date.setText(report.getReportDate());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public void setReportList(List<Report> reportList) {
        this.reportList = reportList;
        notifyDataSetChanged();
    }
}
