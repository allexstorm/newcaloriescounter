/*
 *     Copyright (C) 2016  Alexander Samtsov
 *
 *     This file is part of New Calories Counter.
 *
 *     New Calories Counter is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     New Calories Counter is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with New Calories Counter.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alexandersamtsov.newcaloriescounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewPhisicalActivity extends AppCompatActivity {

    private PhisicalDatabaseHelper myDbHelper;

    private static final String TAG = "AddNewPhisicalActivity";

    private Button addNewActivity;

    private EditText editActivity;
    private EditText editBurns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_phisical);



        addNewActivity = (Button) findViewById(R.id.add_new_activity);

        editActivity = (EditText) findViewById(R.id.new_activity);
        editBurns = (EditText) findViewById(R.id.new_burned);


        myDbHelper = new PhisicalDatabaseHelper(this);
        try {
            myDbHelper.createDatabase();
        } catch (Exception e) {
            Log.e(TAG, "can't create db", e);
        }
        try {
            myDbHelper.openDatabase();
        } catch (Exception e) {
            Log.e(TAG, "can't open db", e);
        }



        addNewActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (editActivity.getText().toString().trim().length() == 0 ||
                        editBurns.getText().toString().trim().length() == 0)
                {

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "please enter all values!", Toast.LENGTH_SHORT);
                    toast.show();

                }

                else {
                    myDbHelper.insertIntoPhisicalBase(editActivity.getText().toString(), 60,
                                Integer.valueOf(editBurns.getText().toString()));


                    Toast toast = Toast.makeText(getApplicationContext(),
                                "activity added!", Toast.LENGTH_SHORT);
                    toast.show();

                }
            }
        });





    }
}
