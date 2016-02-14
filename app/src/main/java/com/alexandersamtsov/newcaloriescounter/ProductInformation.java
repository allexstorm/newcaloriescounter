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

public class ProductInformation extends AppCompatActivity {


    private OperateBaseHandler operateHandler;
    private DatabaseHelper myDbHelper;

    private static final String TAG = "ProductInformation";

    private Button btnAdd;
    private Button remove;


    private int amount;

    private String product;
    private int calories;
    private int proteins;
    private int fats;
    private int carbohydrates;
    private TextView txtProductName;
    private TextView txtCaloriesName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_information);
        btnAdd = (Button) findViewById(R.id.button_add_product);
        remove = (Button) findViewById(R.id.remove_from_base);

        product = getIntent().getExtras().getString("productname");
        calories = getIntent().getExtras().getInt("caloriesvalue");
        proteins = getIntent().getExtras().getInt("proteins");
        fats = getIntent().getExtras().getInt("fats");
        carbohydrates = getIntent().getExtras().getInt("carbohydrates");

        txtProductName = (TextView)findViewById(R.id.product_information__product_name);
        txtProductName.setText(String.format(getString(R.string.product_name), product));

        txtCaloriesName = (TextView)findViewById(R.id.product_information__product_desc);
        txtCaloriesName.setText(String.format(getString(R.string.product_info), calories, proteins, fats, carbohydrates));



        Spinner spinner = (Spinner) findViewById(R.id.product_information_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.grams, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int item = Integer.parseInt(parent.getItemAtPosition(position).toString()) / 100;
                amount = item*100;

                calories = getIntent().getExtras().getInt("caloriesvalue") * item;
                proteins = getIntent().getExtras().getInt("proteins") * item;
                fats = getIntent().getExtras().getInt("fats") * item;
                carbohydrates = getIntent().getExtras().getInt("carbohydrates") * item;

                txtCaloriesName.setText(String.format(getString(R.string.product_info), calories, proteins, fats, carbohydrates));

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
                Intent intent = new Intent(ProductInformation.this, ChooseProduct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                SimpleDateFormat newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                String date = newDate.format(new Date());

                operateHandler.insertData(date, product, amount, calories, proteins, fats, carbohydrates);

                startActivity(intent);
            }
        });


        myDbHelper = new DatabaseHelper(this);
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


                myDbHelper.deleteFromMainBase(product);


                Toast toast = Toast.makeText(getApplicationContext(),
                        "removed!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });


    }


}
