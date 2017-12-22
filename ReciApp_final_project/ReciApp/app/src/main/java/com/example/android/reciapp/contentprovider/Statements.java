package com.example.android.reciapp.contentprovider;

/**
 * Created by enry_ on 21/07/2016.
 */
public class Statements {

    private static final String START_STATEMENTS = "create table if not exists ";

    public static final String RECIPE_STATEMENT = START_STATEMENTS + ContractRecipe.Recipe.TABLE_NAME + " (" +
           "_id integer primary key autoincrement, " +
            ContractRecipe.Recipe.COLUMN_ID + " integer, " +
            ContractRecipe.Recipe.COLUMN_NAME + " text, " +
            ContractRecipe.Recipe.COLUMN_IMG_URL + " text," +
            ContractRecipe.Recipe.COLUMN_INGREDIENTS + " text);";

}
