package com.example.android.reciapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.reciapp.contentprovider.ContractRecipe;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView listView;
    private CursorAdapterRecipe adapter;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        //controllo lo stato della connessione
        ConnectivityManager conman = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNet = conman.getActiveNetworkInfo();

        if(!(activeNet != null && activeNet.isConnectedOrConnecting())){
            Toast.makeText(this, "Connessione Internet Assente! ", Toast.LENGTH_LONG).show();
        }else{


            if(savedInstanceState == null){
                updateRecipeList();
            }
            //esporto il db
            //exportDatabase(this, "recipe.db");
            //initLoader

            listView = (ListView) findViewById(R.id.listview_recipe);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("id", adapter.getItemId(position));
                    startActivity(intent);

                }
            });


        }

        getSupportLoaderManager().initLoader(0, null, this);

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void updateRecipeList() {
        DownloadAsync taskRecipe = new DownloadAsync(this);
        taskRecipe.execute();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                context,
                ContractRecipe.Recipe.CONTENT_URI,
                null,
                null, null , null
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        //instanzio l'adapter e setto sulla list
        adapter = new CursorAdapterRecipe(context, data);
        listView.setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //nothing to do
    }

    //classe task per lo scaricamento, parsing,  e inserimento nel db dei dati
    private class DownloadAsync extends AsyncTask<Void, Void, Void>{

        private ProgressDialog progBar;

        public DownloadAsync(Context context){
            progBar = new ProgressDialog(context);
            progBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progBar.setIndeterminate(true);
            progBar.setMessage("Caricamento dati ...");
            progBar.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            RicettaParser parser = new RicettaParser(context);
            parser.getData();
            progBar.dismiss();
            return null;
        }
    }

    /*

    public static void exportDatabase(MainActivity a, String databaseName) {
        if (MarshMallowPermission.requestWriteStorage(a)) {
            try {
                File sd = Environment.getExternalStorageDirectory();

                if (sd.canWrite()) {
                    File currentDB = a.getDatabasePath(databaseName);
                    File backupDB = getBackupDbFile(databaseName);

                    if (currentDB!=null&&currentDB.exists()) {
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static File getBackupDbFile(String databaseName) {
        File sd = Environment.getExternalStorageDirectory();
        return new File(sd, databaseName);
    }

    */

}
