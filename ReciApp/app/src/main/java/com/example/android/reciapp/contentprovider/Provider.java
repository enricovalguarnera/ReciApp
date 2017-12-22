package com.example.android.reciapp.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by enry_ on 21/07/2016.
 */
public class Provider extends ContentProvider {

    private DbRecipe db;

    private static final String TAG = "provider";

    private static final int CODE_RECIPES = 100;
    private static final int CODE_RECIPE = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ContractRecipe.AUTHORITY;

        matcher.addURI(authority, ContractRecipe.Recipe.TABLE_NAME, CODE_RECIPES);

        matcher.addURI(authority, ContractRecipe.Recipe.TABLE_NAME + "/#", CODE_RECIPE);

        return matcher;
    }
    @Override
    public boolean onCreate() {
        db = new DbRecipe(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {Cursor cursor = null;

        switch (sUriMatcher.match(uri)){

            case CODE_RECIPES:

                cursor = db.getReadableDatabase().query(
                        ContractRecipe.Recipe.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null, sortOrder
                );

                break;

            case CODE_RECIPE:

                cursor = db.getReadableDatabase().query(
                        ContractRecipe.Recipe.TABLE_NAME,
                        projection,
                        "_id" + " = ?",
                        new String[]{uri.getLastPathSegment()},
                        null, null, sortOrder
                );

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri query: " + uri);

        }

        Log.d(TAG, "query: " + uri.toString() + ", " + cursor.getCount());

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {String type = null;

        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPES:
                type = ContractRecipe.Recipe.CONTENT_TYPE;
                break;

            case CODE_RECIPE:
                type = ContractRecipe.Recipe.CONTENT_ITEM_TYPE;
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        Log.d(TAG, "getType: " + uri.toString() + ", " + type);

        return type;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {Uri returnUri = null;
        long id = -1;

        switch (sUriMatcher.match(uri)){

            case CODE_RECIPES:

                id = db.getWritableDatabase().insert(
                        ContractRecipe.Recipe.TABLE_NAME,
                        null, values
                );

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri insert: " + uri);

        }

        returnUri = ContentUris.withAppendedId(uri, id);

        Log.d(TAG, "insert: " + uri.toString() + ", " + returnUri.toString());

        if(!returnUri.getLastPathSegment().equals("-1"))
            getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {int rows = 0;

        final SQLiteDatabase myDb = db.getWritableDatabase();

        switch (sUriMatcher.match(uri)){

            case CODE_RECIPES:
                rows = myDb.delete(ContractRecipe.Recipe.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        if(rows>0)
            getContext().getContentResolver().notifyChange(uri, null);

        Log.d(TAG, "delete: " + uri.toString() + ", " + rows);

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {int rows = 0;

        switch (sUriMatcher.match(uri)){

            case CODE_RECIPE:

                rows = db.getWritableDatabase().update(
                        ContractRecipe.Recipe.TABLE_NAME,
                        values,
                        ContractRecipe.Recipe.COLUMN_ID + " = ?",
                        new String[]{uri.getLastPathSegment()}
                );

                break;

            case CODE_RECIPES:

                rows = db.getWritableDatabase().update(
                        ContractRecipe.Recipe.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        if(rows>0)
            getContext().getContentResolver().notifyChange(uri, null);

        Log.d(TAG, "update: " + uri.toString() + ", " + rows);

        return rows;
    }

    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase myDb = db.getWritableDatabase();
        myDb.beginTransaction();

        int returnCount = 0;

        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPES:
                returnCount = QueryHelper.setBulkInsert(getContext(), ContractRecipe.Recipe.TABLE_NAME, myDb, values);
                break;

            default:
                throw new UnsupportedOperationException("Unknow uri bulkInsert: " + uri);
        }

        Log.i(TAG, "bulk insert: " + uri.toString() + ", " + returnCount);

        if(returnCount > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return returnCount;
    }
}
