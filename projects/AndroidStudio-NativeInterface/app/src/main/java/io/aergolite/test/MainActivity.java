package io.aergolite.test;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteStatement;
import org.sqlite.database.sqlite.SQLiteException;
import android.database.Cursor;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> resultsAdapter;

    MyApplication app = null;
    SQLiteDatabase db = null;

    void logError(String result) {
        android.util.Log.e("AergoLiteTest", result);
        if (app!=null) app.showMessage(result);
        //android.widget.Toast.makeText(this, result, 50);
    }

    void logResult(String result) {
        android.util.Log.i("AergoLiteTest", result);
        resultsAdapter.add(result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInsertClick();
            }
        });

        resultsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView lv1 = findViewById(R.id.listview);
        lv1.setAdapter(resultsAdapter);

        app = (MyApplication) this.getApplication();
        db = app.getConnection();
        app.setCurrentActivity(this);

    }

    @Override
    protected void onDestroy(){
        app.setCurrentActivity(null);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            onUpdateClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onUpdateClick()
    {

        try {
            //SQLiteStatement statement;

            resultsAdapter.clear();

            //statement = db.compileStatement("SELECT count(*) as num FROM tasks");
            //long num = statement.simpleQueryForLong();
            //logResult("items in db: " + num);
            //statement.close();

            Cursor c = db.rawQuery("SELECT * FROM tasks", null);
            if (c.moveToFirst()) {
                do {
                    String name = c.getString(c.getColumnIndex("name"));
                    logResult(name);
                } while (c.moveToNext());
            }
            c.close();

        } catch (SQLiteException ex) {
            logError("on update: " + ex.toString());
        }

    }

    public void onInsertClick()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Add Task");
        //alert.setMessage("Task description:");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            String value = input.getText().toString();
            insertTask(value);
            dialog.dismiss();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            dialog.dismiss();
            }
        });

        alert.show();

    }

    public void insertTask(String name)
    {

        try {
            SQLiteStatement statement;

            statement = db.compileStatement("INSERT INTO tasks (name,done) VALUES (?,0)");
            statement.bindString(1, name);
            while (statement.executeInsert() == 0) {
                logError("ERROR: INSERT failed");
            }
            statement.close();

            android.util.Log.i("AergoLiteTest", "item inserted");

            onUpdateClick();

        } catch (SQLiteException ex) {
            logError("on insertTask: " + ex.toString());
        }

    }

    public void on_db_ready() {

        onUpdateClick();

    }

}
