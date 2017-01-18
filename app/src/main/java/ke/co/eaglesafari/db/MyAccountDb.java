package ke.co.eaglesafari.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ke.co.eaglesafari.constant.Constant;
import ke.co.eaglesafari.items.MyAccountItem;

public class MyAccountDb extends SQLiteOpenHelper {

    static MyAccountDb sInstance;
    Context c;
    String TABLE_NAME = MyAccountItem.TABLE_NAME;

    public MyAccountDb(Context context) {
        super(context, MyAccountItem.DB_NAME, null, 33);
        c = context;
    }

    public static MyAccountDb getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new MyAccountDb(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + Constant.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + MyAccountItem.Const.KEY_BALANCE + " TEXT ,"
                + MyAccountItem.Const.KEY_NAME + " TEXT ,"
                + MyAccountItem.Const.KEY_POINTS + " TEXT ,"
                + MyAccountItem.Const.KEY_TELEPHONE + " TEXT ,"
                + MyAccountItem.Const.KEY_TIME + " TEXT" + ")";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    public void add(MyAccountItem has_model) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MyAccountItem.Const.KEY_BALANCE, has_model.getBalance());
        values.put(MyAccountItem.Const.KEY_NAME, has_model.getName());
        values.put(MyAccountItem.Const.KEY_POINTS, has_model.getPoints());
        values.put(MyAccountItem.Const.KEY_TELEPHONE,
                has_model.getTelephone());
        values.put(MyAccountItem.Const.KEY_TIME, has_model.getTime());

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

    public MyAccountItem get() {

        MyAccountItem item = new MyAccountItem();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {

                item.setBalance(cursor.getString(cursor
                        .getColumnIndex(MyAccountItem.Const.KEY_BALANCE)));

                item.setName(cursor.getString(cursor
                        .getColumnIndex(MyAccountItem.Const.KEY_NAME)));

                item.setPoints(cursor.getString(cursor
                        .getColumnIndex(MyAccountItem.Const.KEY_POINTS)));

                item.setTelephone(cursor.getString(cursor
                        .getColumnIndex(MyAccountItem.Const.KEY_TELEPHONE)));

                item.setTime(cursor.getString(cursor
                        .getColumnIndex(MyAccountItem.Const.KEY_TIME)));

            }
        }
        cursor.close();
        db.close();
        return item;
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
