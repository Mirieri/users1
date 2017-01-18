package ke.co.eaglesafari.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.google.gson.Gson;

import ke.co.eaglesafari.constant.Constant;
import ke.co.eaglesafari.items.Pagination;

public class PaginationDb extends SQLiteOpenHelper {

    public static String DATABASENAME = "PaginationDb";
    public static String TABLENAME = "items";

    // adjust

    Context c;
    static PaginationDb sInstance;

    public PaginationDb(Context context) {
        super(context, DATABASENAME, null, 33);
        c = context;
    }

    public static PaginationDb getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new PaginationDb(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLENAME + "("
                + Constant.KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Constant.KEY_IDENTIFIER + " TEXT ,"
                + Constant.KEY_VALUE + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLENAME);
        onCreate(db);
    }

    public void addPagination(Pagination pagination, String identfier) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Gson gson = new Gson();
        values.put(Constant.KEY_VALUE, gson.toJson(pagination));
        values.put(Constant.KEY_IDENTIFIER, identfier);

        db.insert(TABLENAME, null, values);
        db.close();

    }

    public void update(String id, Pagination pagination) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        Gson gson = new Gson();
        newValues.put(Constant.KEY_VALUE,
                gson.toJson(pagination));

        String[] args = new String[]{id};
        db.update(TABLENAME, newValues, Constant.KEY_IDENTIFIER
                + " = ?", args);
        db.close();

    }

    public void resetTables() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete from " + TABLENAME);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isExist(String id) {

        boolean exist = false;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLENAME + " WHERE " + Constant.KEY_IDENTIFIER + " = ?",
                new String[]{id});
        if (cursor.getCount() > 0) {
            exist = true;
        }
        cursor.close();
        db.close();
        return exist;
    }

    public Pagination getPagination(String id) {

        Pagination item = null;

        Gson gson = new Gson();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLENAME + " WHERE " + Constant.KEY_IDENTIFIER + " = ?",
                new String[]{id});
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {


                    String data = cursor
                            .getString(cursor
                                    .getColumnIndex(Constant.KEY_VALUE));

                    item = gson.fromJson(data, Pagination.class);


                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return item;
    }

}
