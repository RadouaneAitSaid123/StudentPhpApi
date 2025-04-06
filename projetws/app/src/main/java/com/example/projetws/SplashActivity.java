package com.example.projetws;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        TextView getStartedButton = findViewById(R.id.button);
        getStartedButton.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, AddEtudiant.class);
            startActivity(intent);
        });
    }
}
