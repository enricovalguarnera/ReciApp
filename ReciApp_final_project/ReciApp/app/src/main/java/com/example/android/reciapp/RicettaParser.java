package com.example.android.reciapp;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.reciapp.contentprovider.ContractRecipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by enry_ on 14/07/2016.
 */
public class RicettaParser {

    private final String myUrl = "http://api.yummly.com/v1/api/recipes?_app_id=97a0e4f3&_app_key=b4eb9c770bcc43b1556dc1e03ffa49f1";
    private Context context;
    ContentValues[] cv;

    public RicettaParser(Context context){
        this.context = context;
    }

    public void getData() {
        try {
                //il seguente metodo restituisce la stringa json
                String recipeJsonStr = DownloadRicette();

                //Do la stringa in pasto alla funzione di parsing cosi da decifrare tutti i dati , che poi verranno caricati
                //tramite una insert nel db
                parseRicetta(recipeJsonStr);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String DownloadRicette() throws IOException {

        InputStream is = null;
        String recipeJsonStr = null;

        try{
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* millisecondi */);
            conn.setConnectTimeout(15000 /* millisencodi */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            is = conn.getInputStream();

            StringBuffer buffer = new StringBuffer();

            if(is == null)
                return null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0)
                return null;

            recipeJsonStr = buffer.toString();

        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(is != null)
                is.close();
        }

        return recipeJsonStr;
    }


    private void parseRicetta(String recipeJsonStr) throws JSONException{

        final String OWN_MATCHES = "matches";
        final String OWN_RECIPE_NAME = "recipeName";
        final String OWN_IMAGE_URL = "imageUrlsBySize";
        final String OWN_IMAGE_SIZE = "90";
        final String OWN_INGREDIENTS = "ingredients";


        JSONObject recipeJson = new JSONObject(recipeJsonStr);
        JSONArray recipeArray = recipeJson.getJSONArray(OWN_MATCHES);

        cv = new ContentValues[recipeArray.length()];

        for (int i = 0; i < recipeArray.length(); i++) {
            String recipe;
            String imageUrl;
            String jsonIngredients;
            String[] tmpIng;
            String ingredients = null;

            JSONObject recipeObj = recipeArray.getJSONObject(i);   //Accede a un singolo record del JSON
            recipe = recipeObj.getString(OWN_RECIPE_NAME);

            JSONObject recipeImageJson  = recipeObj.getJSONObject(OWN_IMAGE_URL);
            imageUrl = recipeImageJson.getString(OWN_IMAGE_SIZE);

            jsonIngredients = recipeObj.getString(OWN_INGREDIENTS);

            //Prendo la sottostringa contenente solo la serie di ingredienti senza parentesi quadre
            jsonIngredients = jsonIngredients.substring(1,jsonIngredients.length()-1);

            tmpIng = jsonIngredients.split(",");
            ingredients = tmpIng[0].substring(1, tmpIng[0].length()-1);
            for (int y = 1; y < tmpIng.length; y++){
                String tmp = tmpIng[y];
                tmp = tmp.substring(1, tmp.length()-1);
                ingredients = ingredients + "\n" + tmp;
            }

            //carico i dati in un content Values cosi da inserirli nel db , tramite ContentResolver.insert()
            cv[i] = new ContentValues();

            cv[i].put(ContractRecipe.Recipe.COLUMN_NAME, recipe);
            cv[i].put(ContractRecipe.Recipe.COLUMN_IMG_URL, imageUrl);
            cv[i].put(ContractRecipe.Recipe.COLUMN_INGREDIENTS, ingredients);




        }
        context.getContentResolver().bulkInsert(ContractRecipe.Recipe.CONTENT_URI , cv);
    }
}
