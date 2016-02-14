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

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ChoosePhisicalActivity extends AppCompatActivity {


    private static final String TAG = "ChoosePhisicalActivity";
    private PhisicalDatabaseHelper myDbHelper;


    private Button btnAddact;
    private SimpleCursorAdapter dataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_phisical);

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




        btnAddact = (Button) findViewById(R.id.choosephisical_add_button);
        btnAddact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ChoosePhisicalActivity.this, AddNewPhisicalActivity.class);
                startActivity(intent);
            }
        });

        displayListView();



    }



    private void displayListView() {


        Cursor cursor = myDbHelper.getAllActivities();

        String[] columns = new String[] {
                PhisicalDatabaseHelper.PHISICAL_ACTIVITY
        };

        int[] to = new int[] {
                R.id.phisical_name
        };

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.phisicalactivities,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.choosephisical_listView1);
        listView.setAdapter(dataAdapter);

        final EditText myFilter = (EditText) findViewById(R.id.choosephisical_myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return myDbHelper.getActivitiesByName(constraint.toString());
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {

                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                String myActivity =
                        cursor.getString(cursor.getColumnIndexOrThrow("activity"));
                String myTime =
                        cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String myBurned =
                        cursor.getString(cursor.getColumnIndexOrThrow("burned"));

                Intent intent = new Intent(ChoosePhisicalActivity.this, PhisicalActivityInformation.class);
                intent.putExtra("activity", myActivity);
                intent.putExtra("time", Integer.parseInt(myTime));
                intent.putExtra("burned", Integer.parseInt(myBurned));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });


    }




}
