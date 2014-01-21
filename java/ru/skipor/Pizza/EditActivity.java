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
package ru.skipor.Pizza;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;

import ru.skipor.RssReader.R;

public class EditActivity extends Activity {

    private EditText pizzaTypeEditText;
    private EditText phoneNumberEditText;
    private Long mRowId;
    private DatabaseHelper mDatabaseHelper;


    private static class TimeListItem {
        public int courierId;
        public int deliveryId;

        private TimeListItem(int courierId, int deliveryId) {
            this.courierId = courierId;
            this.deliveryId = deliveryId;
        }
    }

    private ArrayList<TimeListItem> timeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseHelper = DatabaseHelper.getInstance(this);

        setContentView(R.layout.activity_edit);
        setTitle(R.string.edit_feed);

        pizzaTypeEditText = (EditText) findViewById(R.id.pizza_type);
        phoneNumberEditText = (EditText) findViewById(R.id.body);





//        Button confirmButton = (Button) findViewById(R.id.confirm);

//        mRowId = (savedInstanceState == null) ? null :
//            (Long) savedInstanceState.getSerializable(DatabaseHelper.ID);
//		if (mRowId == null) {
//			Bundle extras = getIntent().getExtras();
//			mRowId = extras != null ? extras.getLong(DatabaseHelper.ID)
//									: null;
//		}
//
//		populateFields();

//        confirmButton.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View view) {
//                setResult(RESULT_OK);
//                finish();
//            }
//
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm:
                saveState();
                setResult(RESULT_OK);
                finish();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }
//    private void populateFields() {
//        if (mRowId != null) {
//            Cursor feed = mDatabaseHelper.getOrder(mRowId);
//            startManagingCursor(feed);
//            pizzaTypeEditText.setText(feed.getString(
//                    feed.getColumnIndexOrThrow(DatabaseHelper.KEY_MODEL)));
//            phoneNumberEditText.setText(feed.getString(
//                    feed.getColumnIndexOrThrow(DatabaseHelper.KEY_COLOR)));
//        }
//    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        saveState();
//        outState.putSerializable(DatabaseHelper.KEY_ROWID, mRowId);
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        saveState();
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        populateFields();
//    }

    private void saveState() {
        String title = pizzaTypeEditText.getText().toString();
        String body = phoneNumberEditText.getText().toString();

//        if (mRowId == null) {
//            long id = mDatabaseHelper.addOrder(title, body);
//            if (id > 0) {
//                mRowId = id;
//            }
//        } else {
//            mDatabaseHelper.updateOrder(mRowId, title, body);
//        }
    }


}
