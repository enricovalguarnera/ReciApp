package com.example.android.reciapp.contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by enry_ on 25/07/2016.
 */
public class QueryHelper {

    public static final int setBulkInsert(Context context, String table, SQLiteDatabase db, ContentValues[] values){
        int returnCount = 0;

        try{

            for(ContentValues value : values) {

                final long _id = db.replace(table, null, value);

                if(_id != -1)
                    returnCount++;
            }

            db.setTransactionSuccessful();

        }finally {
            db.endTransaction();
        }

        return returnCount;
    }
}
