package com.example.database_readandwrite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private EditText nameEditText;
    private EditText ageEditText;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.editText);
        ageEditText = findViewById(R.id.editText1);
        Button submit = findViewById(R.id.submitButton);
        Button read = findViewById(R.id.readButton);
        listView = findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(this);



        submit.setOnClickListener(v -> insertUserData());
        read.setOnClickListener(v -> getUserData());
    }

    private void insertUserData() {
        String name = nameEditText.getText().toString().trim();
        String ageStr = ageEditText.getText().toString().trim();

        if (!name.isEmpty() && !ageStr.isEmpty()) {
            int age = Integer.parseInt(ageStr); // Convert age to an integer

            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("age", age);

            long newRowId = databaseHelper.insertUserData(name,age);

            if (newRowId != -1) {
                // Insert successful
                nameEditText.setText(""); // Clear the EditText
                ageEditText.setText(""); // Clear the EditText
            } else {
                // Insert failed
                Toast.makeText(this, "Insertion failed", Toast.LENGTH_SHORT).show();
            }

            db.close();
        } else {
            // Display an error message or toast to inform the user
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }
    private void getUserData(){
        List<String> userDataList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllUserData();

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") int age = cursor.getInt(cursor.getColumnIndex("age"));
                String userData = "Name: " + name + ", Age: " + age;
                userDataList.add(userData);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Create an ArrayAdapter to display the data in a ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userDataList);
        listView.setAdapter(adapter);
    }
    }


