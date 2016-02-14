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




public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String TAG = "DatabaseHelper";

    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final String DATABASE_NAME = "calories.db";
    private static final String TABLE_NAME = "calories";

    public static final String PRODUCT_COLUMN_NAME = "product_name";
    public static final String CALORIES_COLUMN_NAME = "calories_value";
    public static final String PROTEINS_COLUMN_NAME = "proteins";
    public static final String FATS_COLUMN_NAME = "fats";
    public static final String CARBOHYDRATES_COLUMN_NAME = "carbohydrates";

    private static String DATABASE_PATH = "";
    private static final int DATABASE_VERSION = 2;
    //public static final int DATABASE_VERSION_old = 1;

    //Constructor
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        DATABASE_PATH = myContext.getDatabasePath(DATABASE_NAME).toString();
    }




    //Create a empty database on the system
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
            Log.v(TAG,"delete database file.");
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

    public void onCreate(SQLiteDatabase db)
    {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (newVersion > oldVersion)
        {
            Log.v("Database Upgrade", "Database version higher than old.");
            db_delete();
        }
    }


    public String getProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        c = myDataBase.rawQuery("SELECT product_name FROM calories", null);
        c.moveToFirst();

        String s = "";

        do {
            s += c.getString(c.getColumnIndex("product_name")) + "\r\n";

        }
        while (c.moveToNext());

        c.close();
           return s;


    }


    public Cursor getAllProducts() {

        Cursor c = myDataBase.query(TABLE_NAME, new String[] {"_id", PRODUCT_COLUMN_NAME, CALORIES_COLUMN_NAME,
                        PROTEINS_COLUMN_NAME, FATS_COLUMN_NAME, CARBOHYDRATES_COLUMN_NAME},
                null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getProductsByName(String inputText) throws android.database.SQLException {
        Log.w(this.getClass().getSimpleName(), inputText);
        Cursor c;
        if (inputText == null  ||  inputText.length () == 0)  {
            c = myDataBase.query(TABLE_NAME, new String[] {"_id", PRODUCT_COLUMN_NAME, CALORIES_COLUMN_NAME,
                            PROTEINS_COLUMN_NAME, FATS_COLUMN_NAME, CARBOHYDRATES_COLUMN_NAME},
                    null, null, null, null, null);

        }
        else {
            c = myDataBase.query(true, TABLE_NAME, new String[]{"_id", PRODUCT_COLUMN_NAME, CALORIES_COLUMN_NAME,
                            PROTEINS_COLUMN_NAME, FATS_COLUMN_NAME, CARBOHYDRATES_COLUMN_NAME},
                    PRODUCT_COLUMN_NAME + " like '%" + inputText + "%'", null, null, null, null, null);
        }
        if (c != null) {
            c.moveToFirst();
        }
        return c;

    }

    public void insertIntoMainDatabase(String product, int calories, int proteins, int fats, int carbohydrates)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertQuery = "INSERT INTO calories (" + PRODUCT_COLUMN_NAME + ", " +
                CALORIES_COLUMN_NAME + ", " + PROTEINS_COLUMN_NAME + ", " +
                FATS_COLUMN_NAME + ", " + CARBOHYDRATES_COLUMN_NAME + ") VALUES" +
                " ('"+product+"', '"+calories+"', '"+proteins+"', '"+fats+"','"+carbohydrates+"')";
        db.execSQL(insertQuery);
    }

    public void deleteFromMainBase(String product)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("calories", "product_name=" + "'" + product + "'", null);
    }







}
