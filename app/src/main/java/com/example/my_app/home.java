package com.example.my_app;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.widget.Toast;

public class home extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private EditText nameEditText, dobEditText;
    private Button searchButton, InsertButton, DeleteButton,logoutbtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String firebaseUrl = "https://my-app-5754e-default-rtdb.firebaseio.com/";
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference("1gLJ8I_1O5SO7El_kklJqBipY-0DqTrJyQRQINbFH8SA/Sheet1");

        nameEditText = findViewById(R.id.nameEditText);
        dobEditText = findViewById(R.id.dobEditText);
        searchButton = findViewById(R.id.searchButton);
        InsertButton = findViewById(R.id.Insert);
        logoutbtn=findViewById(R.id.logout_btn);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getApplicationContext(), loginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDatabase();
            }
        });
        InsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });
    }
    private void searchDatabase() {
        final String inputName = nameEditText.getText().toString().trim();
        final String inputDOB = dobEditText.getText().toString().trim();

        // Reading data from the database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through the data
                for (DataSnapshot personSnapshot : dataSnapshot.getChildren()) {
                    String personName = personSnapshot.getKey();
                    String dob = personSnapshot.child("DOB").getValue(String.class);

                    // Check for a match with either name or DOB
                    if ((personName != null && personName.equalsIgnoreCase(inputName)) ||
                            (dob != null && dob.equals(inputDOB))) {

                        // Person found, print the data
                        String gender = personSnapshot.child("Gender").getValue(String.class);
                        Log.d("Firebase", "Name: " + personName + ", DOB: " + dob + ", Gender: " + gender);

                        // Show Toast or Dialog for the matched person
                        showMatchingRecordDialog(personSnapshot);

                        return; // Stop searching once a match is found
                    }
                }

                // If no match is found
                Log.d("Firebase", "No matching record found.");
                // You can display a Toast message indicating no matching record if needed
                Toast.makeText(home.this, "No matching record found.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("Firebase", "Error reading data: " + databaseError.getMessage());
            }
        });
    }
    private void showMatchingRecordDialog(DataSnapshot personSnapshot) {
        // Create a custom dialog using AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
        builder.setTitle("Match found");

        // Build the message with name, DOB, and gender
        StringBuilder message = new StringBuilder("Name: " + personSnapshot.getKey() +
                "\nDOB: " + personSnapshot.child("DOB").getValue(String.class) +
                "\nGender: " + personSnapshot.child("Gender").getValue(String.class));

        // Iterate through child nodes and add their values to the message
        for (DataSnapshot childSnapshot : personSnapshot.getChildren()) {
            String childKey = childSnapshot.getKey();
            String childValue = childSnapshot.getValue(String.class);
            if (!childKey.equals("DOB") && !childKey.equals("Gender")) {
                message.append("\n").append(childKey).append(": ").append(childValue);
            }
        }

        // Set the message to the dialog
        builder.setMessage(message.toString());

        // Add a button to close the dialog
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Close the dialog
                dialog.dismiss();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void insertData() {
        DatabaseReference newReference = databaseReference.push(); // Create a new entry with a unique key
        newReference.child("Name").setValue(nameEditText.getText().toString().trim());
        newReference.child("DOB").setValue(dobEditText.getText().toString().trim());

        // You may add more fields as needed, for example:
        // newReference.child("Gender").setValue("New Gender");

        Toast.makeText(home.this, "Data inserted successfully.", Toast.LENGTH_SHORT).show();
    }

}





