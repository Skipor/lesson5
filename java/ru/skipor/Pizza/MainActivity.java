package ru.skipor.Pizza;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ru.skipor.RssReader.R;

public class MainActivity extends ListActivity {



    public static String  getTimeById(int deliveryStart) {

        deliveryStart-=1;

        int hours = FirstActivity.BEGIN_HOURS + deliveryStart / 2;
        return String.valueOf(hours) + ":" + (deliveryStart % 2 == 0 ? "00" : "30");

    }


//    public static final String EXTRA_FEED_URL = "ru.skipor.CarWash.MainActivity Feed URL";
//    public static final String EXTRA_FEED_NAME = "ru.skipor.CarWash.MainActivity Feed Name";
    public static final String UPDATE_MESSAGE = "Feed is up to date";


    public static final String TAG = "MainActivity";
    private DatabaseHelper myDatabaseHelper;
    private String name;
    private int couriersCount;
    private Cursor currentCursor;
    private myCursorAdapter adapter;

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    private static final int EDIT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        name = intent.getStringExtra(FirstActivity.NAME_TAG);
        couriersCount = intent.getIntExtra(FirstActivity.COUNT_TAG,0);
        setTitle(name);



        setContentView(R.layout.activity_main);
        myDatabaseHelper = DatabaseHelper.getInstance(this);
        myDatabaseHelper.open();
        //fill data
        currentCursor = myDatabaseHelper.getAllOrdres();

        // Now create a simple cursor adapter and set it to display


        adapter = new myCursorAdapter(this, currentCursor);

        //fill data end


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                createOrder();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    private void refillData() {
        currentCursor = myDatabaseHelper.getAllOrdres();
        Cursor oldCursor = ((SimpleCursorAdapter) getListAdapter()).swapCursor(currentCursor);
        if (oldCursor != null) {
            oldCursor.close();
        }
    }


    //
//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//
//        Cursor cursor = null;
//        try {
//            cursor = myDatabaseHelper.fetchCity(id);
//            String name = cursor.getString(cursor.getColumnIndexOrThrow(WeatherDatabaseHelper.KEY_CITY_NAME));
//            openFeed(name);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//    }


//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        if (currentCursor.isClosed()) { //if base close cursors because of inserting
//            refillData();
//            return;
//
//        }
//        currentCursor.moveToPosition((int) id - 1);
//        Cursor cursor = null;
//        try {
//            cursor = myDatabaseHelper.fetchTitle(name, id);
//            String body = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FEED_KEY_BODY));
//            Intent intent = new Intent(this, DescriptionActivity.class);
//            intent.putExtra(DescriptionActivity.EXTRA_DESCRIPTION, body);
//            intent.putExtra(DescriptionActivity.EXTRA_FEED_NAME, couriersCount);
//            startActivity(intent);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//    }


//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.add(0, EDIT_ID, 0, R.string.menu_edit);
//        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case DELETE_ID:
//                AdapterView.AdapterContextMenuInfo infoDelete = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//                myDatabaseHelper.deleteOrder(infoDelete.id);
//                refillData();
//                break;
//            case EDIT_ID:
//                AdapterContextMenuInfo infoEdit = (AdapterContextMenuInfo) item.getMenuInfo();
//                Intent i = new Intent(this, EditActivity.class);
//                i.putExtra(DatabaseHelper.ID, infoEdit.id);
//                startActivityForResult(i, ACTIVITY_EDIT);
//                break;
//            default:
//                break;
//        }
//        return super.onContextItemSelected(item);
//    }

    private void createOrder() {
        Intent i = new Intent(this, EditActivity.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        refillData();
    }



    @Override
    protected void onResume() {
//        refillData(); //refill only when result received
        super.onResume();
    }

    @Override
    protected void onDestroy() {

        myDatabaseHelper.close();
        super.onDestroy();
    }

    private static class myCursorAdapter extends CursorAdapter {


        private myCursorAdapter(Context context, int layout, Cursor c, int flags) {
            super(context, c, flags);
        }

        public myCursorAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.order_row, null);
        }




        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ((TextView) view.findViewById(R.id.time)).
                    setText(getTimeById(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.DELIVERY_START))));
            ((TextView) view.findViewById(R.id.courierId)).
                    setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COURIER_ID))));
            ((TextView) view.findViewById(R.id.pizza_type)).
                    setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PIZZA_TYPE)));







        }
    }

//    private void closeCursor() {
//        Cursor cursor = ((SimpleCursorAdapter) getListAdapter()).getCursor();
//        if (cursor != null) {
//            cursor.close();
//        }
//    }
}

