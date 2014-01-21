package ru.skipor.Pizza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by Vladimir Skipor on 1/21/14.
 * Email: vladimirskipor@gmail.com
 */
public class DatabaseHelper {
    private final static String TAG = "DatabaseHelper";


    public static final String ID = "_id";
    //Item's columns
//    public static final String COLUMN_FIELD1 = "field1";
//    public static final String COLUMN_FIELD2 = "field2";
//    public static final String COLUMN_FIELD3 = "field3";
//    public static final String COLUMN_FIELD4 = "field4";
    public static final String ORDER_ID = "sub_item_id";
    //Sub item's columns
    public static final String PIZZA_TYPE = "sub_field1";
    public static final String PHONE_NUMBER = "sub_field2";
    public static final String COURIER_ID = "sub_field3";
    public static final String DELIVERY_SPEED = "sub_field4";
    public static final String DELIVERY_START = "deliveryStart";
    //DB info
    public static final String NAME_DB = "exam2";
//    public static final String NAME_TABLE_ITEM = "item";
    public static final String TABLE_NAME_ORDER = "subitem";
    //Version
    public static final int VERSION = 1;
    //Create table scripts



    public static final String COURIER_CREATE_TABLE_PATTERN = "CREATE TABLE " + "%s"
            + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//            + COLUMN_FIELD1 + " TEXT, "
//            + COLUMN_FIELD2 + " TEXT, "
//            + COLUMN_FIELD3 + " TEXT, "
//            + COLUMN_FIELD4 + " TEXT, "
            + ORDER_ID + " INT )";

    public static final String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_NAME_ORDER + "IF NOT EXISTS"
            + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PIZZA_TYPE + " TEXT, "
            + PHONE_NUMBER + " TEXT, "
            + COURIER_ID + " INT, "
            + DELIVERY_SPEED + " INT,"
            + DELIVERY_START + " INT"
    + ")";

    private SQLiteOpenHelper myOpenHelper;
    private SQLiteDatabase myDatabase;

    private int databaseUsers = 0;


    public void createDatabase(int ordersCount, int courierCount) {
        for (int i = 0; i < courierCount; i++) {
            createCourierTable(ordersCount, i);
        }


    }

    private void createCourierTable(int ordersCount, int courierNumber) {

        final String courierTableName = getCourierTableName(courierNumber);
        myDatabase.execSQL(String.format(COURIER_CREATE_TABLE_PATTERN, courierTableName));

        ContentValues contentValues = new ContentValues();
        contentValues.putNull(ORDER_ID);

        for (int i = 0; i < ordersCount; i++) {

            myDatabase.insert(courierTableName, null, contentValues);
        }

    }

    private String getCourierTableName(int courierNumber) {
        return "[" + String.valueOf(courierNumber) + "]";
    }




    private class mySQLiteOpenHelper extends SQLiteOpenHelper {

        //Id column


        public mySQLiteOpenHelper(Context context) {
            super(context, NAME_DB, null, VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_ORDERS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        }
    }


    synchronized public void open() {

        if (databaseUsers == 0) {
            myDatabase = myOpenHelper.getWritableDatabase();
        }
        databaseUsers++;


    }


    synchronized public void close() {
        databaseUsers--;
        if (databaseUsers == 0) {
            myOpenHelper.close();
            myDatabase = null;
        }
    }

    private static DatabaseHelper instance =null;

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }
    private DatabaseHelper(Context context) {
        myOpenHelper = new mySQLiteOpenHelper(context);
    }

    public Cursor getAllOrdres() {
        return myDatabase.query(
                TABLE_NAME_ORDER,
                null,
                null,
                null,
                null,
                null,
                DELIVERY_START + " ASC" //Sort by field1
        );
    }

