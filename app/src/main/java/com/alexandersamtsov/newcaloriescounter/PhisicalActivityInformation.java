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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhisicalActivityInformation extends AppCompatActivity {


    private OperateBaseHandler operateHandler;
    private PhisicalDatabaseHelper myDbHelper;

    private static final String TAG = "PhisActInformation";

    private Button btnAdd;
    private Button remove;



    private String activity;
    private int time;
    private double burned;
    private TextView txtActivityName;
    private TextView txtBurnedName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phisical_activity_information);

        btnAdd = (Button) findViewById(R.id.phisinfo_button_add_activity);
        remove = (Button) findViewById(R.id.phisinfo_remove_from_base);

        activity = getIntent().getExtras().getString("activity");
        time = getIntent().getExtras().getInt("time");
        burned = getIntent().getExtras().getInt("burned");


        txtActivityName = (TextView)findViewById(R.id.phisical_information_activity_name);
        txtActivityName.setText(String.format(getString(R.string.activity_name), activity));
        txtBurnedName = (TextView)findViewById(R.id.phisical_information_activity_desc);
        txtBurnedName.setText(String.format(getString(R.string.activity_info), time, burned));




        final Spinner spinner = (Spinner) findViewById(R.id.phisical_information_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(3);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                time = Integer.parseInt(parent.getItemAtPosition(position).toString());

                burned = (double)(getIntent().getExtras().getInt("burned"))/60 * time;

                txtBurnedName.setText(String.format(getString(R.string.activity_info), time, (int)burned));

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });




        operateHandler = new OperateBaseHandler(this);
        try {
            operateHandler.open();
        } catch (SQLException e) {
            Log.e(TAG, "can't create db", e);
        }


        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PhisicalActivityInformation.this, ChoosePhisicalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                SimpleDateFormat newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                String date = newDate.format(new Date());

                operateHandler.insertOperatePhisData(date, activity, time, (int)burned);

                startActivity(intent);
            }
        });















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

        remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                myDbHelper.deleteFromPhisicalBase(activity);


                Toast toast = Toast.makeText(getApplicationContext(),
                        "removed!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });







    }
}
