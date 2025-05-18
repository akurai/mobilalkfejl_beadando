package com.example.webdevcourseapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.webdevcourseapp.models.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courseList;
    private OnCourseDeleteListener deleteListener;

    public interface OnCourseDeleteListener {
        void onDelete(Course course);
    }

    public CourseAdapter(List<Course> courseList, OnCourseDeleteListener deleteListener) {
        this.courseList = courseList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.titleText.setText(course.getTitle());
        holder.descText.setText(course.getDescription());
        holder.deleteIcon.setOnClickListener(v -> deleteListener.onDelete(course));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView descText;
        ImageView deleteIcon;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.textCourseTitle);
            descText = itemView.findViewById(R.id.textCourseDescription);
            deleteIcon = itemView.findViewById(R.id.iconDelete);
        }
    }
}
