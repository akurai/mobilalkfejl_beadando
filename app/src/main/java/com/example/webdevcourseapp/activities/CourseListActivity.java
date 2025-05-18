package com.example.webdevcourseapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.example.webdevcourseapp.R;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class CourseListActivity extends AppCompatActivity {

    private Button addCourseButton, logOffButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        addCourseButton = findViewById(R.id.buttonAddCourse);
        logOffButton = findViewById(R.id.buttonLogOff2);

        addCourseButton.setOnClickListener(view -> {
            Toast.makeText(this, "Kurzus hozzáadása később jön.", Toast.LENGTH_SHORT).show();
        });

        logOffButton.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent = new Intent(CourseListActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
