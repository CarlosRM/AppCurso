package com.example.carlos.appcurso.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 31/01/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 24;

    private static final String DATABASE_NAME = "JediDB";

    public static final String USER_TABLE_NAME = "user_table";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_POINTS_4 = "points4";
    public static final String COLUMN_POINTS_6 = "points6";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TOAST = "toast";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_IMAGE = "image";

    private String[] allColumns = { COLUMN_ID, COLUMN_USER, COLUMN_POINTS_4, COLUMN_POINTS_6, COLUMN_TOAST, COLUMN_STATUS,COLUMN_IMAGE };

    private static final String USER_TABLE_CREATE =
            "CREATE TABLE " + USER_TABLE_NAME +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT UNIQUE, points4 INTEGER, points6 INTEGER," +
                    "toast INTEGER, status INTEGER, image TEXT);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE);
        db.execSQL("INSERT into " + USER_TABLE_NAME + " (user, points4, points6,toast,status) VALUES ('Carlos',13,14,0,0)");
        db.execSQL("INSERT into " + USER_TABLE_NAME + " (user, points4, points6,toast,status) VALUES ('Ana',12,15,0,0)");
        db.execSQL("INSERT into " + USER_TABLE_NAME + " (user, points4, points6,toast,status) VALUES ('Jordi',26,28,0,0)");
        db.execSQL("INSERT into " + USER_TABLE_NAME + " (user, points4, points6,toast,status) VALUES ('Juanmi',18,27,0,0)");
        db.execSQL("INSERT into " + USER_TABLE_NAME + " (user, points4, points6,toast,status) VALUES ('3',0,0,0,0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME + ";");
        db.execSQL(USER_TABLE_CREATE);
        db.execSQL("INSERT into " + USER_TABLE_NAME + " (user, points4, points6,toast,status) VALUES ('Carlos',13,14,0,0)");
        db.execSQL("INSERT into " + USER_TABLE_NAME + " (user, points4, points6,toast,status) VALUES ('Ana',12,15,0,0)");
        db.execSQL("INSERT into " + USER_TABLE_NAME + " (user, points4, points6,toast,status) VALUES ('Jordi',26,27,0,0)");
        db.execSQL("INSERT into " + USER_TABLE_NAME + " (user, points4, points6,toast,status) VALUES ('Juanmi',18,36,0,0)");
        db.execSQL("INSERT into " + USER_TABLE_NAME + " (user, points4, points6,toast,status) VALUES ('3',0,0,0,0)");
    }

    public List<User> getUsers(SQLiteDatabase db, String order) {
        List<User> users = new ArrayList<>();

        Cursor cursor = db.query(USER_TABLE_NAME,
                allColumns, order + " > -1", null, null, null, order);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return users;
    }

    public void resetRanking(SQLiteDatabase db, String type) {
        String query = "UPDATE user_table SET " +type+ " = -1";
        db.execSQL(query);
    }

    public int getUserPoints(SQLiteDatabase db, String username, String type) {
        Cursor cursor = db.query(USER_TABLE_NAME,
                allColumns, "user = '" + username + "'", null, null, null, null);

        cursor.moveToFirst();
        User user = cursorToUser(cursor);
        if(type == "points4") return user.getPoints4();
        else return user.getPoints6();
    }

    public User getUser(SQLiteDatabase db, String username) {
        Cursor cursor = db.query(USER_TABLE_NAME,
                allColumns, "user = '" + username + "'", null, null, null, null);
        cursor.moveToFirst();
        User user = cursorToUser(cursor);
        return user;
    }

    public void updateImage(SQLiteDatabase db, String username, String image) {
        String query = "UPDATE user_table SET image = '" + image + "' WHERE user = '" + username + "'";
        db.execSQL(query);
    }

    public void updatePreference(SQLiteDatabase db, String username, String preference, boolean value) {

        String query;
        if(value) query = "UPDATE user_table SET " +preference+ " = 1 WHERE user = '" + username + "'";
        else query = "UPDATE user_table SET " +preference+ " = 0 WHERE user = '" + username + "'";
        db.execSQL(query);
    }


    public boolean userExists(SQLiteDatabase db, String username) {
        Cursor cursor = db.query(USER_TABLE_NAME,allColumns,COLUMN_USER + " = '" + username + "'",null,null,null,null);
        return cursor.moveToFirst();
    }

    public void insertUser(SQLiteDatabase db, String username) {
        db.execSQL("INSERT into " + USER_TABLE_NAME + " (user, points4, points6, toast, status) VALUES ('" + username + "',-1,-1,0,0)");
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(0));
        user.setName(cursor.getString(1));
        user.setPoints4(cursor.getInt(2));
        user.setPoints6(cursor.getInt(3));
        user.setToast(cursor.getInt(4) > 0);
        user.setStatus(cursor.getInt(5) > 0);
        user.setImage(cursor.getString(6));
        return user;
    }
}


