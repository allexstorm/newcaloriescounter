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
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.sql.SQLException;

public class Information extends AppCompatActivity {


    private Context context = this;

    private OperateBaseHandler operateHandler;

    private Button btnAbout;
    private Button btnChangeLog;
    private Button btnRateThisApp;
    private ListView lstResults;

    private static final String TAG = "Information";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        btnAbout = (Button) findViewById(R.id.information_about_button);
        btnChangeLog = (Button) findViewById(R.id.information_updates_log);
        btnRateThisApp = (Button) findViewById(R.id.information_rate_this_app_btn);

        lstResults = (ListView) findViewById(R.id.information_result_history);


        btnAbout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                showLicense();

            }
        });

        btnChangeLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                showChangeLog();

            }
        });

        btnRateThisApp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                }

            }
        });



        operateHandler = new OperateBaseHandler(this);
        try {
            operateHandler.open();
        } catch (SQLException e) {
            Log.e(TAG, "can't create db", e);
        }


        operateHandler = new OperateBaseHandler(this);
        try {
            operateHandler.open();
        } catch (SQLException e) {
            Log.e(TAG, "can't create operate phisical activity db", e);
        }

        TextView txtTotalResult = (TextView) findViewById(R.id.information_result_total_label);
        txtTotalResult.setText(operateHandler.getTotalResult());


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                operateHandler.getResultIntakeInfo() );
        lstResults.setAdapter(arrayAdapter);


    }


    private void showLicense() {
        License license = new License(this);
        View mLicense = license.getView();
        mLicense.refreshDrawableState();
        new AlertDialog.Builder(Information.this)
                .setView(mLicense)
                .show();
    }

    private void showChangeLog() {
        ChangeLog changelog = new ChangeLog(this);
        View mChangeLog = changelog.getView();
        mChangeLog.refreshDrawableState();
        new AlertDialog.Builder(Information.this)
                .setView(mChangeLog)
                .show();
    }

}
