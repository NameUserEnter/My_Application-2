package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "students.db";
    private static final int SCHEMA = 1;
    public static final String TABLE = "students";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GRADE1 = "grade_1";
    public static final String COLUMN_GRADE2 = "grade_2";
    public static final String COLUMN_ADDRESS = "address";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE students (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_GRADE1 + " INTEGER, " +
                COLUMN_GRADE2 + " INTEGER, " +
                COLUMN_ADDRESS + " TEXT);");

        db.execSQL("INSERT INTO " + TABLE + " (" +
                COLUMN_NAME + ", " +
                COLUMN_GRADE1 + ", " +
                COLUMN_GRADE2 + ", " +
                COLUMN_ADDRESS + ") VALUES ('Том Смит', 75, 82, '55.86, 87.98');");

    }
    public Cursor getUsersWithAverageGradeAbove60() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE +
                " WHERE (" + COLUMN_GRADE1 + " + " + COLUMN_GRADE2 + ") / 2 > 60";
        return db.rawQuery(query, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}