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
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class PhisicalDatabaseHelper extends SQLiteOpenHelper {


    private static final String TAG = "PhisicalDatabaseHelper";

    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final String DATABASE_NAME = "phisical.db";
    private static final String TABLE_NAME = "phisical";

    public static final String PHISICAL_ID = "_id";
    public static final String PHISICAL_ACTIVITY = "activity";
    public static final String PHISICAL_TIME = "time";
    public static final String PHISICAL_BURNED = "burned";

    public static String DATABASE_PATH = "";
    public static final int DATABASE_VERSION = 2;
    //public static final int DATABASE_VERSION_old = 1;

    //Constructor
    public PhisicalDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        DATABASE_PATH = myContext.getDatabasePath(DATABASE_NAME).toString();
    }



    //Create empty database on the system
    public void createDatabase() throws IOException
    {
        boolean dbExist = checkDataBase();
        if(dbExist)
        {
            Log.v("DB Exists", "db exists");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been
            // bumped
            //onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
        }
        boolean dbExist1 = checkDataBase();
        if(!dbExist1)
        {
            this.getReadableDatabase();
            try
            {
                this.close();
                copyDataBase();
            }
            catch (Exception e)
            {
                Log.e(TAG, "Error copying database", e);
            }
        }
    }


    //Check database already exist or not
    private boolean checkDataBase()
    {
        boolean checkDB = false;
        try
        {
            String myPath = DATABASE_PATH;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        }
        catch(Exception e)
        {
            Log.e(TAG, "exception while checking db", e);
        }
        return checkDB;
    }



    //Copies your database from your local assets-folder to the just created empty database in the system folder

    private void copyDataBase() throws IOException
    {
        String outFileName = DATABASE_PATH;
        OutputStream myOutput = new FileOutputStream(outFileName);
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0)
        {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }



    //delete database
    public void db_delete()
    {
        File file = new File(DATABASE_PATH);
        if(file.exists())
        {
            file.delete();
            System.out.println("delete database file.");
        }
    }


    //Open database
    public void openDatabase() throws SQLException
    {
        String myPath = DATABASE_PATH;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDataBase()throws SQLException
    {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }







    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > oldVersion)
        {
            Log.v("Database Upgrade", "Database version higher than old.");
            db_delete();
        }
    }



    public Cursor getAllActivities() {

        Cursor c = myDataBase.query(TABLE_NAME, new String[] {"_id", PHISICAL_ACTIVITY, PHISICAL_TIME, PHISICAL_BURNED},
                null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getActivitiesByName(String inputText) throws android.database.SQLException {
        Log.w(this.getClass().getSimpleName(), inputText);
        Cursor c;
        if (inputText == null  ||  inputText.length () == 0)  {
            c = myDataBase.query(TABLE_NAME, new String[] {"_id", PHISICAL_ACTIVITY, PHISICAL_TIME, PHISICAL_BURNED},
                    null, null, null, null, null);

        }
        else {
            c = myDataBase.query(true, TABLE_NAME, new String[]{"_id", PHISICAL_ACTIVITY, PHISICAL_TIME, PHISICAL_BURNED},
                    PHISICAL_ACTIVITY + " like '%" + inputText + "%'", null, null, null, null, null);
        }
        if (c != null) {
            c.moveToFirst();
        }
        return c;

    }


    public void deleteFromPhisicalBase(String activity)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("phisical", "activity=" + "'"+activity+"'", null);
    }


    public void insertIntoPhisicalBase(String activity, int time, int burned)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertQuery = "INSERT INTO phisical (" + PHISICAL_ACTIVITY + ", " +
                PHISICAL_TIME + ", " + PHISICAL_BURNED + ") VALUES" +
                " ('"+activity+"', '"+time+"', '"+burned+"')";
        db.execSQL(insertQuery);
    }



}
