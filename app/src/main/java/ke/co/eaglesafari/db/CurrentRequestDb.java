package ke.co.eaglesafari.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.ArrayList;

import ke.co.eaglesafari.constant.Constant;
import ke.co.eaglesafari.items.RequestItem;


public class CurrentRequestDb extends SQLiteOpenHelper {

    static CurrentRequestDb sInstance;
    Context c;
    String TABLE_NAME = "tbl";

    public CurrentRequestDb(Context context) {
        super(context, "DBRequest", null, 34);
        c = context;
    }


    public void update(RequestItem requestItem, String id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        Gson gson = new Gson();
        newValues.put(Constant.KEY_VALUE,
                gson.toJson(requestItem));

        String[] args = new String[]{id};
        db.update(TABLE_NAME, newValues, Constant.KEY_ID
                + " = ?", args);
        db.close();

    }

    public boolean deleteItem(String id) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE "
                    + Constant.KEY_ID + " = " + id);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static CurrentRequestDb getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new CurrentRequestDb(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + Constant.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Constant.KEY_VALUE + " TEXT" + ")";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    public void add(RequestItem has_model) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Gson gson = new Gson();


        values.put(Constant.KEY_VALUE, gson.toJson(has_model));


        db.insert(TABLE_NAME, null, values);
        db.close();

    }

    // update

    public void resetTables() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete from " + TABLE_NAME);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<RequestItem> get() {

        ArrayList<RequestItem> list = new ArrayList<RequestItem>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    Gson gson = new Gson();
                    RequestItem item = gson.fromJson(cursor.getString(cursor.getColumnIndex(Constant.KEY_VALUE)), RequestItem.class);
                    item.setDb_id(cursor.getString(cursor.getColumnIndex(Constant.KEY_ID)));

                    list.add(item);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return list;
    }


    public int count() {

        int count = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

}
