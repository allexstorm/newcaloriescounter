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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class OperateBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "OperateBaseHelper";

    private SQLiteDatabase myOperateBase;
    private final Context myOperateContext;
    private static final String OPERATE_DATABASE_NAME = "operate.db";
    private static final String OPERATE_TABLE_NAME = "operate";

    public static final String ID_OPERATE = "_id";
    public static final String DATE_OPERATE = "date";
    public static final String PRODUCT_OPERATE = "product";
    public static final String AMOUNT_OPERATE = "amount";
    public static final String CALORIES_OPERATE = "calories";
    public static final String PROTEINS_OPERATE = "proteins";
    public static final String FATS_OPERATE = "fats";
    public static final String CARBOHYDRATES_OPERATE = "carbohydrates";

    private static final String PHISICAL_TABLE_NAME = "operatephisical";
    public static final String ID_PHISICAL = "_id";
    public static final String DATE_PHISICAL = "phisdate";
    public static final String ACTIVITY_PHISICAL = "activity";
    public static final String TIME_PHISICAL = "time";
    public static final String BURNED_PHISICAL = "burned";

    private static String DATABASE_PATH = "";
    private static final int DATABASE_VERSION = 16;



    public OperateBaseHelper(Context context)
    {
        super(context, OPERATE_DATABASE_NAME, null, DATABASE_VERSION);
        this.myOperateContext = context;
        DATABASE_PATH = myOperateContext.getDatabasePath(OPERATE_DATABASE_NAME).toString();
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + OPERATE_TABLE_NAME + " (" +
                    ID_OPERATE + " INTEGER PRIMARY KEY, " +
                    DATE_OPERATE + " TEXT, " +
                    PRODUCT_OPERATE + " TEXT, " + AMOUNT_OPERATE + " NUMERIC, " + CALORIES_OPERATE + " NUMERIC, " +
                    PROTEINS_OPERATE + " NUMERIC, " + FATS_OPERATE + " NUMERIC, " +
                    CARBOHYDRATES_OPERATE + " NUMERIC )";

    private static final String SQL_CREATE_ENTRIES_PHISICAL =
            "CREATE TABLE " + PHISICAL_TABLE_NAME + " (" +
                    ID_PHISICAL + " INTEGER PRIMARY KEY, " +
                    DATE_PHISICAL + " TEXT, " +
                    ACTIVITY_PHISICAL + " TEXT, " + TIME_PHISICAL + " NUMERIC, " +
                    BURNED_PHISICAL + " NUMERIC)";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + OPERATE_TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES_PHISICAL =
            "DROP TABLE IF EXISTS " + PHISICAL_TABLE_NAME;




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES_PHISICAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES_PHISICAL);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}
