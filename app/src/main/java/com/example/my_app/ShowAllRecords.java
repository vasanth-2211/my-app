package com.example.my_app;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ShowAllRecords extends AppCompatActivity {
    private Button b1;
    private TextView t1;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_records);

        b1 = findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowAllRecords.this, home.class);
                startActivity(intent);
            }
        });
        String Date1 = getIntent().getStringExtra("Date1");
        String Date2 = getIntent().getStringExtra("Date2");
        String firebaseUrl = "https://my-app-5754e-default-rtdb.firebaseio.com/";
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference("1gLJ8I_1O5SO7El_kklJqBipY-0DqTrJyQRQINbFH8SA/Sheet1");


        if (Date1.length() > 4 || Date2.length() > 4) {

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<home.Record> recordList = new ArrayList<>();

                    // Iterate through the data
                    for (DataSnapshot personSnapshot : dataSnapshot.getChildren()) {
                        String name = personSnapshot.child("Name").getValue(String.class);
                        String dob = personSnapshot.child("DOB").getValue(String.class);

                        // Check if the DOB is within the specified date range
                        if (isDateInRange(dob, Date1, Date2)) {
                            String gender = personSnapshot.child("Gender").getValue(String.class);

                            // Create a Record object and add it to the list
                            home.Record record = new home.Record(name, dob, gender);
                            recordList.add(record);
                        }
                    }
                    //  displayRecordsInTextView(recordList);
                    displayRecordsInTable(recordList);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error reading data: " + databaseError.getMessage());
                }
            });

        }
        if(Date1.length()  <= 4 && Date2.length() <= 4 ){
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = 34)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    List<home.Record> recordList = new ArrayList<>();

                    // Iterate through the data
                    for (DataSnapshot personSnapshot : dataSnapshot.getChildren()) {
                        String name = personSnapshot.child("Name").getValue(String.class);
                        String dob = personSnapshot.child("DOB").getValue(String.class);

                        // Check if the year of DOB is within the specified range
                        if (isYearInRange(dob, Date1, Date2)) {
                            String gender = personSnapshot.child("Gender").getValue(String.class);

                            // Create a Record object and add it to the list
                            home.Record record = new home.Record(name, dob, gender);
                            recordList.add(record);
                        }
                    }
                    displayRecordsInTable(recordList);

                    // Now you have the recordList based on the year range
                    // You can further process or display the records as needed
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled event if needed
                }
            });
        }

    }
    public class MyRecord implements Serializable {
        private String name;
        private String dob;
        private String gender;

        // Constructor
        public MyRecord(String name, String dob, String gender) {
            this.name = name;
            this.dob = dob;
            this.gender = gender;
        }

        // Getter methods
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
    private boolean isYearInRange(String date, String startYear, String endYear) {
        try {

            String year = date.substring(date.length() - 4);
            System.out.println("Year: " + year);
            int yearToSearch = Integer.parseInt(year);


            // Parse startYear and endYear
            int start = Integer.parseInt(startYear);
            int end = Integer.parseInt(endYear);

            // Check if the year is within the specified range
            return yearToSearch >= start && yearToSearch <= end;
        } catch (NumberFormatException e) {
            // Handle the case where parsing fails (e.g., invalid date format)
            e.printStackTrace(); // Log the exception
            return false;
        }
    }
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



    private void displayRecordsInTable(List<home.Record> recordList) {
        TableLayout tableLayout = findViewById(R.id.tableLayout1);

        // Add headers row
        TableRow headersRow = new TableRow(this);

        // Create TextViews for headers
        TextView header1 = new TextView(this);
        TextView header2 = new TextView(this);
        TextView header3 = new TextView(this);

        // Set text for headers


        // Optionally, set layout parameters for the headers (padding, gravity, etc.)

        // Add the headers TextViews to the headers row
        headersRow.addView(header1);
        headersRow.addView(header2);
        headersRow.addView(header3);

        // Add the headers row to the TableLayout
        tableLayout.addView(headersRow);

        // Iterate through the records
        for (home.Record record : recordList) {
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





}