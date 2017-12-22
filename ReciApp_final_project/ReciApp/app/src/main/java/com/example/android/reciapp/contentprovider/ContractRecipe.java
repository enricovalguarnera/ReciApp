package com.example.android.reciapp.contentprovider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

/**
 * Created by enry_ on 21/07/2016.
 */
public class ContractRecipe {

    public static final String CONTENT = "content://";
    public static final String AUTHORITY = "com.example.android.reciapp.contentprovider";
    public static final String CONTENT_AUTHORITY = CONTENT + AUTHORITY;

    public static final class Recipe{

        public static final String TABLE_NAME = "recipe";

        public static final String COLUMN_ID = TABLE_NAME + "_id";
        public static final String COLUMN_NAME = TABLE_NAME + "_name";
        public static final String COLUMN_IMG_URL = TABLE_NAME + "_imgUrl";
        public static final String COLUMN_INGREDIENTS = TABLE_NAME + "_ingredients";

        public static final Uri CONTENT_URI = Uri.parse(CONTENT_AUTHORITY + "/" + TABLE_NAME);


        public static Uri buildUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

    }

}
