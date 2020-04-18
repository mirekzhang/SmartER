package com.example.mirek.smarter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mirek on 29/04/2018.
 */

public class DBManager {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "smarter.db";
    private final Context context;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBStructure.tableEntry.TABLE_NAME + " (" +
                    DBStructure.tableEntry._ID  + " INTEGER PRIMARY KEY," +
                    DBStructure.tableEntry.COLUMN_USAGE_ID + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_RES_ID + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_HOURS + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_FRIDGE_USAGE + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_AC_USAGE + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_WM_USAGE + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_TEMPERATURE + TEXT_TYPE +
                    " );";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBStructure.tableEntry.TABLE_NAME;
    private MySQLiteOpenHelper myDBHelper;
    private SQLiteDatabase db;
    private String[] columns = {
            DBStructure.tableEntry.COLUMN_USAGE_ID,
            DBStructure.tableEntry.COLUMN_RES_ID,
            DBStructure.tableEntry.COLUMN_DATE,
            DBStructure.tableEntry.COLUMN_HOURS,
            DBStructure.tableEntry.COLUMN_FRIDGE_USAGE,
            DBStructure.tableEntry.COLUMN_AC_USAGE,
            DBStructure.tableEntry.COLUMN_WM_USAGE,
            DBStructure.tableEntry.COLUMN_TEMPERATURE };

    public DBManager(Context ctx) {
        this.context = ctx;
        myDBHelper = new MySQLiteOpenHelper(context);
    }
    private static class MySQLiteOpenHelper extends SQLiteOpenHelper {

        public MySQLiteOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            onCreate(db);
        }
    }
    public DBManager open() throws SQLException {
        db = myDBHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        myDBHelper.close();
    }

    public long insertData(String usageid, String resid, String date, String hours, String fridgeusage, String acusage, String wmusage, String temperature) {
        ContentValues values = new ContentValues();
        values.put(DBStructure.tableEntry.COLUMN_USAGE_ID, usageid);
        values.put(DBStructure.tableEntry.COLUMN_RES_ID, resid);
        values.put(DBStructure.tableEntry.COLUMN_DATE, date);
        values.put(DBStructure.tableEntry.COLUMN_HOURS, hours);
        values.put(DBStructure.tableEntry.COLUMN_FRIDGE_USAGE, fridgeusage);
        values.put(DBStructure.tableEntry.COLUMN_AC_USAGE, acusage);
        values.put(DBStructure.tableEntry.COLUMN_WM_USAGE, wmusage);
        values.put(DBStructure.tableEntry.COLUMN_TEMPERATURE, temperature);
        return db.insert(DBStructure.tableEntry.TABLE_NAME, null, values);
    }

    public Cursor getAllData() {
        return db.query(DBStructure.tableEntry.TABLE_NAME, columns, null, null, null, null, null);
    }

}
