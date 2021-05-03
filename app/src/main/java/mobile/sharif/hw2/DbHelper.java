package mobile.sharif.hw2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "data";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_LATITUDE = "latitude";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "locations.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_LONGITUDE + " REAL," +
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

    public void putLocation(SQLiteDatabase db, MyLocation location) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_NAME, location.getName());
        values.put(DbHelper.COLUMN_LATITUDE, location.getLatitude());
        values.put(DbHelper.COLUMN_LONGITUDE, location.getLongitude());
        // Insert the new row, returning the primary key value of the new row
        long row_id = db.insert(DbHelper.TABLE_NAME, null, values);
    }

    public void removeLocation(SQLiteDatabase db, MyLocation location) {
        // todo ali
    }

    public MyLocation getLocation(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_NAME));
        double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LONGITUDE));
        double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LATITUDE));
        return new MyLocation(longitude, latitude, name);
    }

    public ArrayList<MyLocation> getAllLocations(SQLiteDatabase db) {
        Cursor cursor = db.query(DbHelper.TABLE_NAME,
                null, null, null, null, null, null);

        ArrayList<MyLocation> locations = new ArrayList<>();
        double length = cursor.getCount();
        while (cursor.moveToNext()) {
            MyLocation location = getLocation(cursor);
            locations.add(location);
        }
        cursor.close();
        return locations;
    }
}

