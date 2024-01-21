package com.example.my_app;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import android.widget.LinearLayout;
import android.widget.TextView;
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
        DeleteButton = findViewById(R.id.Delete);

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
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
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
                        Intent intent = new Intent(home.this, dialog_table_layout.class);

                        // You can also pass data to the second activity using putExtra
                        intent.putExtra("DataSnapShot",personSnapshot.getValue().toString());

                        // Start the SecondActivity
                        startActivity(intent);

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

        // Create a linear layout for the dialog content
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Create horizontal linear layout for headers
        LinearLayout headerLayout = new LinearLayout(this);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Create TextViews for headers
        TextView nameLabel = new TextView(this);
        nameLabel.setText("Name");
        nameLabel.setPadding(20, 5, 20, 5);

        TextView dobLabel = new TextView(this);
        dobLabel.setText("DOB");
        dobLabel.setPadding(20, 5, 20, 5);

        TextView genderLabel = new TextView(this);
        genderLabel.setText("Gender");
        genderLabel.setPadding(20, 5, 20, 5);

        // Add headers to the header layout
        headerLayout.addView(nameLabel);
        headerLayout.addView(dobLabel);
        headerLayout.addView(genderLabel);

        // Add header layout to the main layout
        linearLayout.addView(headerLayout);

        // Create horizontal linear layout for data
        LinearLayout dataLayout = new LinearLayout(this);
        dataLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Build the message with name, DOB, and gender
        String name = personSnapshot.getKey();
        String dob = personSnapshot.child("DOB").getValue(String.class);
        String gender = personSnapshot.child("Gender").getValue(String.class);

        // Create TextViews for data
        TextView nameValue = new TextView(this);
        nameValue.setText(name);
        nameValue.setPadding(20, 5, 20, 5);

        TextView dobValue = new TextView(this);
        dobValue.setText(dob);
        dobValue.setPadding(20, 5, 20, 5);

        TextView genderValue = new TextView(this);
        genderValue.setText(gender);
        genderValue.setPadding(20, 5, 20, 5);

        // Add data to the data layout
        dataLayout.addView(nameValue);
        dataLayout.addView(dobValue);
        dataLayout.addView(genderValue);

        // Add data layout to the main layout
        linearLayout.addView(dataLayout);

        // Iterate through child nodes and add their values to the message
        for (DataSnapshot childSnapshot : personSnapshot.getChildren()) {
            String childKey = childSnapshot.getKey();
            String childValue = childSnapshot.getValue(String.class);
            if (!childKey.equals("DOB") && !childKey.equals("Gender")) {
                // Create horizontal linear layout for additional data
                LinearLayout additionalDataLayout = new LinearLayout(this);
                additionalDataLayout.setOrientation(LinearLayout.HORIZONTAL);

                // Create TextViews for additional data
                TextView keyLabel = new TextView(this);
                keyLabel.setText(childKey);
                keyLabel.setPadding(20, 5, 20, 5);

                TextView valueLabel = new TextView(this);
                valueLabel.setText(childValue);
                valueLabel.setPadding(20, 5, 20, 5);

                // Add additional data to the layout
                additionalDataLayout.addView(keyLabel);
                additionalDataLayout.addView(valueLabel);

                // Add additional data layout to the main layout
                linearLayout.addView(additionalDataLayout);
            }
        }

        // Add a button to close the dialog
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Close the dialog
                dialog.dismiss();
            }
        });

        // Set the custom layout to the dialog
        builder.setView(linearLayout);

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
    private void deleteData() {
        final String inputName = nameEditText.getText().toString().trim();
        final String inputDOB = dobEditText.getText().toString().trim();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot personSnapshot : dataSnapshot.getChildren()) {
                    String personName = personSnapshot.child("Name").getValue(String.class);
                    String dob = personSnapshot.child("DOB").getValue(String.class);

                    if (personName != null && personName.equalsIgnoreCase(inputName)
                            && dob != null && dob.equals(inputDOB)) {
                        // Match found, delete the entry
                        personSnapshot.getRef().removeValue();
                        Toast.makeText(home.this, "Data deleted successfully.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // No matching record found for deletion
                Toast.makeText(home.this, "No matching record found for deletion.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error reading data: " + databaseError.getMessage());
            }
        });
    }
    private void readDataFromFirebase() {
        // Add a ValueEventListener to the database reference
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if there is any data
                if (dataSnapshot.exists()) {
                    // Build a string with the data
                    StringBuilder dataStringBuilder = new StringBuilder();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        dataStringBuilder.append("Key: ").append(snapshot.getKey()).append("\n");
                        for (DataSnapshot column : snapshot.getChildren()) {
                            dataStringBuilder.append(column.getKey()).append(": ").append(column.getValue()).append("\n");
                        }
                        dataStringBuilder.append("\n");
                    }

                    // Display data in AlertDialog
                    showDataInAlertDialog(dataStringBuilder.toString());
                } else {
                    showAlertDialog("No data found in the database.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error reading data from Firebase Database: " + databaseError.getMessage());
            }
        });
    }

    private void showDataInAlertDialog(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Firebase Data");
        builder.setMessage(data);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Firebase Data");
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

}







