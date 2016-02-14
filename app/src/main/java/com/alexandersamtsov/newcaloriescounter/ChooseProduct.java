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

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;

public class ChooseProduct extends Activity {


    private static final String TAG = "ChooseProduct";
    private DatabaseHelper myDbHelper;

    private Button btnAddtoBase;

    private SimpleCursorAdapter dataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_product);

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


        btnAddtoBase = (Button) findViewById(R.id.addtobasebutton);
        btnAddtoBase.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ChooseProduct.this, AddNewProduct.class);
                startActivity(intent);
            }
        });

        displayListView();

    }



    private void displayListView() {


        Cursor cursor = myDbHelper.getAllProducts();
        String[] columns = new String[] {
                DatabaseHelper.PRODUCT_COLUMN_NAME
        };

        int[] to = new int[] {
                R.id.name
        };

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.products,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(dataAdapter);




        final EditText myFilter = (EditText) findViewById(R.id.myFilter);
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
                return myDbHelper.getProductsByName(constraint.toString());
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                String myProduct =
                        cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
                String myCalories =
                        cursor.getString(cursor.getColumnIndexOrThrow("calories_value"));
                String myProteins =
                        cursor.getString(cursor.getColumnIndexOrThrow("proteins"));
                String myFats =
                        cursor.getString(cursor.getColumnIndexOrThrow("fats"));
                String myCarbohydrates =
                        cursor.getString(cursor.getColumnIndexOrThrow("carbohydrates"));

                Intent intent = new Intent(ChooseProduct.this, ProductInformation.class);
                intent.putExtra("productname", myProduct);
                intent.putExtra("caloriesvalue", Integer.parseInt(myCalories));
                intent.putExtra("proteins", Integer.parseInt(myProteins));
                intent.putExtra("fats", Integer.parseInt(myFats));
                intent.putExtra("carbohydrates", Integer.parseInt(myCarbohydrates));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


    }



}
