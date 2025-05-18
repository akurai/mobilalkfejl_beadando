package com.example.webdevcourseapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.webdevcourseapp.models.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courseList;

    public CourseAdapter(List<Course> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.titleText.setText(course.getTitle());
        holder.descText.setText(course.getDescription());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView descText;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(android.R.id.text1);
            descText = itemView.findViewById(android.R.id.text2);
        }
    }
}
