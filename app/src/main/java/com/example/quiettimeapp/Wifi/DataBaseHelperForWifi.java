package com.example.quiettimeapp.Wifi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelperForWifi extends SQLiteOpenHelper
{

    public static final String COLUMN_ID = "ID";
    public static final String SAVED_SSIDS_TABLE = "savedSSDsTable";
    public static final String COLUMN_SSID = "SSID";

    public DataBaseHelperForWifi(@Nullable Context context) { super(context, "SavedSS" + COLUMN_ID + "s.db", null, 1); }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String createTableStatement = "CREATE TABLE " + SAVED_SSIDS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_SSID + " TEXT )";
        sqLiteDatabase.execSQL(createTableStatement);
//        sqLiteDatabase.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public void addSSID(String ssid)
    {
        SQLiteDatabase sqLiteDatabase =  this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SSID, ssid);
        sqLiteDatabase.insert(SAVED_SSIDS_TABLE, null, cv);
//        sqLiteDatabase.close();
    }

    public List<String> getAllSSIDs()
    {
        List<String> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM "+ SAVED_SSIDS_TABLE ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                returnList.add(cursor.getString(1));
            }while(cursor.moveToNext());
        } else {
            Log.d("Results", "NO DATA IN SQL!");
        }

        cursor.close();
//        db.close();
        return returnList;
    }

    public boolean SearchInSSIDs(String ssid)
    {
        boolean result = false;

        String queryString = "SELECT * FROM "+ SAVED_SSIDS_TABLE ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                if(ssid.equals(cursor.getString(1))) {
                    result = true;
                    break;
                }
            }while(cursor.moveToNext());
        } else {
            Log.d("Results", "NO DATA IN SQL!");
        }
        cursor.close();
        db.close();

        return result;
    }
}
