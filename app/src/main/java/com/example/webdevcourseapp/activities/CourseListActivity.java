package com.example.webdevcourseapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.webdevcourseapp.R;
import com.example.webdevcourseapp.models.Course;
import com.example.webdevcourseapp.utils.CourseAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CourseListActivity extends AppCompatActivity {

    private Button addCourseButton, logOffButton;
    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private List<Course> courseList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        addCourseButton = findViewById(R.id.buttonAddCourse);
        recyclerView = findViewById(R.id.recyclerViewCourses);
        logOffButton = findViewById(R.id.buttonLogOff2);

        logOffButton.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent = new Intent(CourseListActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        db = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(courseList, this::deleteCourse, this::fetchCourses);
        recyclerView.setAdapter(adapter);

        addCourseButton.setOnClickListener(view -> {
            adapter.showAddCourseDialog(CourseListActivity.this, this::fetchCourses);
        });

        fetchCourses();
    }

    private void fetchCourses() {
        adapter.getIndexedQuery()
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        courseList.clear();
                        for (DocumentSnapshot doc : task.getResult()) {
                            Course course = doc.toObject(Course.class);
                            if (course != null) {
                                course.setId(doc.getId());
                                courseList.add(course);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Hiba a kurzusok lekérésekor!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteCourse(Course course) {
        if (course.getId() != null) {
            db.collection("courses").document(course.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Kurzus törölve!", Toast.LENGTH_SHORT).show();
                        fetchCourses();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Törlési hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}