package com.example.android.reciapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.reciapp.contentprovider.ContractRecipe;
import com.squareup.picasso.Picasso;

/**
 * Created by enry_ on 21/07/2016.
 */
public class CursorAdapterRecipe extends CursorAdapter {

    public CursorAdapterRecipe(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById(R.id.item_textview);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_imageview);

        String text = cursor.getString(cursor.getColumnIndexOrThrow(ContractRecipe.Recipe.COLUMN_NAME));
        String imgUrl = cursor.getString(cursor.getColumnIndexOrThrow(ContractRecipe.Recipe.COLUMN_IMG_URL));

        //new DownloadImageTask(imageView).execute(imgUrl);
        Picasso.with(context).load(imgUrl).into(imageView);

        textView.setText(text);

    }

    //ritona l'id del cursor alla posizione indicata
    public long getItemId(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        return cursor.getLong(cursor.getColumnIndex("_id"));
    }
}
