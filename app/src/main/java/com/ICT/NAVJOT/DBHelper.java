package com.ICT.NAVJOT;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Chouhan on 1/13/2017.
 */

public class DBHelper {

        public static final String COL_1 = "id";
        public static final String COL_2 = "name";
        public static final String COL_3 = "surname";
        public static final String COL_4 = "hindi";
        public static final String COL_5 = "english";
        public static final String COL_6 = "maths";
        public static final String COL_7 = "maths2";
        public static final String COL_8 = "maths3";
        private static final String TAG = "DBAdapter";
        private static final String DATABASE_NAME = "MyDB";
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_TABLE = "student";
        private static final String DATABASE_CREATE = "create table student (id integer primary key autoincrement, "
                + "name text not null, surname text not null, hindi text not null,english text not null," +
                "maths text not null, maths2 text not null, maths3 text not null);";

        private final Context context;
        private DatabaseHelper DBHelper;
        private SQLiteDatabase db;

        public DBHelper(Context ctx) {
            this.context = ctx;
            DBHelper = new DatabaseHelper(context);
        }


        private static class DatabaseHelper extends SQLiteOpenHelper {
            DatabaseHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                try {
                    db.execSQL(DATABASE_CREATE);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
                db.execSQL("DROP TABLE IF EXISTS student");
                onCreate(db);
            }
        }


        public DBHelper open() throws SQLException {
            db = DBHelper.getWritableDatabase();
            return this;
        }

        //---closes the database---
        public void close() {
            DBHelper.close();
        }


        public long insertdata(String name, String surname, String hindi, String english, String maths, String maths2, String maths3) {

            ContentValues cv = new ContentValues();
            cv.put(COL_2, name);
            cv.put(COL_3, surname);
            cv.put(COL_4, hindi);
            cv.put(COL_5, english);
            cv.put(COL_6, maths);
            cv.put(COL_7, maths2);
            cv.put(COL_8, maths3);
            long val= db.insert(DATABASE_TABLE, null, cv);
            return val;


        }

        public boolean deleteContact(long id) {
            return db.delete(DATABASE_TABLE, COL_1 + "=" + id, null) > 0;
        }

        public Cursor getAllContacts() {
            return db.query(DATABASE_TABLE, new String[]{COL_1, COL_2, COL_3, COL_4, COL_5, COL_6}, null, null, null, null, null);
        }

        public Cursor getContact(long id) throws SQLException {
            Cursor mCursor = db.query(true, DATABASE_TABLE, new String[]{ COL_2, COL_3, COL_4, COL_5, COL_6,COL_7,COL_8}, COL_1
                    + "=" + id, null, null, null, null, null);
            if (mCursor != null) mCursor.moveToFirst();
            return mCursor;
        }

        public boolean updateContact(long id, String name, String surname, int hindi, int english, int maths) {
            ContentValues cv = new ContentValues();
            cv.put(COL_2, name);
            cv.put(COL_3, surname);
            cv.put(COL_4, hindi);
            cv.put(COL_5, english);
            cv.put(COL_6, maths);

            return db.update(DATABASE_TABLE, cv, COL_1 + "=" + id, null) > 0;
        }

 /*/   public Cursor totaldata()
    {



    //    return db.query(DATABASE_TABLE,new String[]{"SUM("+COL_4+")"},null,null,null,null,null);
       String q="SELECT SUM("+COL_4+") FROM ("+DATABASE_TABLE+")";
        Cursor cursor=db.rawQuery(q,null);
        return  cursor;

    }

    }/*/


        public int totaldata() {

            //   Cursor cursor = db.rawQuery("SELECT SUM(" + COL_4 + ") FROM" + DATABASE_TABLE, null);
            //  cursor.getInt(0);
            Cursor cursor = db.rawQuery("SELECT SUM(" + COL_4 + ") FROM " + DATABASE_TABLE, null);
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        }
        public int totalenglish() {

            //   Cursor cursor = db.rawQuery("SELECT SUM(" + COL_4 + ") FROM" + DATABASE_TABLE, null);
            //  cursor.getInt(0);
            Cursor cursor = db.rawQuery("SELECT SUM(" + COL_5 + ") FROM " + DATABASE_TABLE, null);
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        }
        public int totalmaths() {

            //   Cursor cursor = db.rawQuery("SELECT SUM(" + COL_4 + ") FROM" + DATABASE_TABLE, null);
            //  cursor.getInt(0);
            Cursor cursor = db.rawQuery("SELECT SUM(" + COL_6 + ") FROM " + DATABASE_TABLE, null);
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        }

    }
