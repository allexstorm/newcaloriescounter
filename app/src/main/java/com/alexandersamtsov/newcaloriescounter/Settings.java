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

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {


    private Context context = this;

    private Button update;
    private Button btnInfo;



    private int modeId;

    private RadioGroup weightlossMode;
    private RadioButton normal;
    private RadioButton fast;


    private int sexId;

    private RadioButton male;
    private RadioButton female;
    private RadioGroup sexGroup;

    private EditText editAge;
    private EditText editWeight;
    private EditText editHeight;
    private EditText editTargetWeight;
    private EditText editCurrentWeight;

    private TextView txtDailyNeeds;
    private TextView txtFinalNeeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editAge = (EditText) findViewById(R.id.set_age);
        editWeight = (EditText) findViewById(R.id.set_weight);
        editHeight = (EditText) findViewById(R.id.set_height);
        editTargetWeight = (EditText) findViewById(R.id.set_target_weight);
        editCurrentWeight = (EditText) findViewById(R.id.set_current_weight);

        txtDailyNeeds = (TextView) findViewById(R.id.settings_setcalories_needs);
        txtFinalNeeds = (TextView) findViewById(R.id.settings_txt_set_for_weight_loss);

        update = (Button) findViewById(R.id.update_settings);
        btnInfo = (Button) findViewById(R.id.settings_information_button);

        sexGroup = (RadioGroup) findViewById(R.id.settings_sexRadiogroup);
        male = (RadioButton) findViewById(R.id.radioButtonM);
        female = (RadioButton) findViewById(R.id.radioButtonF);

        weightlossMode = (RadioGroup) findViewById(R.id.settings_radgr_choose_weight_loss);
        normal = (RadioButton) findViewById(R.id.settings_normal_weight_loss);
        fast = (RadioButton) findViewById(R.id.settings_fast_weight_loss);


        // we need if condition below to avoid all fields fill with 0 when app starts first time
        SavedData data = new SavedData();
        if (data.getStartedVarsCreated(this)) {

            if (data.getSex(this) == 1) {
                sexGroup.check(R.id.radioButtonM);
                sexId = 1;
            } else if (data.getSex(this) == 2) {
                sexGroup.check(R.id.radioButtonF);
                sexId = 2;
            }

            if (data.getMode(this) == 1) {
                weightlossMode.check(R.id.settings_normal_weight_loss);
                modeId = 1;
            } else if (data.getMode(this) == 2) {
                weightlossMode.check(R.id.settings_fast_weight_loss);
                modeId = 2;
            }

            editAge.setText(String.valueOf(data.getAge(this)));
            editWeight.setText(String.valueOf(data.getWeight(this)));
            editHeight.setText(String.valueOf(data.getHeight(this)));
            editTargetWeight.setText(String.valueOf(data.getTargetWeight(this)));
            editCurrentWeight.setText(String.valueOf(data.getCurrentWeight(this)));
            txtDailyNeeds.setText(String.valueOf(data.getDailyNeeds(this)));
            txtFinalNeeds.setText(String.valueOf(data.getFinalNeeds(this)));
        }




        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (male.isChecked()) {
                    sexId = 1;
                } else {
                    sexId = 2;
                }


            }
        });

        weightlossMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (normal.isChecked()) {
                    modeId = 1;
                } else {
                    modeId = 2;
                }


            }
        });






        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (sexId == 0 || modeId == 0 ||
                        editAge.getText().toString().trim().length() == 0 ||
                        editWeight.getText().toString().trim().length() == 0 ||
                        editHeight.getText().toString().trim().length() == 0 ||
                        editTargetWeight.getText().toString().trim().length() == 0 ||
                        editCurrentWeight.getText().toString().trim().length() == 0) {

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "please enter all values!", Toast.LENGTH_SHORT);
                    toast.show();

                } else {

                    if (!editAge.getText().toString().matches("^[0-9]*$") ||
                            !editWeight.getText().toString().matches("^[0-9]*$") ||
                            !editHeight.getText().toString().matches("^[0-9]*$") ||
                            !editTargetWeight.getText().toString().matches("^[0-9]*$") ||
                            !editCurrentWeight.getText().toString().matches("^[0-9]*$")) {

                        Toast toast = Toast.makeText(getApplicationContext(),
                                "please enter correct values!", Toast.LENGTH_SHORT);
                        toast.show();


                    } else {


                        SavedData data = new SavedData();
                        data.setStartedVarsCreated(context, true);
                        data.setSex(context, sexId);
                        data.setMode(context, modeId);
                        data.setAge(context, Integer.parseInt(editAge.getText().toString()));
                        data.setWeight(context, Integer.parseInt(editWeight.getText().toString()));
                        data.setHeight(context, Integer.parseInt(editHeight.getText().toString()));
                        data.setTargetWeight(context, Integer.parseInt(editTargetWeight.getText().toString()));
                        data.setCurrentWeight(context, Integer.parseInt(editCurrentWeight.getText().toString()));


                        if (sexId == 1) {
                            double needs = (10 * Integer.parseInt(editWeight.getText().toString()) +
                                    6.25 * Integer.parseInt(editHeight.getText().toString()) -
                                    5 * Integer.parseInt(editAge.getText().toString()) + 5) * 1.2;
                            data.setDailyNeeds(context, (int) needs);
                            txtDailyNeeds.setText(String.valueOf((int) needs));
                        } else if (sexId == 2) {
                            double needs = (10 * Integer.parseInt(editWeight.getText().toString()) +
                                    6.25 * Integer.parseInt(editHeight.getText().toString()) -
                                    5 * Integer.parseInt(editAge.getText().toString()) - 161) * 1.2;
                            data.setDailyNeeds(context, (int) needs);
                            txtDailyNeeds.setText(String.valueOf((int) needs));
                        }


                        if (modeId == 1) {
                            double finalNeeds = data.getDailyNeeds(context) -
                                    (data.getDailyNeeds(context) / 100) * 20;
                            data.setFinalNeeds(context, (int) finalNeeds);
                            txtFinalNeeds.setText(String.valueOf((int) finalNeeds));
                        } else if (modeId == 2) {
                            double finalNeeds = data.getDailyNeeds(context) -
                                    (data.getDailyNeeds(context) / 100) * 40;
                            data.setFinalNeeds(context, (int) finalNeeds);
                            txtFinalNeeds.setText(String.valueOf((int) finalNeeds));
                        }


                        Toast toast = Toast.makeText(getApplicationContext(),
                                "updated!", Toast.LENGTH_SHORT);
                        toast.show();


                    }
                }
            }
        });





        btnInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Settings.this, Information.class);
                startActivity(intent);

            }
        });


    }


}
