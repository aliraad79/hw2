package mobile.sharif.hw2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "data";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_LATITUDE = "latitude ";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "coins.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_LONGITUDE + " TEXT," +
                    COLUMN_LATITUDE + " REAL)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    // If you change the database schema, you must increment the database version.


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void deleteAllData(SQLiteDatabase db) {
        db.execSQL("delete from " + TABLE_NAME);
    }

    public void putCoin(SQLiteDatabase db, Coin coin) {
        if (hasCoin(db, coin)) {
            return;
        }
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(mobile.sharif.coinmarket.DbHelper.COLUMN_NAME, coin.getName());
        values.put(mobile.sharif.coinmarket.DbHelper.COLUMN_SEVEN_DAY, coin.getSeven_day_change());
        values.put(mobile.sharif.coinmarket.DbHelper.COLUMN_RANK, coin.getRank());
        // Insert the new row, returning the primary key value of the new row
        long row_id = db.insert(mobile.sharif.coinmarket.DbHelper.TABLE_NAME, null, values);
        coin.setRow_id(row_id);
    }

    public Coin getCoin(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(mobile.sharif.coinmarket.DbHelper.COLUMN_NAME));
        String logo = cursor.getString(cursor.getColumnIndexOrThrow(mobile.sharif.coinmarket.DbHelper.COLUMN_LOGO));
        int rank = cursor.getInt(cursor.getColumnIndexOrThrow(mobile.sharif.coinmarket.DbHelper.COLUMN_RANK));
        return new Coin(name, short_name, price, one_hour, one_day, seven_day, logo, rank);
    }

    public ArrayList<Coin> getAllCoins(SQLiteDatabase db, ProgressBar progress) {
        Cursor cursor = db.query(mobile.sharif.coinmarket.DbHelper.TABLE_NAME,
                null, null, null, null, null, null);

        ArrayList<Coin> coins = new ArrayList<>();
        double length = cursor.getCount();
        while (cursor.moveToNext()) {
            Coin coin = getCoin(cursor);
            coins.add(coin);
        }
        progress.setProgress(0);
        cursor.close();
        return coins;
    }
}

