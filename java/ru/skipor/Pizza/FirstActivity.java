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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import ru.skipor.RssReader.R;

/**
 * Created by Vladimir Skipor on 11/7/13.
 * Email: vladimirskipor@gmail.com
 */
public class FirstActivity extends Activity {

    public static final String PREFERENCES_NAME = "MySharedPreferences";
    public static final String NAME_TAG = "CarWashName";
    public static final String COUNT_TAG = "BoxCount";
    private SharedPreferences preferences;
    private EditText countEditText, nameEditText;


    public static int BEGIN_HOURS = 10;
    public static int DELIVERY_ITEMS = 28;







    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.first_activity);

        preferences = getPreferences( MODE_PRIVATE);

        if (preferences.getString(NAME_TAG, null) != null) {
            startMainActivity();
        }


        countEditText = (EditText) findViewById(R.id.boxes_count_edit_text);
        nameEditText = (EditText) findViewById(R.id.car_wash_name_edit_text);


        setTitle(R.string.first_activity_title);

    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(NAME_TAG, preferences.getString(NAME_TAG, null));
        intent.putExtra(COUNT_TAG, preferences.getInt(COUNT_TAG, -1));
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.first_activty_menu, menu);


        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm:
                saveData();
                break;

        }

        return super.onMenuItemSelected(featureId, item);
    }


    private void saveData() {

        String carWashName = nameEditText.getText().toString();
        int boxNumber = Integer.valueOf(countEditText.getText().toString());

        if (boxNumber > 0 && !carWashName.equals("")) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(COUNT_TAG, boxNumber);
            editor.putString(NAME_TAG, carWashName);
            editor.commit();

            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
            databaseHelper.open();

            databaseHelper.createDatabase(DELIVERY_ITEMS, boxNumber);
            databaseHelper.close();

           startMainActivity();

        }


    }





}
