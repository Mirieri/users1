package ke.co.eaglesafari.auth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ke.co.eaglesafari.constant.Constant;


public class LoginDb extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Login";

    // Login table name
    private static final String TABLE_LOGIN = "user";

    // Login Table Columns names
    static LoginDb sInstance;

    public LoginDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static LoginDb getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new LoginDb(context);
        }
        return sInstance;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + Constant.KEY_ID + " INTEGER PRIMARY KEY,"
                + LoginModel.KEY_TOKEN + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // Create tables again
        onCreate(db);
    }

    public void saveToken(String token) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LoginModel.KEY_TOKEN, token);


        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        // db.close(); // Closing database connection
    }

    /********************************************************************
     * Getting user data from database
     ********************************************************************/
    public String getToken() {
        String token = "";
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            token = cursor.getString(cursor
                    .getColumnIndex(LoginModel.KEY_TOKEN));


        }
        cursor.close();
        // db.close();
        // return user
        return token;
    }

    /**
     * Getting user login status return true if rows are there in table
     */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        // db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        // db.close();
    }

}
