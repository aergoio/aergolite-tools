package io.aergolite.test;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;

import android.os.Handler;
import android.util.Log;

//import org.json.JSONObject;
import java.io.File;

import org.sqlite.database.sqlite.SQLiteDatabase;
//import org.sqlite.database.sqlite.SQLiteStatement;
import org.sqlite.database.sqlite.SQLiteException;


public class MyApplication extends Application {

    SQLiteDatabase db = null;

    MainActivity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        ConnectToDb();
    }

    public SQLiteDatabase getConnection() {
        return db;
    }

    public void setCurrentActivity(MainActivity activity) {
        this.currentActivity = activity;
    }

    public void ConnectToDb() {

        try {

            System.loadLibrary("sqliteX");

            File dbfile = new File(getFilesDir(), "test.db");
            //File dbfile = currentActivity.getApplicationContext().getDatabasePath("test.db");
            //String uriStr = dbfile.getAbsolutePath();
            String uriStr = "file:" + dbfile.getAbsolutePath() + "?blockchain=on&discovery=local:4329&num_nodes=3";
            //String uriStr = "file:" + dbfile.getAbsolutePath();

            //currentActivity.getApplicationContext();
            //String dbpath = "/data/data/"+ Context.getPackageName() + "/databases/";

            Log.w("uri:", uriStr);

            try {
                db = SQLiteDatabase.openOrCreateDatabase(uriStr, null);
            } catch (SQLiteException ex) {
                showMessage("DB open: " + ex.toString());
                return;
            }

            on_db_ready();

        } catch (java.lang.Exception ex) {
            showMessage("ConnectToDb: " + ex.toString());
        }

    }


    final Handler handler = new Handler();

    Runnable reload_values = new Runnable() {
        @Override
        public void run() {
            try {
                //onUpdateClick();
                if (currentActivity != null) {
                    currentActivity.on_db_ready();
                }
            } catch (Exception e) {
                showMessage("reload_values: " + e.toString());
            } finally {
                // call the same runnable again
                handler.postDelayed(this, 500);
            }
        }
    };


    public void on_db_ready() {

        prepare_db();

        //if (currentActivity != null) {
        //    currentActivity.on_db_ready();
        //}

        handler.postDelayed(reload_values, 0);

    }


    public void prepare_db() {

        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY,name,done)");
        } catch (SQLiteException ex) {
            showMessage("create table: " + ex.toString());
            db.close();
        }

    }


    public void showMessage(String msg){
        //Snackbar snackbar = Snackbar.make(coordinatorLayout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);
        //snackbar.show();

        if(this.currentActivity == null){
            Log.e("", msg);
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this.currentActivity).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

}