    public CourierFreeTimeList getFreeDeliveryIdsByCourierId(int courierId) {
//


        Cursor cursor =myDatabase.query(getCourierTableName(courierId), new String[]{ID}, ORDER_ID + "NOT NULL", null, null, null, ID);

        cursor.moveToFirst();

        CourierFreeTimeList courerFreeTimeList = new CourierFreeTimeList(courierId);

        do {

            courerFreeTimeList.addFreeTime(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_ID)));

        } while (cursor.moveToNext());

        return courerFreeTimeList;


    }

//    public SubItem getSubItem(int subItemId) {
//        String query = "SELECT * FROM " + TABLE_NAME_ORDER
//                + " WHERE " + ID + " = " + subItemId;
//        Cursor cursor = myDatabase.rawQuery(
//                query,
//                null
//        );
//
//        cursor.moveToFirst();
//
//        int id = cursor.getInt(cursor.getColumnIndex(ID));
//        String strSubField1 = cursor.getString(cursor.getColumnIndex(PIZZA_TYPE));
//        String strSubField2 = cursor.getString(cursor.getColumnIndex(PHONE_NUMBER));
//        String strSubField3 = cursor.getString(cursor.getColumnIndex(COURIER_ID));
//        String strSubField4 = cursor.getString(cursor.getColumnIndex(DELIVERY_SPEED));
//
//        return new SubItem(id, strSubField1, strSubField2, strSubField3, strSubField4);
//    }

//    public long addItem(CourierFreeTimeList item) {
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_FIELD1, item.getField1());
//        values.put(COLUMN_FIELD2, item.getField2());
//        values.put(COLUMN_FIELD3, item.getField3());
//        values.put(COLUMN_FIELD4, item.getField4());
//        values.put(ORDER_ID, item.getSubItemId());
//
//        return myDatabase.insert(
//                NAME_TABLE_ITEM,
//                null,
//                values);
//    }

    public long addOrder(String pizzaType, String phoneNumber, int courierId, int deliverySpeed, int deliveryStart) {
        ContentValues values = new ContentValues();
        values.put(PIZZA_TYPE, pizzaType);
        values.put(PHONE_NUMBER, phoneNumber);
        values.put(COURIER_ID, courierId);
        values.put(DELIVERY_SPEED, deliverySpeed);
        values.put(DELIVERY_START, deliveryStart);




        int orderId = (int) myDatabase.insert(
                TABLE_NAME_ORDER,
                null,
                values
        );


        addOrderToCourier(courierId, deliverySpeed, deliveryStart, orderId);
        return orderId;
    }

   private void addOrderToCourier(int courierId, int deliverySpeed, int deliveryStart, int orderId) {
        ContentValues values = new ContentValues();
        values.put(ORDER_ID, orderId);

       for (int i = deliveryStart; i < deliveryStart + deliverySpeed; i++) {

           updateCourierTableRow(courierId, values, i);

       }
    }

    private void updateCourierTableRow(int courierId, ContentValues values, int i) {
        myDatabase.update(
                getCourierTableName(courierId),
                values,
                ID + " = " + String.valueOf(i),
                null
        );
    }

//    public void updateSubItem(int subItemId, SubItem subItem) {
//        ContentValues values = new ContentValues();
//        values.put(PIZZA_TYPE, subItem.getSubField1());
//        values.put(PHONE_NUMBER, subItem.getSubField2());
//        values.put(COURIER_ID, subItem.getSubField3());
//        values.put(DELIVERY_SPEED, subItem.getSubField4());
//
//        myDatabase.update(
//                TABLE_NAME_ORDER,
//                values,
//                ID + " = " + subItemId,
//                null
//        );
//    }
/*
    public void removeItem(int itemId) {
        CourierFreeTimeList item = getFreeDeliveryIdsByCourierId(itemId);
        myDatabase.delete(
                NAME_TABLE_ITEM,
                ID + " = " + itemId,
                null
        );
        removeSubItem(item.getSubItemId());
    }

    public void removeSubItem(int subItemId) {
        myDatabase.delete(
                TABLE_NAME_ORDER,
                ID + " = " + subItemId,
                null
        );
    }*/
}

