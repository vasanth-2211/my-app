package com.example.my_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class dialog_table_layout extends AppCompatActivity {
    private Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_table_layout);
        b1=findViewById(R.id.closeButton);

        // Retrieve data from Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String dob = intent.getStringExtra("DOB");
        String gender = intent.getStringExtra("Gender");

        // Find TableLayout in your layout
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        // Create a new row
        TableRow tableRow = new TableRow(this);

        // Create TextViews for Name, DOB, and Gender
        TextView nameTextView = new TextView(this);
        nameTextView.setText(name);
        nameTextView.setPadding(8, 8, 8, 8);
        nameTextView.setGravity(android.view.Gravity.CENTER);

        TextView dobTextView = new TextView(this);
        dobTextView.setText(dob);
        dobTextView.setPadding(8, 8, 8, 8);
        dobTextView.setGravity(android.view.Gravity.CENTER);

        TextView genderTextView = new TextView(this);
        genderTextView.setText(gender);
        genderTextView.setPadding(8, 8, 8, 8);
        genderTextView.setGravity(android.view.Gravity.CENTER);

        // Add TextViews to the TableRow
        tableRow.addView(nameTextView);
        tableRow.addView(dobTextView);
        tableRow.addView(genderTextView);

        // Add TableRow to the TableLayout
        tableLayout.addView(tableRow);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(dialog_table_layout.this, home.class);
                startActivity(intent1);
            }
        });
    }

}