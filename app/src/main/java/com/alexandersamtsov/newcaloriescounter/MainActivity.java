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

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {


    private Context context = this;

    private SimpleCursorAdapter adapter;
    private SimpleCursorAdapter opphisadapter;

    private OperateBaseHandler operateHandler;

    private static final String TAG = "MainActivity";


    private Button btnSettings;
    private Button btnAddPhis;

    private Button btnChoose;
    private TextView txtDayTotal;
    private TextView txtDayTotalBurned;
    private TextView txtCurrentDate;
    private TextView txtExceeded;
    private TextView txtYoucaneatToday;
    private ListView lstChosenProducts;
    private ListView lstChosenActivities;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChoose = (Button) findViewById(R.id.button_choose);
        btnSettings = (Button) findViewById(R.id.main_settings);
        btnAddPhis = (Button) findViewById(R.id.main_add_phisical_activity);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChooseProduct.class);
                startActivity(intent);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

        btnAddPhis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChoosePhisicalActivity.class);
                startActivity(intent);
            }
        });

        txtExceeded = (TextView) findViewById(R.id.main_exceeded);
        txtDayTotal = (TextView) findViewById(R.id.main_day_total);
        txtDayTotalBurned = (TextView) findViewById(R.id.main_daytotal_burned);
        txtCurrentDate = (TextView) findViewById(R.id.main_current_date);
        lstChosenProducts = (ListView) findViewById(R.id.main_chosen_product_list);
        lstChosenActivities = (ListView) findViewById(R.id.main_chosen_phisactivity_list);
        txtYoucaneatToday = (TextView) findViewById(R.id.main_youcaneat_today);


        SavedData prefs = new SavedData();

        //go to settings if app starts first time
        if (!prefs.getStartedVarsCreated(this))
        {

            Intent intent = new Intent(MainActivity.this, Settings.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        operateHandler = new OperateBaseHandler(this);
        try {
            operateHandler.open();
        } catch (SQLException e) {
            Log.e(TAG, "can't create db", e);
        }

        if (operateHandler.getOnlyDayCalories() - operateHandler.getOperatePhisicalBurned() > prefs.getFinalNeeds(this)) {
            int exceeded = operateHandler.getOnlyDayCalories() -
                    operateHandler.getOperatePhisicalBurned() - prefs.getFinalNeeds(this);
            txtExceeded.setText(String.format(getString(R.string.exceeded),
                    exceeded));
            txtExceeded.setTextColor(Color.parseColor("#e40114"));
        }
        else
        {
            txtExceeded.setText(R.string.eatmore);
            txtExceeded.setTextColor(Color.parseColor("#177b03"));
        }


            txtDayTotal.setText(operateHandler.getCalories());
        txtCurrentDate.setText(operateHandler.getDate());

        txtYoucaneatToday.setText(String.format(getString(R.string.you_can_eat_today),prefs.getFinalNeeds(this) +
                operateHandler.getOperatePhisicalBurned()));

        Cursor c = operateHandler.readData();



        String[] from = new String[] { "product", "amount", "calories", "proteins", "fats", "carbohydrates" };
        int[] to = new int[] { R.id.product, R.id.amount, R.id.calories, R.id.proteins, R.id.fats, R.id.carbohydrates };
        adapter = new SimpleCursorAdapter(this, R.layout.day_list, c, from, to, 1);
        lstChosenProducts.setAdapter(adapter);


        lstChosenProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                SQLiteCursor cursor = (SQLiteCursor) parent.getItemAtPosition(position);
                final int pos = cursor.getInt(cursor.getColumnIndex("_id"));

                operateHandler.deleteProduct(pos);


                txtDayTotal.setText(operateHandler.getCalories());

                SavedData prefs = new SavedData();
                if (operateHandler.getOnlyDayCalories() - operateHandler.getOperatePhisicalBurned() > prefs.getFinalNeeds(context)) {
                    int exceeded = operateHandler.getOnlyDayCalories() -
                            operateHandler.getOperatePhisicalBurned() - prefs.getFinalNeeds(context);
                    txtExceeded.setText(String.format(getString(R.string.exceeded),
                            exceeded));
                    txtExceeded.setTextColor(Color.parseColor("#e40114"));
                }
                else
                {
                    txtExceeded.setText(R.string.eatmore);
                    txtExceeded.setTextColor(Color.parseColor("#177b03"));
                }

                cursor.requery();


            }
        });



        txtDayTotalBurned.setText(String.valueOf(operateHandler.getOperatePhisicalBurned()));

        Cursor opphiscursor = operateHandler.readOperatePhisicalData();

        String[] opphis_from = new String[] { "activity", "burned", "time" };
        int[] opphis_to = new int[] { R.id.day_activity_name, R.id.burned_amount, R.id.burned_time };
        opphisadapter = new SimpleCursorAdapter(this, R.layout.day_activitylist,
                opphiscursor, opphis_from, opphis_to, 1);
        lstChosenActivities.setAdapter(opphisadapter);


        lstChosenActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                SQLiteCursor opphiscursor = (SQLiteCursor) parent.getItemAtPosition(position);
                final int opphispos = opphiscursor.getInt(opphiscursor.getColumnIndex("_id"));

                operateHandler.deleteOperateActivity(opphispos);


                txtDayTotalBurned.setText(String.valueOf(operateHandler.getOperatePhisicalBurned()));


                SavedData prefs = new SavedData();
                txtYoucaneatToday.setText(String.format(getString(R.string.you_can_eat_today),prefs.getFinalNeeds(context) +
                        operateHandler.getOperatePhisicalBurned()));


                if (operateHandler.getOnlyDayCalories() - operateHandler.getOperatePhisicalBurned() > prefs.getFinalNeeds(context)) {
                    int exceeded = operateHandler.getOnlyDayCalories() -
                            operateHandler.getOperatePhisicalBurned() - prefs.getFinalNeeds(context);
                    txtExceeded.setText(String.format(getString(R.string.exceeded),
                            exceeded));
                    txtExceeded.setTextColor(Color.parseColor("#e40114"));
                }
                else
                {
                    txtExceeded.setText(R.string.eatmore);
                    txtExceeded.setTextColor(Color.parseColor("#177b03"));
                }

                opphiscursor.requery();




            }
        });






    }

    @Override
    protected void onResume() {
        super.onResume();


        SavedData prefs = new SavedData();
        //go to settings if app starts first time
        if (!prefs.getStartedVarsCreated(this))
        {

            Intent intent = new Intent(MainActivity.this, Settings.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please enter all values and click update button!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            startActivity(intent);
        }





        if (operateHandler.getOnlyDayCalories() - operateHandler.getOperatePhisicalBurned() > prefs.getFinalNeeds(this)) {
            int exceeded = operateHandler.getOnlyDayCalories() -
                    operateHandler.getOperatePhisicalBurned() - prefs.getFinalNeeds(this);
            txtExceeded.setText(String.format(getString(R.string.exceeded),
                    exceeded));
            txtExceeded.setTextColor(Color.parseColor("#e40114"));
        }
        else
        {
            txtExceeded.setText(R.string.eatmore);
            txtExceeded.setTextColor(Color.parseColor("#177b03"));
        }


        txtDayTotal.setText(operateHandler.getCalories());
        txtCurrentDate.setText(operateHandler.getDate());

        txtYoucaneatToday.setText(String.format(getString(R.string.you_can_eat_today), prefs.getFinalNeeds(this) +
                operateHandler.getOperatePhisicalBurned()));



        Cursor c = operateHandler.readData();
        String[] from = new String[] { "product", "amount", "calories", "proteins", "fats", "carbohydrates" };
        int[] to = new int[] { R.id.product, R.id.amount, R.id.calories, R.id.proteins, R.id.fats, R.id.carbohydrates };
        adapter = new SimpleCursorAdapter(this, R.layout.day_list, c, from, to, 1);
        lstChosenProducts.setAdapter(adapter);


        lstChosenProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                SQLiteCursor cursor = (SQLiteCursor) parent.getItemAtPosition(position);
                final int pos = cursor.getInt(cursor.getColumnIndex("_id"));

                operateHandler.deleteProduct(pos);


                txtDayTotal.setText(operateHandler.getCalories());

                SavedData prefs = new SavedData();
                if (operateHandler.getOnlyDayCalories() - operateHandler.getOperatePhisicalBurned() > prefs.getFinalNeeds(context)) {
                    int exceeded = operateHandler.getOnlyDayCalories() -
                            operateHandler.getOperatePhisicalBurned() - prefs.getFinalNeeds(context);
                    txtExceeded.setText(String.format(getString(R.string.exceeded),
                            exceeded));
                    txtExceeded.setTextColor(Color.parseColor("#e40114"));
                }
                else
                {
                    txtExceeded.setText(R.string.eatmore);
                    txtExceeded.setTextColor(Color.parseColor("#177b03"));
                }

                cursor.requery();


            }
        });






        txtDayTotalBurned.setText(String.valueOf(operateHandler.getOperatePhisicalBurned()));

        Cursor opphiscursor = operateHandler.readOperatePhisicalData();

        String[] opphis_from = new String[] { "activity", "burned", "time" };
        int[] opphis_to = new int[] { R.id.day_activity_name, R.id.burned_amount, R.id.burned_time };
        opphisadapter = new SimpleCursorAdapter(this, R.layout.day_activitylist,
                opphiscursor, opphis_from, opphis_to, 1);
        lstChosenActivities.setAdapter(opphisadapter);


        lstChosenActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                SQLiteCursor opphiscursor = (SQLiteCursor) parent.getItemAtPosition(position);
                final int opphispos = opphiscursor.getInt(opphiscursor.getColumnIndex("_id"));

                operateHandler.deleteOperateActivity(opphispos);


                txtDayTotalBurned.setText(String.valueOf(operateHandler.getOperatePhisicalBurned()));

                SavedData prefs = new SavedData();
                txtYoucaneatToday.setText(String.format(getString(R.string.you_can_eat_today),prefs.getFinalNeeds(context) +
                        operateHandler.getOperatePhisicalBurned()));


                if (operateHandler.getOnlyDayCalories() - operateHandler.getOperatePhisicalBurned() > prefs.getFinalNeeds(context)) {
                    int exceeded = operateHandler.getOnlyDayCalories() -
                            operateHandler.getOperatePhisicalBurned() - prefs.getFinalNeeds(context);
                    txtExceeded.setText(String.format(getString(R.string.exceeded),
                            exceeded));
                    txtExceeded.setTextColor(Color.parseColor("#e40114"));
                }
                else
                {
                    txtExceeded.setText(R.string.eatmore);
                    txtExceeded.setTextColor(Color.parseColor("#177b03"));
                }

                opphiscursor.requery();


            }
        });




    }

    @Override
    protected void onStop() {
        super.onStop();



    }




}
