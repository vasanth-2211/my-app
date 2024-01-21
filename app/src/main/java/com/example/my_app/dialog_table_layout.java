package com.example.my_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class dialog_table_layout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_table_layout);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("DataSnapShot")) {
            // Get the data from the Intent
            String dataFromIntent = intent.getStringExtra("DataSnapShot");

            // Now you can use the data as needed, for example, set it in a TextView
            TextView textView = findViewById(R.id.textView);
            textView.setText();
        }
    }
}