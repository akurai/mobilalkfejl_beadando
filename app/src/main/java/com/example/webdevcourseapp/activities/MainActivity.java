package com.example.webdevcourseapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.webdevcourseapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private Button loginButton, registerButton, logOffButton;
    private TextView userText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.buttonLogin);
        registerButton = findViewById(R.id.buttonGoToRegister);
        logOffButton = findViewById(R.id.buttonLogOff);

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        logOffButton.setOnClickListener(view -> {
            mAuth.signOut();
            finish();
            startActivity(getIntent());
        });

        userText = findViewById(R.id.userText);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, CourseListActivity.class);
            startActivity(intent);
            finish();

            String email = user.getEmail();
            userText.setText("Bejelentkezve: " + email);
            loginButton.setVisibility(TextView.GONE);
            registerButton.setVisibility(TextView.GONE);
            logOffButton.setVisibility(TextView.VISIBLE);

            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            userText.startAnimation(fadeIn);
        } else {
            userText.setText("Nincs bejelentkezett felhasználó");
            loginButton.setVisibility(TextView.VISIBLE);
            registerButton.setVisibility(TextView.VISIBLE);
            logOffButton.setVisibility(TextView.GONE);
        }
    }
}
