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

public class AddNewProduct extends AppCompatActivity {


    private DatabaseHelper myDbHelper;

    private static final String TAG = "AddNewProduct";

    private Button addNewProduct;

    private EditText editProduct;
    private EditText editCalories;
    private EditText editProteins;
    private EditText editFats;
    private EditText editCarbohydrates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);




        addNewProduct = (Button) findViewById(R.id.add_new_product);

        editProduct = (EditText) findViewById(R.id.new_product);
        editCalories = (EditText) findViewById(R.id.new_calories);
        editProteins = (EditText) findViewById(R.id.new_proteins);
        editFats = (EditText) findViewById(R.id.new_fats);
        editCarbohydrates = (EditText) findViewById(R.id.new_carbohydrates);




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


        addNewProduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (editProduct.getText().toString().trim().length() == 0 ||
                        editCalories.getText().toString().trim().length() == 0 ||
                        editProteins.getText().toString().trim().length() == 0 ||
                        editFats.getText().toString().trim().length() == 0 ||
                        editCarbohydrates.getText().toString().trim().length() == 0)
                {

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "please enter all values!", Toast.LENGTH_SHORT);
                    toast.show();

                }
                else {

                    if (!editCalories.getText().toString().matches("^[0-9]*$") ||
                            !editProteins.getText().toString().matches("^[0-9]*$") ||
                            !editFats.getText().toString().matches("^[0-9]*$") ||
                            !editCarbohydrates.getText().toString().matches("^[0-9]*$"))
                    {

                        Toast toast = Toast.makeText(getApplicationContext(),
                                "please enter numbers in calories, proteins, fats, carbohydrates lines!", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                    else {
                        myDbHelper.insertIntoMainDatabase(editProduct.getText().toString(),
                                Integer.valueOf(editCalories.getText().toString()),
                                Integer.valueOf(editProteins.getText().toString()),
                                Integer.valueOf(editFats.getText().toString()),
                                Integer.valueOf(editCarbohydrates.getText().toString()));


                        Toast toast = Toast.makeText(getApplicationContext(),
                                "product added!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });


    }
}
