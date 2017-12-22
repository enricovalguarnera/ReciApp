package com.example.android.reciapp;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.reciapp.contentprovider.ContractRecipe;

/**
 * Created by enry_ on 15/07/2016.
 */
public class  DetailActivity extends AppCompatActivity{

    private final String TAG = DetailActivity.class.getSimpleName();

    String totalIngr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        long id = bundle.getLong("id");


        Uri uri = ContractRecipe.Recipe.buildUri(id);

        Cursor cursor =  getContentResolver().query(
                uri,
                null,
                null,
                null,
                null   );

        if(cursor.moveToFirst()) {
            String text_name = cursor.getString(cursor.getColumnIndexOrThrow(ContractRecipe.Recipe.COLUMN_NAME));
            String text_ingredients = cursor.getString(cursor.getColumnIndexOrThrow(ContractRecipe.Recipe.COLUMN_INGREDIENTS));

            ((TextView) findViewById(R.id.recipe_title_textview)).setText(text_name);
            ((TextView) findViewById(R.id.recipe_ingredients_textview)).setText(text_ingredients);
        }
    }
}
