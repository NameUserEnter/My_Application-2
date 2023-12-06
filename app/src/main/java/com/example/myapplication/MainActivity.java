package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startWorkWithStudents(View v){
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }
    public void startWorkWithContacts(View v){
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }

}