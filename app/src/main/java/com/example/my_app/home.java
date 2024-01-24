package com.example.my_app;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
public class home extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private EditText nameEditText, dobEditText, startYearEditText,result,endYearEditText;
    private Button searchButton, InsertButton, DeleteButton, logoutbtn, ShowAlltheRecord;

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
        logoutbtn = findViewById(R.id.logout_btn);
        DeleteButton = findViewById(R.id.Delete);
        ShowAlltheRecord = findViewById(R.id.button);
        startYearEditText = findViewById(R.id.startYearEditText);
        endYearEditText = findViewById(R.id.endYearEditText);
        result = findViewById(R.id.result);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), loginActivity.class);
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
        ShowAlltheRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RangeSearch();
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
                        Intent intent = new Intent(home.this, dialog_table_layout.class);
                        intent.putExtra("name", personName);
                        intent.putExtra("DOB", dob);
                        intent.putExtra("Gender", gender);
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

    private void displayRecordsInTableLayout(List<String> records) {
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        for (String record : records) {
            // Create a new row
            TableRow tableRow = new TableRow(this);

            // Create TextView for the record
            TextView recordTextView = new TextView(this);
            recordTextView.setText(record);
            recordTextView.setPadding(8, 8, 8, 8);
            recordTextView.setGravity(Gravity.CENTER);

            // Add TextView to the TableRow
            tableRow.addView(recordTextView);

            // Add TableRow to the TableLayout
            tableLayout.addView(tableRow);
        }
    }

    private void readAllRecordsFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if there is any data
                if (dataSnapshot.exists()) {
                    // Iterate through the data
                    List<String> records = new ArrayList<>();
                    for (DataSnapshot personSnapshot : dataSnapshot.getChildren()) {
                        StringBuilder record = new StringBuilder();
                        for (DataSnapshot column : personSnapshot.getChildren()) {
                            record.append(column.getKey()).append(": ").append(column.getValue()).append("\n");
                        }
                        records.add(record.toString());
                    }

                    // Display all records in TableLayout
                    displayRecordsInTableLayout(records);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });





    }
    private class Record {
        private String name;
        private String dob;
        private String gender;

        // Constructors, getters, and setters

        // Example constructor
        public Record(String name, String dob, String gender) {
            this.name = name;
            this.dob = dob;
            this.gender = gender;
        }
        public String getName() {
            return name;
        }

        public String getDob() {
            return dob;
        }

        public String getGender() {
            return gender;
        }
    }
    public  void RangeSearch(){
        String Date1 =startYearEditText.getText().toString();
        String Date2 = endYearEditText.getText().toString();
        if (Date1.length() > 4 || Date2.length() > 4 ){
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Record> recordList = new ArrayList<>();

                    // Iterate through the data
                    for (DataSnapshot personSnapshot : dataSnapshot.getChildren()) {
                        String name = personSnapshot.child("Name").getValue(String.class);
                        String dob = personSnapshot.child("DOB").getValue(String.class);

                        // Check if the DOB is within the specified date range
                        if (isDateInRange(dob, Date1, Date2)) {
                            String gender = personSnapshot.child("Gender").getValue(String.class);

                            // Create a Record object and add it to the list
                            Record record = new Record(name, dob, gender);
                            recordList.add(record);
                        }
                    }

                    // Display records in the table
                    Intent intent = new Intent(home.this, ShowAllRecords.class);

// Put the record list as an extra in the Intent
                    intent.putExtra("recordList", (Serializable) recordList);

// Start the second activity
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error reading data: " + databaseError.getMessage());
                }
            });
        }
}
    private void displayRecordsInTable(List<Record> recordList) {
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        // Add headers row
        TableRow headersRow = new TableRow(this);

        // Create TextViews for headers
        TextView header1 = new TextView(this);
        TextView header2 = new TextView(this);
        TextView header3 = new TextView(this);

        // Set text for headers
        header1.setText("Name");
        header2.setText("DOB");
        header3.setText("Gender");

        // Optionally, set layout parameters for the headers (padding, gravity, etc.)

        // Add the headers TextViews to the headers row
        headersRow.addView(header1);
        headersRow.addView(header2);
        headersRow.addView(header3);

        // Add the headers row to the TableLayout
        tableLayout.addView(headersRow);

        // Iterate through the records
        for (Record record : recordList) {
            // Create a new row
            TableRow newRow = new TableRow(this);

            // Create new TextViews for each cell in the row
            TextView cell1 = new TextView(this);
            TextView cell2 = new TextView(this);
            TextView cell3 = new TextView(this);

            // Set text for each cell
            cell1.setText(record.getName());
            cell2.setText(record.getDob());
            cell3.setText(record.getGender());

            // Optionally, set layout parameters for the TextViews

            // Add the TextViews to the TableRow
            newRow.addView(cell1);
            newRow.addView(cell2);
            newRow.addView(cell3);

            // Add the TableRow to the TableLayout
            tableLayout.addView(newRow);
        }

    }





    // Helper method to check if a date is within the specified range
    private boolean isDateInRange(String date, String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date dobDate = sdf.parse(date);
            Date startDateRange = sdf.parse(startDate);
            Date endDateRange = sdf.parse(endDate);

            // Extract day, month, and year from the dates
            Calendar dobCalendar = Calendar.getInstance();
            Calendar startCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();

            dobCalendar.setTime(dobDate);
            startCalendar.setTime(startDateRange);
            endCalendar.setTime(endDateRange);

            // Check if the DOB is within the specified range
            return dobCalendar.equals(startCalendar) || dobCalendar.equals(endCalendar) ||
                    (dobCalendar.after(startCalendar) && dobCalendar.before(endCalendar));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
























































}









