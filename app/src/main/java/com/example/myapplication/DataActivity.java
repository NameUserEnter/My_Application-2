package com.example.myapplication;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.EditText;
        import android.widget.SimpleCursorAdapter;
        import android.widget.TextView;
        import android.widget.ListView;

public class DataActivity extends AppCompatActivity {
    ListView userList;
    TextView header;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        header = findViewById(R.id.header);
        userList = findViewById(R.id.list);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        databaseHelper = new DatabaseHelper(getApplicationContext());
    }
    @Override
    public void onResume() {
        super.onResume();
        db = databaseHelper.getReadableDatabase();
        userCursor =  db.rawQuery("SELECT * FROM "+ DatabaseHelper.TABLE, null);
        String[] headers = new String[] {DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_GRADE1, DatabaseHelper.COLUMN_GRADE2, DatabaseHelper.COLUMN_ADDRESS};
        userAdapter = new SimpleCursorAdapter(this, R.layout.custom_list_item_4,
                userCursor, headers, new int[]{R.id.text1,R.id.text2,R.id.text3,R.id.text4}, 0);
        header.setText("Найдено элементов: " +  userCursor.getCount());
        userList.setAdapter(userAdapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        userCursor.close();
    }
    public void onClickGrade(View view){
        Cursor query = databaseHelper.getUsersWithAverageGradeAbove60();
        userCursor =  db.rawQuery("SELECT * FROM "+ DatabaseHelper.TABLE, null);
        String[] headers = new String[] {DatabaseHelper.COLUMN_NAME};
        userAdapter = new SimpleCursorAdapter(this, R.layout.custom_list_item_1,
                query, headers, new int[]{R.id.text1}, 0);
        int q1 = query.getCount();
        int q2 = userCursor.getCount();
        float per = ((float)q1/q2)*100;
        header.setText("Відсоток студентів: " +  per +"%");
        userList.setAdapter(userAdapter);
    }
    public void onClickAdd(View view){
        Intent intent = new Intent(this, StudentActivity.class);
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