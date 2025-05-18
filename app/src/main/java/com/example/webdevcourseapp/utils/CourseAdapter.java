package com.example.webdevcourseapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.webdevcourseapp.R;
import com.example.webdevcourseapp.models.Course;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courseList;
    private OnCourseDeleteListener deleteListener;
    private Runnable refreshCallback;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface OnCourseDeleteListener {
        void onDelete(Course course);
    }

    public CourseAdapter(List<Course> courseList, OnCourseDeleteListener deleteListener, Runnable refreshCallback) {
        this.courseList = courseList;
        this.deleteListener = deleteListener;
        this.refreshCallback = refreshCallback;
    }

    public void showAddCourseDialog(Context context, Runnable onCourseAdded) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_course, null);
        EditText editTitle = dialogView.findViewById(R.id.editCourseTitle);
        EditText editDesc = dialogView.findViewById(R.id.editCourseDescription);

        new AlertDialog.Builder(context)
                .setTitle("Új kurzus hozzáadása")
                .setView(dialogView)
                .setPositiveButton("Hozzáadás", (dialog, which) -> {
                    String newTitle = editTitle.getText().toString().trim();
                    String newDesc = editDesc.getText().toString().trim();

                    if (!newTitle.isEmpty() && !newDesc.isEmpty()) {
                        Course newCourse = new Course(null, newTitle, newDesc);
                        db.collection("courses")
                                .add(newCourse)
                                .addOnSuccessListener(documentReference -> onCourseAdded.run());
                    }
                })
                .setNegativeButton("Mégse", null)
                .show();
    }

    public Query getIndexedQuery() {
        return db.collection("courses")
                .whereGreaterThan("title", "A")
                .orderBy("title")
                .limit(10);
    }

    public Query getDescriptionFilteredQuery() {
        return db.collection("courses")
                .whereGreaterThan("description", "M")
                .orderBy("description");
    }

    public Query getLatestCoursesQuery() {
        return db.collection("courses")
                .orderBy("title", Query.Direction.DESCENDING)
                .limit(5);
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

        holder.itemView.setOnClickListener(v -> showEditDialog(holder.itemView.getContext(), course));
    }

    private void showEditDialog(Context context, Course course) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_course, null);
        EditText editTitle = dialogView.findViewById(R.id.editCourseTitle);
        EditText editDesc = dialogView.findViewById(R.id.editCourseDescription);

        editTitle.setText(course.getTitle());
        editDesc.setText(course.getDescription());

        new AlertDialog.Builder(context)
                .setTitle("Kurzus szerkesztése")
                .setView(dialogView)
                .setPositiveButton("Mentés", (dialog, which) -> {
                    String newTitle = editTitle.getText().toString().trim();
                    String newDesc = editDesc.getText().toString().trim();

                    if (!newTitle.isEmpty() && !newDesc.isEmpty() && course.getId() != null) {
                        db.collection("courses").document(course.getId())
                                .update("title", newTitle, "description", newDesc)
                                .addOnSuccessListener(aVoid -> {
                                    if (refreshCallback != null) refreshCallback.run();
                                });
                    }
                })
                .setNegativeButton("Mégse", null)
                .show();
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
