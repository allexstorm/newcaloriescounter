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
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;


public class SavedData {



    private static final String PREFS_NAME = "myprefs";
    private static final String PREFS_STARTED_VARS = "startedVars";

    private static final String PREFS_SEX = "sex";
    private static final String PREFS_AGE = "age";
    private static final String PREFS_WEIGHT = "weight";
    private static final String PREFS_HEIGHT = "height";
    private static final String PREFS_TARGET_WEIGHT = "target";
    private static final String PREFS_CURRENT_WEIGHT = "current";
    private static final String PREFS_DAILY_NEEDS = "needs";
    private static final String PREFS_MODE = "mode";
    private static final String PREFS_FINAL_NEEDS = "final";

    public SavedData() {
        super();
    }


    public void setStartedVarsCreated(Context context, boolean b)
    {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putBoolean(PREFS_STARTED_VARS, b);
        editor.apply();
    }

    public boolean getStartedVarsCreated(Context context)
    {
        SharedPreferences settings;
        Boolean value;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        value = settings.getBoolean(PREFS_STARTED_VARS, false);
        return value;
    }


    public void setSex(Context context, int sex) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt(PREFS_SEX, sex);
        editor.apply();
    }
    public int getSex(Context context) {
        SharedPreferences settings;
        int sex;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sex = settings.getInt(PREFS_SEX, 0);
        return sex;
    }




    public void setAge(Context context, int age) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt(PREFS_AGE, age);
        editor.apply();
    }
    public int getAge(Context context) {
        SharedPreferences settings;
        int age;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        age = settings.getInt(PREFS_AGE, 0);
        return age;
    }




    public void setWeight(Context context, int weight) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt(PREFS_WEIGHT, weight);
        editor.apply();
    }

    public int getWeight(Context context) {
        SharedPreferences settings;
        int weight;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        weight = settings.getInt(PREFS_WEIGHT, 0);
        return weight;
    }

    public void setHeight(Context context, int height) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt(PREFS_HEIGHT, height);
        editor.apply();
    }

    public int getHeight(Context context) {
        SharedPreferences settings;
        int height;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        height = settings.getInt(PREFS_HEIGHT, 0);
        return height;
    }

    public void setTargetWeight(Context context, int target) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt(PREFS_TARGET_WEIGHT, target);
        editor.apply();
    }

    public int getTargetWeight(Context context) {
        SharedPreferences settings;
        int targetWeight;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        targetWeight = settings.getInt(PREFS_TARGET_WEIGHT, 0);
        return targetWeight;
    }

    public void setCurrentWeight(Context context, int current) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit(); //2
        editor.putInt(PREFS_CURRENT_WEIGHT, current);
        editor.apply();
    }
    public int getCurrentWeight(Context context) {
        SharedPreferences settings;
        int currentWeight;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentWeight = settings.getInt(PREFS_CURRENT_WEIGHT, 0);
        return currentWeight;
    }

    public void setDailyNeeds(Context context, int needs) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt(PREFS_DAILY_NEEDS, needs);
        editor.apply();
    }

    public int getDailyNeeds(Context context) {
        SharedPreferences settings;
        int dailyNeed;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        dailyNeed = settings.getInt(PREFS_DAILY_NEEDS, 0);
        return dailyNeed;
    }

    public void setMode(Context context, int mode) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt(PREFS_MODE, mode);
        editor.apply();
    }

    public int getMode(Context context) {
        SharedPreferences settings;
        int mode;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mode = settings.getInt(PREFS_MODE, 0);
        return mode;
    }

    public void setFinalNeeds(Context context, int finalNeeds) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt(PREFS_FINAL_NEEDS, finalNeeds);
        editor.apply();
    }

    public int getFinalNeeds(Context context) {
        SharedPreferences settings;
        int finalNeeds;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        finalNeeds = settings.getInt(PREFS_FINAL_NEEDS, 0);
        return finalNeeds;
    }



}
