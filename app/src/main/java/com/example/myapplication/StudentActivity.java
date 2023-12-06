package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.DataActivity;
import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;

public class StudentActivity extends AppCompatActivity {

    EditText nameBox;
    EditText grade1Box;
    EditText grade2Box;
    EditText addressBox;
    Button delButton;
    Button saveButton;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        nameBox = findViewById(R.id.name);
        grade1Box = findViewById(R.id.grade1);
        grade2Box = findViewById(R.id.grade2);
        addressBox = findViewById(R.id.address);
        delButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }
        if (userId > 0) {
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                    DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();
            nameBox.setText(userCursor.getString(1));
            grade1Box.setText(String.valueOf(userCursor.getInt(2)));
            grade2Box.setText(String.valueOf(userCursor.getInt(3)));
            addressBox.setText(userCursor.getString(4));
            userCursor.close();
        } else {
            delButton.setVisibility(View.GONE);
        }
    }

    public void save(View view){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, nameBox.getText().toString());
        cv.put(DatabaseHelper.COLUMN_GRADE1, Integer.parseInt(grade1Box.getText().toString()));
        cv.put(DatabaseHelper.COLUMN_GRADE2, Integer.parseInt(grade2Box.getText().toString()));
        cv.put(DatabaseHelper.COLUMN_ADDRESS, addressBox.getText().toString());

        if (userId > 0) {
            db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + userId, null);
        } else {
            db.insert(DatabaseHelper.TABLE, null, cv);
        }
        goHome();
    }
    public void delete(View view){
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        goHome();
    }
    public void goMap(View view){
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        goHome();
    }
    private void goHome(){
        db.close();
        Intent intent = new Intent(this, DataActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
    public void startWorkWithStudents(View v){
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }
    public void startWorkWithContacts(View v){
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }
    public void startWorkWithMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}