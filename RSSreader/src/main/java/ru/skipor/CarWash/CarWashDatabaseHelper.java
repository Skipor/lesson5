/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ru.skipor.CarWash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CarWashDatabaseHelper {

    private static CarWashDatabaseHelper instance = null;

    public static CarWashDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CarWashDatabaseHelper(context);
        }
        return instance;
    }

    public static final String KEY_ROWID = "_id";
    public static final String KEY_BOX = "box";
    public static final String KEY_MODEL = "model";
    public static final String KEY_COLOR = "color";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_TIME = "time";
    public static final String KEY_NUMBER = "number";
    private static final String DATABASE_NAME = "data";

    private static final String TAG = "FeedsDbAdapter";
    private final DatabaseHelper myDatabaseHelper;
    private SQLiteDatabase myDatabase;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table " + DATABASE_NAME + " ("+ KEY_ROWID +" integer primary key autoincrement, "
                    + KEY_TIME +" text not null, "
                    + KEY_BOX +  " int not null,"+ KEY_MODEL +" text, "
                    + KEY_COLOR +" text, "+ KEY_PHONE +" text, " + KEY_NUMBER +" text, "+");";

    private static final int DATABASE_VERSION = 1;

    private int databaseUsers;



    public static int oneOrderTimeMinutes = 30;
    public static int startTimeHour = 8;
    public static int startTimeMinutes = 0;
    public static int ordersCount = 28;


    private static class DatabaseHelper extends SQLiteOpenHelper {


        public DatabaseHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
            onCreate(db);
        }
    }


    private CarWashDatabaseHelper(Context context) {
        myDatabaseHelper = new DatabaseHelper(context);
        databaseUsers = 0;

    }


    synchronized public void open() throws SQLException {

        if (databaseUsers == 0) {
            myDatabase = myDatabaseHelper.getWritableDatabase();
        }
        databaseUsers++;


    }


    synchronized public void close() {
        databaseUsers--;
        if (databaseUsers == 0) {
            myDatabaseHelper.close();
            myDatabase = null;
        }
    }

    public void recreateOrdersTable(int boxCount) {
        myDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        myDatabase.execSQL(DATABASE_CREATE);



    }


//
//    public long createOrder(int box, String model, String color, String phone, String carNumber) {
//        createOrRecreateFeedTable(url);
//
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(KEY_MODEL, name);
//        initialValues.put(KEY_COLOR, url);
//
//        return myDatabase.insert(DATABASE_NAME, null, initialValues);
//    }



    /**
     * Delete the feed with the given rowId
     *
     * @param rowId id of feed to delete
     * @return true if deleted, false otherwise
     */
    public void deleteOrder(long rowId) {
        ContentValues args = new ContentValues();
        args.putNull(KEY_MODEL);
        args.putNull(KEY_COLOR);
        args.putNull(KEY_PHONE);
        args.putNull(KEY_NUMBER);

         myDatabase.update(DATABASE_NAME, args, KEY_ROWID + "=" + rowId, null);
    }

    /**
     * Return a Cursor over the list of all feeds in the database
     *
     * @return Cursor over all feeds
     */
    public Cursor fetchAllOrders() {


        return myDatabase.query(DATABASE_NAME, new String[]{KEY_ROWID,
                KEY_TIME,  KEY_BOX , KEY_MODEL,
                KEY_COLOR ,  KEY_PHONE ,KEY_NUMBER},KEY_MODEL + " IS NULL", null, null, null, null);
    }



    /**
     * Return a Cursor positioned at the feed that matches the given rowId
     *
     * @param rowId id of feed to retrieve
     * @return Cursor positioned to matching feed, if found
     * @throws android.database.SQLException if feed could not be found/retrieved
     */
    public Cursor fetchOrder(long rowId) throws SQLException {

        Cursor mCursor =

                myDatabase.query(true, DATABASE_NAME, new String[]{KEY_ROWID,
                         KEY_TIME,  KEY_BOX , KEY_MODEL,
                        KEY_COLOR ,  KEY_PHONE ,KEY_NUMBER}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;

    }







    public boolean updateOrder(long rowId, String model, String color, String phone, String carNumber) {

        ContentValues args = new ContentValues();
        args.put(KEY_MODEL, model);
        args.put(KEY_COLOR, color);
        args.put(KEY_PHONE, phone);
        args.put(KEY_NUMBER, carNumber);

        return myDatabase.update(DATABASE_NAME, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
