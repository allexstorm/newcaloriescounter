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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class OperateBaseHandler {


    private OperateBaseHelper operateHelper;
    private SQLiteDatabase db;
    private Context context;

    public OperateBaseHandler(Context c) {
        context = c;
    }


    public OperateBaseHandler open() throws SQLException {
        operateHelper = new OperateBaseHelper(context);
        db = operateHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        operateHelper.close();
    }

    public void insertData(String date, String product, int amount, int calories, int proteins, int fats, int carbohydrates)
    {
        String insertQuery = "INSERT INTO " +
                "operate" +
                " (date, product, amount, calories, proteins, fats, carbohydrates) VALUES " + "('"+date+"', '"+product+"', '"+amount+"', '"+calories+"', '"+proteins+"', '"+fats+"','"+carbohydrates+"')";
        db.execSQL(insertQuery);
    }

    public Cursor readData() {
        SimpleDateFormat newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String date = newDate.format(new Date());
        Cursor c = db.rawQuery("SELECT _id, product, amount, calories, proteins, fats, carbohydrates FROM operate WHERE date = '"+date+"'", null);
        c.moveToFirst();
        return c;
    }


    public void deleteProduct(int pos)
    {
        db.delete("operate", "_id="+pos, null);
    }


    public String getCalories()
    {
        SimpleDateFormat newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String date = newDate.format(new Date());
        String calories = "";
        Cursor c = db.rawQuery("SELECT SUM(calories), SUM(proteins), SUM(fats), SUM(carbohydrates) FROM operate WHERE date = '"+date+"'", null);
        if(c != null && c.moveToFirst()) {
            calories = c.getString(0) + ":" + c.getString(1) + ":" + c.getString(2) + ":" + c.getString(3);
        }
        if (c != null) {
            c.close();
        }

        return calories;


    }


    public int getOnlyDayCalories()
    {
        SimpleDateFormat newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String date = newDate.format(new Date());
        int calories = 0;
        Cursor c = db.rawQuery("SELECT SUM(calories) FROM operate WHERE date = '"+date+"'", null);
        if(c != null && c.moveToFirst()) {
            calories = c.getInt(0);
        }
        if (c != null) {
            c.close();
        }

        return calories;
    }

    public String getDate()
    {
        SimpleDateFormat newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        return newDate.format(new Date());
    }



    public ArrayList<String> getResultIntakeInfo()
    {
        ArrayList<String> result = new ArrayList<>();
        String value;
        Cursor c = db.rawQuery("SELECT _id, date, SUM(calories), SUM(proteins), SUM(fats), SUM(carbohydrates), (SELECT SUM(burned) FROM operatephisical WHERE date=phisdate) FROM operate GROUP BY date ORDER BY operate._id DESC", null);
        if(c != null && c.moveToFirst()) {
            int i = 0;
            do {
                String diff = String.valueOf(c.getInt(2) - c.getInt(6));
                value = c.getString(1) + "\n" + c.getString(2) + ":" + c.getString(3) + ":" + c.getString(4) + ":" + c.getString(5) + ":" + c.getString(6) + ":" + diff;
                result.add(value);
            }
            while (c.moveToNext());

        }

        if (c != null) {
            c.close();
        }


        return result;

    }


    public String getTotalResult() {
        Cursor c = db.rawQuery("SELECT SUM(calories), SUM(proteins), SUM(fats), SUM(carbohydrates), (SELECT SUM(burned) FROM operatephisical) FROM operate", null);
        c.moveToFirst();

        String diff = String.valueOf(c.getInt(0) - c.getInt(4));
        String s = c.getString(0) + ":" + c.getString(1) + ":" + c.getString(2) + ":" + c.getString(3) + ":" + c.getString(4) + ":" + diff;

        c.close();
        return s;


    }






    public void insertOperatePhisData(String date, String activity, int time, int burned)
    {
        String insertQuery = "INSERT INTO " +
                "operatephisical" +
                " (phisdate, activity, time, burned) VALUES " + "('"+date+"', '"+activity+"', '"+time+"', '"+burned+"')";
        db.execSQL(insertQuery);
    }


    public Cursor readOperatePhisicalData() {
        SimpleDateFormat newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String date = newDate.format(new Date());
        Cursor c = db.rawQuery("SELECT _id, activity, time, burned FROM operatephisical WHERE phisdate = '"+date+"'", null);
        c.moveToFirst();

        return c;
    }

    public void deleteOperateActivity(int pos)
    {
        db.delete("operatephisical", "_id=" + pos, null);
    }

    public int getOperatePhisicalBurned() {
        SimpleDateFormat newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String date = newDate.format(new Date());
        int burned = 0;
        Cursor c = db.rawQuery("SELECT SUM(burned) FROM operatephisical WHERE phisdate = '"+date+"'", null);
        if(c != null && c.moveToFirst()) {
            burned = c.getInt(0);
        }
        if (c != null) {
            c.close();
        }


        return burned;
    }

}
