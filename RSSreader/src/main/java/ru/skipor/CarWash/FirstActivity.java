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
    public static final String CAR_WASH_NAME_TAG = "CarWashName";
    public static final String BOX_COUNT_TAG = "BoxCount";
    private SharedPreferences preferences;
    private EditText boxNumberEditText, carWashNameEditText;






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        preferences = getSharedPreferences(PREFERENCES_NAME, 0);

        if (preferences.getString(CAR_WASH_NAME_TAG, null) != null) {
            startMainActivity();
        }


        boxNumberEditText = (EditText) findViewById(R.id.boxes_count_edit_text);
        carWashNameEditText = (EditText) findViewById(R.id.car_wash_name_edit_text);


        setContentView(R.layout.first_activity);
        setTitle(R.string.first_activity_title);

    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(CAR_WASH_NAME_TAG, preferences.getString(CAR_WASH_NAME_TAG, null));
        intent.putExtra(BOX_COUNT_TAG, preferences.getInt(BOX_COUNT_TAG, -1));
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

        String carWashName = carWashNameEditText.getText().toString();
        int boxNumber = Integer.valueOf(boxNumberEditText.getText().toString());

        if (boxNumber > 0 && carWashName != "") {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(BOX_COUNT_TAG, boxNumber);
            editor.putString(CAR_WASH_NAME_TAG, carWashName);
            editor.commit();
           startMainActivity();

        }


    }





}
