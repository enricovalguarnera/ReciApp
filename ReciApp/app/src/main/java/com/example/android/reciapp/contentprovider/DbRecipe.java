package com.example.android.reciapp.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by enry_ on 21/07/2016.
 */
public class DbRecipe extends SQLiteOpenHelper {

    private static final String DB_NAME = "recipe.db";
    private static final int DB_VERSION = 1;

    public DbRecipe(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Statements.RECIPE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nothing to do
    }
}
