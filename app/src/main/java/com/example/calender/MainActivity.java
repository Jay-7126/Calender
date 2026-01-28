package com.example.calender;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView dateDisplay;
    private EditText noteInput;
    private Button saveButton, todayButton;

    private String selectedDate;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        calendarView = findViewById(R.id.calendarView);
        dateDisplay = findViewById(R.id.dateDisplay);
        noteInput = findViewById(R.id.noteInput);
        saveButton = findViewById(R.id.saveButton);
        todayButton = findViewById(R.id.todayButton);

        sharedPreferences = getSharedPreferences("CalendarData", Context.MODE_PRIVATE);

        // Set Default Date as Today
        long today = calendarView.getDate();
        calendarView.setDate(today);

        // 1. Listener for Date Selection
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Construct Date String (Month is 0-indexed, so add 1)
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                dateDisplay.setText("Events for: " + selectedDate);

                // Load any existing note for this date
                String savedNote = sharedPreferences.getString(selectedDate, "");
                noteInput.setText(savedNote);

                // Special Holiday Easter Egg
                if (dayOfMonth == 1 && month == 0) { // Jan 1st
                    Toast.makeText(MainActivity.this, "Happy New Year!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 2. Save Button Logic
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate != null) {
                    String note = noteInput.getText().toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(selectedDate, note);
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Note Saved for " + selectedDate, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Please tap a date first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 3. Go To Today Logic
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = System.currentTimeMillis();
                calendarView.setDate(currentTime, true, true);
                Toast.makeText(MainActivity.this, "Back to Today", Toast.LENGTH_SHORT).show();
            }
        });
    }
}